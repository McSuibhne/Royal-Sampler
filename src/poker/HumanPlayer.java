package poker;

import twitter4j.Status;
import twitter4j.TwitterException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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




    public void discards() {
        discardTList();
//
//        Scanner scanner = new Scanner(System.in);
//
//
//        boolean[] discard_cards = {false, false, false, false, false};
      /*//  twitter.postreply("Enter 1 to discard and 0 to keep",getTweetId());
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

        // return discard_cards;*/
    }


    public void getBet(int highest_bet) { //TODO: merge with twitter code. See PokerPlayer for description
        twitter.postreply("Please tweet  hastag #rsbet  with raise fold or call",this);

        String string= null;

            string = twitter.getTweet("#rsbet",this);



        if (string.contains("fold")){
            current_bet = -1;
        }if(string.contains("raise")){
            current_bet= highest_bet+1;

        }if(string.contains("call")) {

            current_bet = highest_bet;
        }
    }
    public boolean[] discardTList() {
        twitter.postreply("Enter  the postion numbers you wish to discard  and  #rsdiscard", this );
        twitter.getTweet("#rsdiscard", this);
        boolean[] discard_cards = {false, false, false, false, false};
        String status =twitter.getTweet("#rsdiscard",this);
       char[] chars= status.toCharArray();



        for(int i = 0; i <chars.length ; i++) {
            char c= chars[i];
        int k=  Character.getNumericValue(c);
        System.out.println(k);

           for(int j=0; j<discard_cards.length;j++) {
               if (k == j) {
                   discard_cards[j] = true;
               }
           }
        }
        discard_cards(discard_cards);
        return discard_cards;
    }


    public long getTweetId() {
        return tweetId;
    }

    public static void main(String[] args) {

    }
}


