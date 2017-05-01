package poker;

import twitter4j.User;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Orla on 17/03/2017.
 */
public class HumanPlayer extends PokerPlayer {
    private TwitterInterface twitter;
    private long last_tweet_id;
    private long user_id;
    private String reply_message;
    public boolean reply_found;
    boolean tweet_id_updated = true;
    public final Object reply_sync = new Object();

    HumanPlayer(TwitterInterface twitter_interface, User user, long id) {
        super(null);
        name = user.getScreenName();
        user_id = user.getId();
        last_tweet_id = id;
        twitter = twitter_interface;
        reply_found = false;
    }

    long getUserId(){
        return user_id;
    }

    long getLastTweetId() {
        return last_tweet_id;
    }

    void setLastTweetId(long tweet_id) {
        last_tweet_id = tweet_id;
    }

    void setReplyMessage(String message){
        reply_message = message;
    }

    //gets the status from the user and  decides whether  to fold call or raise
    public void getBet(int highest_bet, int betting_round, int calls_since_raise, int pot, ArrayList<PokerPlayer> live_players) {
        String bet_size, chip_size;
        if(highest_bet == 1){
            bet_size = " chip";
        }
        else {
            bet_size = " chips";
        }
        if(chips == 1){
            chip_size = " chip";
        }
        else {
            chip_size = " chips";
        }
        //twitter.postMessageToUser("Current bet is "+ highest_bet + bet_size +"\nYou have "+ chips + chip_size,this);
        twitter.postMessageToUser("Bet is "+ highest_bet + bet_size +"\nReply to this tweet with fold/call/chips to raise to",this);
        // +"\nReply to this tweet with fold, call, or raise number"
        while(!reply_found){
            synchronized(reply_sync){
                try{
                    reply_sync.wait();
                }
                catch(InterruptedException e){}
            }
        }
        reply_found = false;
        String bet_answer = reply_message;
        //System.out.println("reply: " + reply_message); //testing
        if (bet_answer.contains("fold")){
            current_bet = -1;
        }
        else {
            if(bet_answer.matches(".*\\d+.*")){
                String raise_amount = "";
                boolean number_found = false;
                char[] raise_answer = bet_answer.toCharArray();
                for(int i=0; i < raise_answer.length && number_found == false; i++){
                    if(Character.isDigit(raise_answer[i])){
                        while(i < raise_answer.length) {
                            if(Character.isDigit(raise_answer[i])) {
                                raise_amount += raise_answer[i];
                                i++;
                            }
                            else {
                                break;
                            }
                        }
                        number_found = true;
                    }
                }
                current_bet = Integer.parseInt(raise_amount);
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

//method gets the tweet from the user and  checks  if it contains a digit  then parses to int status contains 0
    //then  discard  the position if doesnt coint 0  discard postion -1
    public boolean[] discard() {
        twitter.postMessageToUser("Reply to this tweet with up to 3 card position numbers (1, 2, 3...) you wish to discard or \"none\" to skip", this );
        boolean[] discard_cards = {false, false, false, false, false};
        while(!reply_found){
            synchronized(reply_sync){
                try{
                    reply_sync.wait();
                }
                catch(InterruptedException e){}
            }
        }
        reply_found = false;
        String status = reply_message;
        char[] status_text = status.toCharArray();
        int discards_entered = 0;

        for(int i = 0; i <status_text.length && discards_entered < 3; i++) {
            String tweet_character = Character.toString(status_text[i]);
            if (tweet_character.matches("\\d{1}")) {
                int discard_index = Integer.parseInt(tweet_character);

                if (status.contains("0")) {
                    if (discard_index >= 0 && discard_index < 5) {
                        discard_cards[discard_index] = true;
                        discards_entered++;
                    }
                } else {
                    if (discard_index > 0 && discard_index <= 5) {
                        discard_index = discard_index - 1;
                        discard_cards[discard_index] = true;
                        discards_entered++;

                    }
                }
            }
        }
        return discard_cards;
    }

    public void outputHand() {
        String discard_string = "";
        if(discards>0){
            discard_string = "now ";
        }
        // hand.toString should be tweeted anyway in case the image can't be loaded or fails
        twitter.postImageToUser(hand.card_hand, "Your hand is "+ discard_string + hand.toString(), this);
    }

    public static void main(String[] args) {
        TwitterInterface t = null;
        try {
            t = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        User u = null;
        HumanPlayer player = new HumanPlayer(t, u, 676);
        boolean[] b = player.discard();
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }
    }
}


