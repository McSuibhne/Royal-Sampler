package poker;

import java.io.IOException;
import java.util.ArrayList;

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

//method gets the tweet from the user and  checks  if it contains a digit  then parses to int status contains 0
    //then  discard  the position if doesnt coint 0  discard postion -1
    public boolean[] discard() {
     twitter.postMessagetoUser(" Enter up to 3 card position numbers (1, 2, 3...) you wish to discard and #rsdiscard", this );
        boolean[] discard_cards = {false, false, false, false, false};
        String status = twitter.getTweetfromUser("#rsdiscard", this);
        char[] status_text = status.toCharArray();
        int discards_entered = 0;


        for(int i = 0; i <status_text.length ; i++) {
            String tweetcharacter = Character.toString(status_text[i]);
            if (tweetcharacter.matches("\\d{1}")) {
                int discard_index = Integer.parseInt(tweetcharacter);


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

//gets the status from the user and  decides whether  to fold call or raise
    public void getBet(int highest_bet, int betting_round, int calls_since_raise, ArrayList<PokerPlayer> live_players) {
        twitter.postMessagetoUser(getName()+ "Please tweet #rsbet with fold, call, or amount to raise the bet to", this);
        //Scanner scanner = new Scanner(System.in);
        //String bet_answer = scanner.nextLine();

        String bet_answer = twitter.getTweetfromUser("#rsbet",this);

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
//posts userHand to twitter
    public void outputHand(String tweet_message) {
        twitter.postImagetoUser(hand.card_hand,getName()+"  "+ tweet_message+" "+ hand.toString(), this);

    }
    public static void main(String[] args) {
        TwitterInterface t = null;
        try {
            t = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HumanPlayer player = new HumanPlayer(t, "O", 676);
        boolean[] b = player.discard();
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }
    }
}


