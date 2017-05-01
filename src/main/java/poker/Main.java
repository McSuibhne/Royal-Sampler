package poker;

import twitter4j.TwitterException;

import java.io.IOException;

/**
 * Main runnable class, should be the starting point of the application.*/
public class Main {
    public static void main(String[] args) throws TwitterException, IOException {
        TwitterInterface twitterInterface = new TwitterInterface();
        final String[] keywords = {"#rsdealmein", "#rsdealmeout", TwitterInterface.BOT_TWITTER_NAME};
        twitterInterface.startGame(keywords, twitterInterface);
    }
}
