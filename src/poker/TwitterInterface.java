package poker;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Orla on 20/03/2017.
 */
public class TwitterInterface {
    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;
    public static final String BOT_TWITTER_NAME = "@RoyalSampler1";
    long BOT_TWITTER_ID = 843979502158004225L;
    private final Object lock = new Object();
    public final Object post_sync = new Object();
    TwitterFactory twitterFactory;
    Twitter twitter;
    Configuration config;
    TwitterStream stream;
    ArrayList<GameOfPoker> game_list = new ArrayList();

    public TwitterInterface() throws IOException {
        config = setConfiguration();
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

    public void startGame(String[] word, TwitterInterface twitter_interface) throws TwitterException {
        stream = new TwitterStreamFactory(config).getInstance();
        StatusListener listener = new StatusListener() {
            public void onStatus(Status status) {
                User user = status.getUser();
                String user_name = user.getScreenName();
                if(!status.isRetweet()) {
                    //It's our reply to a user
                    if(user_name.equals(BOT_TWITTER_NAME.substring(1))) {
                        if(status.getText().toLowerCase().contains(word[1])) {
                            for(int i = 0; i < game_list.size(); i++) {
                                if(game_list.get(i).game_ended) {
                                    game_list.get(i).interrupt();
                                    game_list.remove(i);
                                }
                            }
                        }
                        else {
                            for(int i = 0; i < game_list.size(); i++) {
                                if(status.getInReplyToStatusId() == game_list.get(i).getHumanPlayer().getLastTweetId()) {
                                    game_list.get(i).getHumanPlayer().setLastTweetId(status.getId());
                                    game_list.get(i).getHumanPlayer().tweet_id_updated = true;
                                    synchronized(post_sync) {
                                        post_sync.notify();
                                    }
                                }
                            }
                        }
                    }
                    //It's someone tweeting #rsdealmein
                    else if(status.getText().toLowerCase().contains(word[0])) {
                        int game_index = -1;
                        for(int i = 0; i < game_list.size(); i++) {
                            if(game_list.get(i).user.getName().equals(user.getName())) {
                                game_index = i;
                                break;
                            }
                        }
                        if(game_index != -1) {
                            game_list.get(game_index).interruptMessage();
                            game_list.get(game_index).interrupt();
                            game_list.remove(game_index);
                        }
                        GameOfPoker game = new GameOfPoker(twitter_interface, user, status.getId());
                        game_list.add(game);
                        game.start();
                    }
                    //It's someone tweeting #rsdealmeout
                    else if(status.getText().toLowerCase().contains(word[1])) {
                        for(int i = 0; i < game_list.size(); i++) {
                            if(game_list.get(i).user.getName().equals(user.getName())) {
                                game_list.get(i).quitMessage();
                                game_list.get(i).interrupt();
                                game_list.remove(i);
                                break;
                            }
                        }
                    }
                    //It's someone tweeting a reply to bet or discard cards
                    else if(status.getText().contains(word[2])) {
                        for(int i = 0; i < game_list.size(); i++) {
                            if(game_list.get(i).user.getScreenName().equals(user_name)) {
                                HumanPlayer reply_player = game_list.get(i).getHumanPlayer();
                                if(status.getInReplyToStatusId() == reply_player.getLastTweetId()) {
                                    reply_player.setReplyMessage(status.getText().substring(BOT_TWITTER_NAME.length()));
                                    reply_player.setLastTweetId(status.getId());
                                    reply_player.tweet_id_updated = true;
                                    reply_player.reply_found = true;
                                    synchronized(reply_player.reply_sync) {
                                        reply_player.reply_sync.notify();
                                    }
                                }
                            }
                        }
                    }
                    //System.out.println(status.getText());
                }
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
        filterQuery.follow(BOT_TWITTER_ID).track(word);

        stream.addListener(listener);
        stream.filter(filterQuery);

        try {
            synchronized(lock) {
                lock.wait();
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        stream.shutdown();
    }

    public synchronized void postMessageToUser(String answer, HumanPlayer human_player) {
        answer = "@" + human_player.getName() + " " + answer;
        if(answer.length() > 139) {
            answer = answer.substring(0, 139);
        }
        StatusUpdate status_reply = new StatusUpdate(answer);
        while(!human_player.tweet_id_updated){
            try {
                synchronized(post_sync) {
                    post_sync.wait();
                }
            }
            catch(InterruptedException e){}
        }
        long reply_id = human_player.getLastTweetId();
        status_reply.setInReplyToStatusId(reply_id);

        try {
            human_player.tweet_id_updated = false;
            //System.out.println(answer);
            twitter.updateStatus(status_reply);
        } catch(TwitterException ex) {
            //System.out.println("post failed"+ ex.toString());
        }
    }

    public synchronized void postImageToUser(PlayingCard[] current_hand, String answer, HumanPlayer human_player) {
        answer = "@" + human_player.getName() + " " + answer;
        StatusUpdate status_reply = new StatusUpdate(answer);
        while(!human_player.tweet_id_updated){
            try {
                synchronized(post_sync) {
                    post_sync.wait();
                }
            }
            catch(InterruptedException e){}
        }
        long reply_id = human_player.getLastTweetId();
        status_reply.setInReplyToStatusId(reply_id);

        Picture picture = new Picture(current_hand);
        status_reply.setMedia("Card Hand", new ByteArrayInputStream(picture.createPicture()));
        try {
            human_player.tweet_id_updated = false;
            //System.out.println(answer);
            twitter.updateStatus(status_reply);
        } catch(TwitterException ex) {
            //System.out.println("post failed"+ ex.toString());
        }
    }

    public void endGame(HumanPlayer human_player){
        StatusUpdate end_message = new StatusUpdate("#rsdealmeout");
        end_message.setInReplyToStatusId(human_player.getLastTweetId());
        try {
            twitter.updateStatus(end_message);
        }catch(TwitterException e){
            //System.out.println("end post failed"+ e.toString());
        }
    }
}
