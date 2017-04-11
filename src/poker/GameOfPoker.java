package poker;

import java.util.Scanner;

/**
 * Created by Gavin on 03/04/2017.
 */
public class GameOfPoker {

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    // Any output/input will be to/from twitter once implemented

    public void start(String twitterHandle, long statusID) {
        long currentGameStatusID = statusID;
        String currentHandle = twitterHandle;

        Scanner scan = new Scanner(System.in);

        System.out.println("Do you want to play Poker? Y/N");
        String s = scan.next();

        if (s.equals("Y") || s.equals("y")) {
            // game start
            // check list for player already in game
            // create human player in list 0
            // create 3 AI players in list 1-3
            // create array of player names?
            // start round of poker

            System.out.println("THIS WILL START A GAME"); // for testing


        } else if (s.equals("N") || s.equals("n")) {

            System.out.println("Thank you and goodbye");
        } else {

            System.out.println("Please enter a valid input");
        }
    }

    public static void main(String[] args) {

        GameOfPoker game = new GameOfPoker();
        game.start("me", 1);

    }


    // main method initialising twitter listener and waiting for game start request

}
