package poker;

import java.util.Random;

public class PokerPlayer {
    public HandOfCards hand;
    private String player_name;
    static public final int NO_OF_PLAYERS = 5;

    //Hand is initialized.
    public PokerPlayer(DeckOfCards deck, String name) {
        hand = new HandOfCards(deck);
        player_name = name;

    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
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
    public boolean[] discard() {
        Random random = new Random();
        int discard_minimum = random.nextInt(100);
        int discards = 0;
        boolean[] discard_cards = {false, false, false, false, false};
        System.out.println("Discard probability minimum: " + discard_minimum);
        for (int i = 0; i < HandOfCards.CARDS_IN_HAND; i++) {
            int discard_probability = hand.getDiscardProbability(i);
            System.out.println("Card " + i + " discard probability: " + discard_probability);

            if (discard_probability > discard_minimum) {
                discard_cards[i] = true;
            }
        }

        return discard_cards;
    }

    public void discard_cards(boolean[] discard_cards) {
        int discards = 0;
        for (int i = 0; i < HandOfCards.CARDS_IN_HAND; i++) {
            if (discard_cards[i] && discards < 3) {
                hand.discardCard(i);
                discards++;
            }
        }
        System.out.println("Number of cards discarded: " + discards);

        hand.sort();
    }

    public boolean canOpen() {
        if (hand.isOnePair()) {
            return true;
        } else

            return false;
    }


    //The main creates a new deck object and passes it into a new player object. The status of the deck is printed before
    // and after calling discard() to show the effects, as well as the number of discards that occur.
    public static void main(String[] args) {
       /* DeckOfCards deck = new DeckOfCards();
        ArrayList<PokerPlayer> player = new ArrayList<>();

        for (int i=0 ;i<5; i ++ ){
            if (i==0){
                HumanPlayer players = new HumanPlayer(deck);
            player.add(players);
            }
                else{
            AIPlayer players = new AIPlayer(deck);
                player.add(players);
           }

        }
//        for(int j= 0 ;j<DeckOfCards.CARDS_IN_DECK; j++){
//            System.out.println("Deck is  " deck.);
//
//        }
        for(int j= 0 ;j<player.size(); j++){
            System.out.println("Player"+j+"hand is " + player.get(j).hand.toString());

        }
        for(int j= 0 ;j<player.size(); j++){
            System.out.println("Player"+j+"can Open :" + player.get(j).canOpen());
            if (true) {
                boolean[] array = player.get(j).discard();
                player.get(j).discard_cards(array);
                System.out.println("Player" + j + "hand is " + player.get(j).hand.toString());
            }
            else {
                continue;}
        }



//        System.out.println("Before discard: " +player.hand.toString());
//        boolean[] discards = humanPlayer.discard();
//        player.discardCards(discards);
//
//        System.out.println("After discard: " + player.hand.toString());

    }*/
    }
}