package poker;

import twitter4j.*;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 */
public class TwitterListener {
    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;

    public String twittername;
    public long statusId;
    //Configuration config = setConfiguration();
    StatusListener listener;
    TwitterStream stream;
    GameOfPoker game;
    ArrayList<GameOfPoker> gamelist = new ArrayList();
    TwitterStreamFactory factory;




    public static void main(String[] args) throws TwitterException, IOException {
        TwitterInterface twitterInterface = new TwitterInterface();
        String[] keywords = {"#rsdealmein", "#rsdealmeout"};
        twitterInterface.startGame(keywords, twitterInterface);

        // twitterInterface.stopGame ("rsdealmeout");
    }


}