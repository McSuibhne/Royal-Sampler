package poker;

import twitter4j.TwitterException;

import java.io.IOException;

/**
 * Created by Orla on 27/04/2017.
 */
public class Main {
    public static void main(String[] args) throws TwitterException, IOException {
        TwitterInterface twitterInterface = new TwitterInterface();
        String[] keywords = {"#rsdealmein", "#rsdealmeout"};
        twitterInterface.startGame(keywords, twitterInterface);
    }

}
