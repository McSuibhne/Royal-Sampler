package poker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Gavin on 03/04/2017.
 */
public class GameOfPoker {
    DeckOfCards deck = new DeckOfCards();
    TwitterInterface twitter;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<PokerPlayer> player;
    RoundOfPoker round;

    public GameOfPoker(TwitterInterface twitterInterface) {
        twitter = twitterInterface;
    }

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    public ArrayList<PokerPlayer> create_player() {

        ArrayList<PokerPlayer> player = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String name = null;
            if (i == 0) {

                HumanPlayer players = new HumanPlayer(deck, name, twitter);
                players.setPlayer_name();
                player.add(players);
            } else {
                AIPlayer players = new AIPlayer(deck, null);
                players.setPlayer_name();

                player.add(players);
            }

        }
        return player;
    }

    public void runRounds(boolean flag) {
        if (flag == true) {
            round.createRound();
        } else {
            exit();
        }
    }

    public void start() {

        Scanner scan = new Scanner(System.in);

        System.out.println("Do you want to play Poker? Y/N");
        String s = scan.next();

        if (s.equals("Y") || s.equals("y")) {
            ArrayList<PokerPlayer> player = create_player();

            round = new RoundOfPoker(deck, player);
            round.createRound();

        } else if (s.equals("N") || s.equals("n")) {
            // thankyou and goodbye

        } else {
            System.out.println("Please enter a valid input");
        }
    }

    public boolean checkwanttoplay() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Do you want to Keep playing Poker? Y/N");
        String s = scan.next();

        if (s.equals("Y") || s.equals("y")) return true;
        else
            return false;
    }


    public void exit() {
        System.out.println("Thank You for Playing ");
        System.exit(-1);
    }

    public static void main(String[] args) {
        TwitterInterface twitterInterface = null;
        try {
            twitterInterface = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameOfPoker gameOfPoker = new GameOfPoker(twitterInterface);
        gameOfPoker.start();
        boolean flag;
        do {
            flag = gameOfPoker.checkwanttoplay();
            gameOfPoker.runRounds(flag);
        }
        while (flag != true);
        gameOfPoker.exit();


        // main method initialising twitter listener and waiting for game start request

    }
}
