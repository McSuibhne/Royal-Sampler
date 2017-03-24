package poker;

import java.util.Random;

public class PokerPlayer {
    protected HandOfCards hand;
    
    //deafault contructor

    DeckOfCards deckofcards;
    //Hand is initialized.
    public PokerPlayer(DeckOfCards deck) {
       deckofcards=deck;
        hand = new HandOfCards(deckofcards);

    }

    //When discard is called, the method generates a new random integer between 0 and 100, not including 100.
    // It then calls getDiscardProbability on each card index in the hand and compares the returned values to the
    // random required minimum.
    // As this minimum is always <100 and cannot go below 0, a card with a discard probability of 100 will always be
    // traded while a card with a discard probability of 0 can never be traded.
    // The index of each card that should be traded is recorded in a boolean array. This is so all discards occur only
    // after the discard probabilities of all cards have been determined, as errors could occur otherwise from testing a
    // constantly changing hand.
    // The number of discards that occur is recorded and returned.
    // All print statements are for testing and can be later removed.
    public int discard(){
        Random random = new Random();
        int discard_minimum = random.nextInt(100);
        int discards = 0;
        boolean[] discard_cards = {false, false, false, false, false};
        System.out.println("Discard probability minimum: " + discard_minimum);
        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            int discard_probability = hand.getDiscardProbability(i);
            System.out.println("Card " + i + " discard probability: " + discard_probability);

            if(discard_probability > discard_minimum){
                discard_cards[i] = true;
            }
        }
        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            if(discard_cards[i] && discards < 3) {
                hand.discardCard(i);
                discards++;
            }
        }

        hand.sort();
        return discards;
    }

    //The main creates a new deck object and passes it into a new player object. The status of the deck is printed before
    // and after calling discard() to show the effects, as well as the number of discards that occur.
    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();
        PokerPlayer player = new PokerPlayer(deck);

        System.out.println("Before discard: " + player.hand.toString());
        int discards = player.discard();
        System.out.println("Discards: " + discards);
        System.out.println("After discard: " + player.hand.toString());

    }
}
