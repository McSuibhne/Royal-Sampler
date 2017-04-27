package poker;

import twitter4j.*;
import twitter4j.media.ImageUpload;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.image.BufferedImage;
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

    TwitterFactory tf;
    Twitter twitter;
    List<Status> tweets;
    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;

    public String twittername;
    public long statusId;

    Configuration config = setConfiguration();

    TwitterStream stream;
    GameOfPoker game;
    ArrayList<GameOfPoker> gamelist = new ArrayList();
    TwitterStreamFactory factory;

    public TwitterInterface() throws IOException {
        tf = new TwitterFactory(config);
        twitter = tf.getInstance();
    }
    public Configuration setConfiguration() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        String ai_name = null;
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

        config = configurationBuilder.build();

        return config;
    }


    public void postReply(String answer, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;

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

    public void postImage(PlayingCard[] currentHand, String answer, PokerPlayer pokerPlayer) {

        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;
        File file = new File("src/twitter_output/hand_picture.png");
        long replyId = humanPlayer.getTweetId();
        ImageUpload image;

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


    //Should be removed before submission. Rename to postReply to easily switch to console output instead of twitter.
    public void testpostreply(String answer, PokerPlayer pokerPlayer) {
        System.out.println(answer);
    }


    public String getTweet(String word, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;

        Status s = null;


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
                            System.out.println("result: " + result.getTweets().get(i).getText());
                            s = result.getTweets().get(i);
                            break;
                        }
                    }
                    System.out.println("result: " + result.getTweets().get(0).getText());
                    System.out.println("result: " + result.getTweets().size());
                }
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        String h;

        // humanPlayer.setTweetId(s.getId());
        h = s.getText();


        return h;
    }
    private final Object lock = new Object();


    public GameOfPoker startGame(String[] word, TwitterInterface twitterInterface) throws TwitterException {

        stream = new TwitterStreamFactory(config).getInstance();
        StatusListener listener = new StatusListener() {
            public void onStatus(Status status) {
                String t = "@" + status.getUser().getScreenName();
                long id = status.getId();
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

        FilterQuery fq = new FilterQuery();

        fq.track(word);

        stream.addListener(listener);
        stream.filter(fq);

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

    public boolean stopGame(String word) throws TwitterException {

        stream = new TwitterStreamFactory(config).getInstance();
        final boolean[] flag = {false};

        StatusListener listener = new StatusListener() {


            @Override
            public void onException(Exception ex) {

            }

            public void onStatus(Status status) {

                String t = "@" + status.getUser().getScreenName();
                long id = status.getId();
                for(int i = 0; i < gamelist.size(); i++) {
                    synchronized(gamelist) {
                        if(gamelist.get(i).tname.equals(t)) {
                            gamelist.remove(i);
                        }
                    }
                }

                stream.shutdown();
                //  game.notify ( );
                System.out.println("unlocked");

            }


            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }
        };
        stream.addListener(listener);
        stream.filter(word);

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
        FilterQuery fq = new FilterQuery();
        fq.track(word);
        return flag[0];
    }
    public static void main(String[] args) throws TwitterException, IOException {
        TwitterInterface twitterInterface = new TwitterInterface();
        String[] keywords = {"#rsdealmeinx", "#rsdealmeout"};
        twitterInterface.startGame(keywords, twitterInterface);


    }


}

