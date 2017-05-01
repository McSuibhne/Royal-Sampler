package poker;

import twitter4j.User;

import java.util.ArrayList;

/**HumanPlayer contains all data and methods of the twitter user playing the game. It implements all abstract methods
 * defined in PokerPlayer. It also contains some multithreading thread synchronising code to prevent a game from moving
 * ahead until the player has provided their next move when required*/
@SuppressWarnings("StringConcatenationInLoop")
public class HumanPlayer extends PokerPlayer {
    private TwitterInterface twitter;
    private long last_tweet_id;
    private String reply_message;
    boolean reply_found;
    boolean tweet_id_updated = true;
    final Object reply_sync = new Object();

    /**HumanPlayer constructor initialises the variables used during the game for game logic and thread control*/
    HumanPlayer(TwitterInterface twitter_interface, User user, long id) {
        super(null);
        name = user.getScreenName();
        last_tweet_id = id;
        twitter = twitter_interface;
        reply_found = false;
    }

    /**Getter for the ID of the latest tweet in the chain of this user's game*/
    long getLastTweetId() {
        return last_tweet_id;
    }

    /**Setter for the ID of the latest tweet in the chain of this user's game*/
    void setLastTweetId(long tweet_id) {
        last_tweet_id = tweet_id;
    }

    /**Setter for the text of this user's last reply to the twitter bot*/
    void setReplyMessage(String message) {
        reply_message = message;
    }

    /**Implements the abstract method defined in PokerPlayer by asking the player to fold call or raise and returning
     * their decision. The thread will wait here while the user is replying*/
    public void getBet(int highest_bet, int betting_round, int calls_since_raise, int pot, ArrayList<PokerPlayer> live_players) {
        String bet_size;
        if(highest_bet == 1) {
            bet_size = " chip";
        }
        else {
            bet_size = " chips";
        }
        twitter.postMessageToUser("Bet is " + highest_bet + bet_size + "\nReply to this tweet with fold/call/chips to raise to", this);
        while(!reply_found) {
            synchronized(reply_sync) {
                try {
                    reply_sync.wait();
                } catch(InterruptedException e) {
                    System.out.print(e.getMessage());
                }
            }
        }
        reply_found = false;
        String bet_answer = reply_message;
        if(bet_answer.contains("fold")) {
            current_bet = -1;
        }
        else {
            if(bet_answer.matches(".*\\d+.*")) {
                String raise_amount = "";
                boolean number_found = false;
                char[] raise_answer = bet_answer.toCharArray();
                for(int i = 0; i < raise_answer.length && !number_found; i++) {
                    if(Character.isDigit(raise_answer[i])) {
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

            if(current_bet < highest_bet) {
                current_bet = highest_bet;
            }

            if(current_bet >= chips) {
                current_bet = chips;
                all_in = true;
            }
        }
    }

    /**Implements the abstract method defined in PokerPlayer by asking the player to give the indexes of the cards
     * they wish to discard, if any, and returning their decision. The thread will wait here while the user is replying*/
    public boolean[] discard() {
        twitter.postMessageToUser("Reply to this tweet with up to 3 card position numbers (1, 2, 3...) you wish to discard or \"none\" to skip", this);
        boolean[] discard_cards = {false, false, false, false, false};
        while(!reply_found) {
            synchronized(reply_sync) {
                try {
                    reply_sync.wait();
                } catch(InterruptedException e) {
                    System.out.print(e.getMessage());
                }
            }
        }
        reply_found = false;
        String status = reply_message;
        char[] status_text = status.toCharArray();
        int discards_entered = 0;

        for(int i = 0; i < status_text.length && discards_entered < 3; i++) {
            String tweet_character = Character.toString(status_text[i]);
            if(tweet_character.matches("\\d{1}")) {
                int discard_index = Integer.parseInt(tweet_character);

                if(status.contains("0")) {
                    if(discard_index >= 0 && discard_index < 5) {
                        discard_cards[discard_index] = true;
                        discards_entered++;
                    }
                }
                else {
                    if(discard_index > 0 && discard_index <= 5) {
                        discard_index = discard_index - 1;
                        discard_cards[discard_index] = true;
                        discards_entered++;

                    }
                }
            }
        }
        return discard_cards;
    }

    /**Posts the user's hand to them via an image. In case the image fails or the player has very slow data, a unicode
     * representation is also provided*/
    void outputHand() {
        String discard_string = "";
        if(getDiscards() > 0) {
            discard_string = "now ";
        }
        twitter.postImageToUser(getHand().card_hand, "Your hand is " + discard_string + getHand().toString(), this);
    }
}

