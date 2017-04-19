package poker;

import twitter4j.TwitterException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Orla on 17/03/2017.
 */
public class HumanPlayer extends PokerPlayer {
    TwitterInterface twitter;
    public long tweetId;
    public HumanPlayer(DeckOfCards deck ,TwitterInterface twitterInterface,String twittername, long id) {
        super(null, deck);
        name=twittername;
        tweetId= id;
        twitter = twitterInterface;
        chips=11;       //testing
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public boolean getRaise(String word) {
        if (word.equalsIgnoreCase("Raise")) {
            return true;
        } else {
            return false;
        }
    }

//    public String getHumanName() {
////        System.out.println("Please enter your name");
////        Scanner input = new Scanner(System.in);
//        String name =input.nextLine ();
//String name = twitter.getTwittername ();
//    return name;}



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
      //  twitter.postreply("Enter 1 to discard and 0 to keep",getTweetId());
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


        String status = twit.getUserTweet("OrlaCullen15", null);

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

    public long getTweetId() {
        return tweetId;
    }

    public void outputHand(){
        //TODO: Small amount of twitter code to tell the player what their hand is. Called after each deal/trade in RoundOfPoker.
        String answer=getName()+" your hand :"+ hand.toString();
        long id = getTweetId();
        twitter.postreply (answer,this);
       // System.out.println(getName()+" your hand :"+ hand.toString()); //TEMPORARY!
    }


    public static void main(String[] args) {
//        DeckOfCards deck = new DeckOfCards();
//
//        TwitterInterface twit = null;
//        try {
//            twit = new TwitterInterface();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        HumanPlayer player = new HumanPlayer(deck,twit);
//        twit.postreply("This is your Hand"+player.hand.toString(),0);
//        boolean[] discardlist = player.discardTList();
//        player.discard_cards(discardlist);
//        //System.out.println("Discards: " + );
//        twit.postreply("Hand after discard" +player.hand.toString(),0);
//        System.out.println("After discard: " + player.hand.toString());
    }
}


