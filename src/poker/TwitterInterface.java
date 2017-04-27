package poker;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Orla on 20/03/2017.
 */
public class TwitterInterface  {
    private final Object lock = new Object();
    TwitterFactory twitterFactory;
    Twitter twitter;
    List<Status> tweets;
    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;
    public String twittername;
    public long statusId;
    Configuration config;
    TwitterStream stream;
    GameOfPoker game;
    ArrayList<GameOfPoker> gamelist = new ArrayList();

    public TwitterInterface() throws IOException {

        config=setConfiguration();
        twitterFactory = new TwitterFactory(config);
        twitter = twitterFactory.getInstance();
    }
    public Configuration setConfiguration() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        Configuration configuration;
        try {

            BufferedReader reader = new BufferedReader(new FileReader(NAMES_FILE));
            String[] line = new String[NAMES_FILE_LENGTH];
            for(int i = 0; i < NAMES_FILE_LENGTH; i++) {
                line[i] = reader.readLine();

            }
            configurationBuilder.setDebugEnabled(true)
                    .setOAuthConsumerKey(line[0])
                    .setOAuthConsumerSecret(line[1])
                    .setOAuthAccessToken(line[2])
                    .setOAuthAccessTokenSecret(line[3]);
            reader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        configuration = configurationBuilder.build();
        return configuration;
    }


    public void postMessagetoUser(String answer, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;
        answer= humanPlayer.getName()+" "+answer;
        if(answer.length() > 144){
            answer = answer.substring(0, 144);
        }

        long replyId = humanPlayer.getTweetId();

        StatusUpdate statusReply = new StatusUpdate(answer);
        statusReply.setInReplyToStatusId(replyId);

        try {
            twitter.updateStatus(statusReply);
            System.out.println(answer);
            List<Status> h = twitter.getHomeTimeline();
            for (int i = 0; i < h.size(); i++) {
                if (h.get(i).getInReplyToStatusId() == replyId) {
                    humanPlayer.setTweetId(h.get(i).getId());
                    break;
                }
            }
        } catch (TwitterException ex) {
            // handle exception
        }
    }

    public void postImagetoUser(PlayingCard[] currentHand, String answer, PokerPlayer pokerPlayer) {

        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;
        Picture picture = new Picture(currentHand);
        picture.createPicture();

        File file = new File("src/twitter_output/hand_picture.png");
        long replyId = humanPlayer.getTweetId();

        StatusUpdate statusReply = new StatusUpdate(answer);
        statusReply.setInReplyToStatusId(replyId);
        statusReply.setMedia(file); // attach image rather than file?

        try {
            twitter.updateStatus(statusReply);
            System.out.println("Status reply successfull " + currentHand);
            List<Status> h = twitter.getHomeTimeline();
            System.out.println(h.get(0).getText());
            for (int i = 0; i < h.size(); i++) {
                if (h.get(i).getInReplyToStatusId() == replyId) {
                    humanPlayer.setTweetId(h.get(i).getId());
                    break;
                }
            }
        } catch (TwitterException ex) {
            // handle exception
        }
    }


    //Should be removed before submission. Rename to postMessagetoUser to easily switch to console output instead of twitter.
    public void testpostMessagetoUser(String answer, PokerPlayer pokerPlayer) {
        System.out.println(answer);
    }


    public String getTweetfromUser(String word, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;
        String message;
        Status status = null;
        try {
            Query query = new Query(word);
            System.out.println("query string: " + query.getQuery());

            try {
                query.setCount(100);

            } catch (Throwable e) {
                // enlarge buffer error?
                query.setCount(30);
            }
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
            int j = 0;
            while (j < 1) {
                TimeUnit.MINUTES.sleep(1);

                try {
                    result = twitter.search(query);

                } catch (TwitterException e1) {
                    e1.printStackTrace();
                }
                if (result.getTweets().size() >= 1) {
                    tweets = result.getTweets();
                    for (int i = 0; i < tweets.size(); i++) {
                        String name = "@" + result.getTweets().get(i).getUser().getScreenName();
                        if (name.equals(humanPlayer.getName())) {
                           // System.out.println("result: " + result.getTweets().get(i).getText()); testing
                            status = result.getTweets().get(i);
                            break;
                        }
                    }
                    System.out.println("result: " + result.getTweets().get(0).getText());
                    System.out.println("result: " + result.getTweets().size());
                }
                j++;
            }
        } catch (Exception e) {
            if(word.equals("#rsbet")){
                message="call";

            }else{
                message= "discard";
            }
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        message = status.getText();


        return message;
    }

    public GameOfPoker startGame(String[] word, TwitterInterface twitterInterface) throws TwitterException {

        stream = new TwitterStreamFactory(config).getInstance();
        StatusListener listener = new StatusListener() {
            public void onStatus(Status status) {
                String t = "@" + status.getUser().getScreenName();
                twittername = "@" + status.getUser().getScreenName();
                statusId = status.getId();

                if(status.getText().contains(word[0])) {
                    GameOfPoker game = new GameOfPoker(twitterInterface, twittername, statusId);
                    gamelist.add(game);
                    game.start();
                }
                else if(status.getText().contains(word[1])) {
                    for(int i = 0; i < gamelist.size(); i++) {
                        if(gamelist.get(i).tname.equals(t)) {
                            gamelist.get(i).quitMessage();
                            gamelist.get(i).interrupt();
                            gamelist.remove(i);
                        }
                    }
                }

                System.out.println(status.getText());
            }

            public void onDeletionNotice(
                    StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:"
                        + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:"
                        + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId
                        + " upToStatusId:" + upToStatusId);
            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                System.out.println(sw.getMessage());
            }
        };

        FilterQuery filterQuery = new FilterQuery();

        filterQuery.track(word);

        stream.addListener(listener);
        stream.filter(filterQuery);

        try {
            synchronized(lock) {
                lock.wait();

            }
        } catch(InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("returning statuses");
        stream.shutdown();

        return game;
    }



}

