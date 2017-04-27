package poker;

import twitter4j.TwitterException;
import java.io.IOException;

/**
 *
 */
public class Main {
    public static void main(String args[]) throws IOException, TwitterException {
        TwitterInterface twitter_interface = new TwitterInterface();
        String[] keywords = {"#rsdealmein", "#rsdealmeout"};
        twitter_interface.startGame(keywords, twitter_interface);
    }
}
