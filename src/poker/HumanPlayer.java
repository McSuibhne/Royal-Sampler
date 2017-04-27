package poker;

import twitter4j.Status;
import twitter4j.TwitterException;

import java.awt.image.BufferedImage;
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
    public HumanPlayer(TwitterInterface twitter_interface, String twitter_name, long id) {
        super(null);
        name = twitter_name;
        tweetId = id;
        twitter = twitter_interface;
    }

    public void setTweetId(long tweet_id) {
        tweetId = tweet_id;
    }


    public long getTweetId() {
        return tweetId;
    }


    public boolean[] discard() {
        twitter.postReply("Enter up to 3 card position numbers (1, 2, 3...) you wish to discard and #rsdiscard", this );
        twitter.getTweet("#rsdiscard", this);
        boolean[] discard_cards = {false, false, false, false, false};
        String status = twitter.getTweet("#rsdiscard", this);
        char[] status_text = status.toCharArray();
        int discards_entered = 0;
        boolean uses_zero = false;

        for(int i = 0; i <status_text.length && discards_entered <= 3; i++) {
            String tweet_character = Character.toString(status_text[i]);
            if(tweet_character.matches("\\d{1}")) {
                int discard_index = Integer.parseInt(tweet_character);
                if(discard_index >= 0 && discard_index <= 5) {
                    if(discard_index == 0){
                        uses_zero = true;
                    }
                    if(!uses_zero){
                        discard_index--;
                    }
                    discard_cards[discard_index] = true;
                    discards_entered++;
                }
            }
        }

        //Console testing below if wanted, remove before submission
        /*Scanner scanner = new Scanner(System.in);
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
        */

         return discard_cards;
    }


    public void getBet(int highest_bet, int betting_round, int calls_since_raise, ArrayList<PokerPlayer> live_players) {
        twitter.postReply("Please tweet #rsbet with fold, call, or amount to raise the bet to",this);
        //Scanner scanner = new Scanner(System.in);
        //String bet_answer = scanner.nextLine();

        String bet_answer = twitter.getTweet("#rsbet",this);

        if (bet_answer.contains("fold")){
            current_bet = -1;
        }
        else {
            if(bet_answer.matches(".*\\d+.*")){
                String raise_string = "";
                for(char character : bet_answer.toCharArray()){
                    if(Character.isDigit(character)){
                        raise_string += character;
                    }
                    else if(!raise_string.equals("")){
                        break;
                    }
                }
                current_bet = Integer.parseInt(raise_string);
            }
            else {
                current_bet = highest_bet;
            }

            if(current_bet < highest_bet){
                current_bet = highest_bet;
            }

            if(current_bet >= chips){
                current_bet = chips;
                all_in = true;
            }
        }
    }

    public void outputHand() {
        //TODO: Twitter code/Graphics image to tell the player what their hand is. Called after each deal/trade in RoundOfPoker.
        createImage();
        twitter.postImage(hand.card_hand, "Your hand is " + hand.toString(), this);
        // twitter.postReply("Your Hand is " + hand.toString(), this); // can be removed once picture addition

    }

    public BufferedImage createImage() {
        Picture picture = new Picture(hand.card_hand);
        BufferedImage image = picture.createPicture();
        return image;
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
//        twit.postReply("This is your Hand"+player.hand.toString(),0);
//        boolean[] discardlist = player.discardTList();
//        player.discard_cards(discardlist);
//        //System.out.println("Discards: " + );
//        twit.postReply("Hand after discard" +player.hand.toString(),0);
//        System.out.println("After discard: " + player.hand.toString());


    }

}


