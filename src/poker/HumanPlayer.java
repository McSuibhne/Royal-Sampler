package poker;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Orla on 17/03/2017.
*/
public class HumanPlayer extends PokerPlayer {
TwitterInterface twitter;
    public HumanPlayer(DeckOfCards deck, TwitterInterface twitterInterface, String tname, long tid) {
        super(null, deck);
       name=getHumanName();
        twitter = twitterInterface;
        chips=11;       //testing
    }

    public boolean getRaise(String word) {
        if (word.equalsIgnoreCase("Raise")) {
            return true;
        } else {
            return false;
        }
    }

    public String getHumanName() {
        System.out.println("Please enter your name");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();

    return name;}



    public boolean getFold(String word) {

        if (word.equalsIgnoreCase("Fold")) {
            return true;
        } else {
            return false;
        }
    }

    public void discards() {

        Scanner scanner = new Scanner(System.in);


        boolean[] discard_cards = {false, false, false, false, false};
        System.out.println("Enter 1 to discard and 0 to keep");
        for(int i = 0; i < HandOfCards.CARDS_IN_HAND; i++) {
            int j = scanner.nextInt();
            if(j == 1) {
                discard_cards[i] = true;
            }
            else
                discard_cards[i] = false;

        }
        discard_cards(discard_cards);

        // return discard_cards;
    }


    public void getBet(int highest_bet) { //TODO: merge with twitter code. See PokerPlayer for description
        System.out.print("Please enter raise or fold or call ");
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        if (getFold(word)){
            current_bet = -1;
        }else if (getRaise(word)){
            current_bet= highest_bet+1;

        }
        else
            current_bet=highest_bet;
    }
    public boolean[] discardTList() {

        boolean[] discard_cards = {false, false, false, false, false};
        TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String status = twit.getUserTweet("OrlaCullen15");

        //Process char String str = "true false true false false";

        String[] parts = status.split(" ");

        boolean[] array = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = Boolean.parseBoolean(parts[i]);
        }
        // System.out.println(Arrays.toString(array));


        for (int i = 0; i < HandOfCards.CARDS_IN_HAND; i++) {

            if (array[i] == true) {
                discard_cards[i] = true;
            } else
                discard_cards[i] = false;

        }
        return discard_cards;
    }

    public void outputHand(){
        //TODO: Small amount of twitter code to tell the player what their hand is. Called after each deal/trade in RoundOfPoker.
        System.out.println(getName()+" your hand :"+ hand.toString()); //TEMPORARY!
    }


    public static void main(String[] args) {
      /*  DeckOfCards deck = new DeckOfCards();
        HumanPlayer player = new HumanPlayer(deck);
        TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
         String s =player.hand.toString();
          System.out.println("Before discard: " + player.hand.toString());
//twit.getUserTweet("OrlaCullen15");

        twit.postreply("This is your Hand"+player.hand.toString(),0);
        boolean[] discardlist = player.discardTList();
        player.discard_cards(discardlist);
        //System.out.println("Discards: " + );
        twit.postreply("Hand after discard" +player.hand.toString(),0);
        System.out.println("After discard: " + player.hand.toString());
    }*/
    }
}

