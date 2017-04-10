package poker;

/**
 * Created by Jonathan on 10/04/2017.
 */
public class AIPlayer extends PokerPlayer {
    int discard_minimum;

    public AIPlayer(String player_name, int discard_min, DeckOfCards deck){
        super(player_name, deck);
        discard_minimum = discard_min;
    }

    public int getBet(int current_bet, int chips_to_call){
        int bet = -1;


        return bet;
    }


    // Method calls getDiscardProbability on each card index in the hand and compares the returned values to the
    // given discard minimum.
    // As this minimum is always <100 and cannot go below 0, a card with a discard probability of 100 will always be
    // traded while a card with a discard probability of 0 can never be traded.
    // The index of each card that should be traded is recorded in a boolean array and returned.
    // All print statements are for testing and can be later removed.
    public boolean[] discard(){
        int discards = 0;
        boolean[] discard_cards = {false, false, false, false, false};
        System.out.println("Discard probability minimum: " + discard_minimum); //Testing
        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            int discard_probability = hand.getDiscardProbability(i);
            System.out.println("Card " + i + " discard probability: " + discard_probability); //Testing

            if(discard_probability > discard_minimum){
                discard_cards[i] = true;
            }
        }
        return discard_cards;
    }
}
