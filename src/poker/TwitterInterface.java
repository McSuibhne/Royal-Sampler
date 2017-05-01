package poker;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;

/**TwitterInterface is called only once by main but will listen continuously to tweets related to starting, ending, or
 * playing games until it is terminated. It also posts all updates to games and replies/images to users.*/
@SuppressWarnings("ForLoopReplaceableByForEach, WeakerAccess")
class TwitterInterface {
    static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    static final int NAMES_FILE_LENGTH = 4;
    static final String BOT_TWITTER_NAME = "@RoyalSampler1";
    static final long BOT_TWITTER_ID = 843979502158004225L;
    private final Object lock = new Object();
    private final Object post_sync = new Object();
    private Twitter twitter;
    private Configuration config;
    private ArrayList<GameOfPoker> game_list = new ArrayList<>();

    /**TwitterInterface constructor initialises the core twitter objects needed for the application.*/
    public TwitterInterface() throws IOException {
        config = setConfiguration();
        TwitterFactory twitterFactory = new TwitterFactory(config);
        twitter = twitterFactory.getInstance();
    }

    /**SetConfiguration reads in the account data of the twitter bot from and external text file and gains access permissions*/
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
            e.getMessage();
        }
        configuration = configurationBuilder.build();
        return configuration;
    }

    /**StartGame, when called, begins to listen for all tweets posted byt the twitter bot itself
     * (to update the most recent tweet for that game), for all tweets containing #rsdealmein or #rsdealmeout to
     * start and end games, and for any replies to the twitter bot which contain bet or discard instructions.
     * On each instance of one of the above the relevant thread is updated and the listener continues.*/
    public void startGame(String[] word, TwitterInterface twitter_interface) throws TwitterException {
        TwitterStream stream = new TwitterStreamFactory(config).getInstance();
        StatusListener listener = new StatusListener() {
            public void onStatus(Status status) {
                User user = status.getUser();
                String user_name = user.getScreenName();
                if(!status.isRetweet()) {
                    //It's our reply to a user
                    if(user_name.equals(BOT_TWITTER_NAME.substring(1))) {
                        if(status.getText().toLowerCase().contains(word[1])) {
                            for(int i = 0; i < game_list.size(); i++) {
                                if(game_list.get(i).isGameEnded()) {
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
                            if(game_list.get(i).getUser().getName().equals(user.getName())) {
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
                            if(game_list.get(i).getUser().getName().equals(user.getName())) {
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
                            if(game_list.get(i).getUser().getScreenName().equals(user_name)) {
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

    /**Sends reply to user with game information, given by the game, round, or player object that has called it.*/
    synchronized void postMessageToUser(String answer, HumanPlayer human_player) {
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
            catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
        long reply_id = human_player.getLastTweetId();
        status_reply.setInReplyToStatusId(reply_id);

        try {
            human_player.tweet_id_updated = false;
            twitter.updateStatus(status_reply);
        } catch(TwitterException e) {
            System.out.println(e.getMessage());
        }
    }

    /**Sends tweet to relevant user with image of their present hand of cards*/
    synchronized void postImageToUser(PlayingCard[] current_hand, String answer, HumanPlayer human_player) {
        answer = "@" + human_player.getName() + " " + answer;
        StatusUpdate status_reply = new StatusUpdate(answer);
        while(!human_player.tweet_id_updated){
            try {
                synchronized(post_sync) {
                    post_sync.wait();
                }
            }
            catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
        long reply_id = human_player.getLastTweetId();
        status_reply.setInReplyToStatusId(reply_id);

        Picture picture = new Picture(current_hand);
        status_reply.setMedia("Card Hand", new ByteArrayInputStream(picture.createPicture()));
        try {
            human_player.tweet_id_updated = false;
            twitter.updateStatus(status_reply);
        } catch(TwitterException e) {
            System.out.println(e.getMessage());
        }
    }

    /**Sends terminating tweet, ending any game that has come to its natural conclusion*/
    void endGame(HumanPlayer human_player){
        StatusUpdate end_message = new StatusUpdate("#rsdealmeout");
        end_message.setInReplyToStatusId(human_player.getLastTweetId());
        try {
            twitter.updateStatus(end_message);
        }catch(TwitterException e){
            System.out.println(e.getMessage());
        }
    }
}
