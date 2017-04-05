package poker;

import java.util.Scanner;

/**
 * Created by Gavin on 03/04/2017.
 */
public class GameOfPoker {

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    public void start() {

        Scanner scan = new Scanner(System.in);

        System.out.println("Do you want to play Poker? Y/N");
        String s = scan.next();

        if (s.equals("Y") || s.equals("y")) {
            // game start

        } else if (s.equals("N") || s.equals("n")) {
            // thankyou and goodbye

        } else {
            System.out.println("Please enter a valid input");
        }
    }


    // main method initialising twitter listener and waiting for game start request

}
