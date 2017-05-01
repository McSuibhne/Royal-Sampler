package poker;

import java.util.ArrayList;

/**The PokerPlayer class is a superclass that both HumanPlayer and AIPlayer extend. Where the behavior of the two
 * classes is identical, the implementation is included in this class to reduce code duplication. Where the behaviors
 * differ, the method is left abstract to force the subclasses to provide their own implementation.*/
public abstract class PokerPlayer {
    static final int STARTING_CHIPS = 20;
    private HandOfCards hand;
    String name;
    int chips;
    int current_bet;
    private int previous_bet;
    private int discards;
    boolean bluff = false;
    boolean all_in = false;

    /**Constructor for each poker player, usually invoked via a super() call within a subclass' constructor */
    PokerPlayer(String player_name) {
        name=player_name;
        chips = STARTING_CHIPS;
        current_bet = -1;
        previous_bet = 0;
    }

    /**Getter for player's hand*/
    HandOfCards getHand(){
        return hand;
    }

    /**Getter for player name*/
    String getName() {
        return name;
    }

    /**Getter for player's number of chips*/
    int getChips() {
        return chips;
    }

    /**Setter for player's number of chips*/
    void setChips(int new_chips) {
        chips = new_chips;
    }

    /**Getter for this player's current highest bet in this round*/
    int getCurrentBet(){
        return current_bet;
    }

    /**Getter for the previous bet in the round by this player*/
    int getPreviousBet(){
        return previous_bet;
    }

    /**Setter for the previous bet in the round by this player*/
    void setPreviousBet(int new_previous_bet){
        previous_bet = new_previous_bet;
    }

    /**Getter for the number of discards made by the player*/
    int getDiscards(){
        return discards;
    }

    /**Getter for the AIPlayer's decision to bluff or not*/
    boolean isBluff(){
        return bluff;
    }

    /**Getter for the boolean set when a player goes all in on a bet*/
    boolean isAllIn(){
        return all_in;
    }

    /**Method deal() creates a new hand for itself at the beginning of the betting round, also resetting some variables needed for betting*/
    synchronized void deal(DeckOfCards deck){
        all_in = false;
        discards = 0;
        current_bet = -1;
        previous_bet = 0;
        hand = new HandOfCards(deck);
    }

    /**Abstract method forces both subclasses to implement a betting strategy*/
    public abstract void getBet(int current_bet, int betting_round, int calls_since_raise, int pot, ArrayList<PokerPlayer> live_players);

    /**Abstract method forces both subclasses to implement a discard strategy*/
    public abstract boolean[] discard();

    /**Finds the cards to be discarded by the players and trades them from the hand for replacements*/
    void discard_cards() {
        discards = 0;
        current_bet = -1;
        previous_bet = 0;
        boolean[] discard_cards = discard();
        for (int i = 0; i < HandOfCards.CARDS_IN_HAND; i++) {
            if (discard_cards[i] && discards < 3) {
                hand.discardCard(i);
                discards++;
            }
        }
        hand.sort();
    }
}