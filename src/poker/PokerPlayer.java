package poker;

import java.io.IOException;
import java.util.ArrayList;

public abstract class PokerPlayer {
    public static final int STARTING_CHIPS = 10;
    public HandOfCards hand;
    protected String name;
    public int chips;
    public int current_bet;
    public int previous_bet;
    int discards;
    boolean bluff = false;
    public boolean all_in = false;

    //Hand is initialized.
    public PokerPlayer(String player_name) {
        name=player_name;
        chips = STARTING_CHIPS;
        current_bet = -1;
        previous_bet = 0;
    }

    public String getName() {
        return name;
    }

    public synchronized void deal(DeckOfCards deck){
        all_in = false;
        hand = new HandOfCards(deck);
    }


    public abstract void getBet(int current_bet, int betting_round, int calls_since_raise, int pot, ArrayList<PokerPlayer> live_players);


    public abstract boolean[] discard();


    public void discard_cards() {
        discards = 0;
        boolean[] discard_cards = discard();
        for (int i = 0; i < HandOfCards.CARDS_IN_HAND; i++) {
            if (discard_cards[i] && discards < 3) {
                hand.discardCard(i);
                discards++;
            }
        }
        hand.sort();
    }

    //The main creates a new deck object and passes it into a new player object. The status of the deck is printed before
    // and after calling discard() to show the effects, as well as the number of discards that occur.
    // 10-04-17 Note: Tests disabled while old code is moved into AIPlayer
    public static void main(String[] args) {
        /*
        DeckOfCards deck = new DeckOfCards();
        PokerPlayer player = new PokerPlayer("test_player", deck);
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
        System.out.println("After discard: " + player.cards.toString());

        }
//        for(int j= 0 ;j<DeckOfCards.CARDS_IN_DECK; j++){
//            System.out.println("Deck is  " deck.);
//
//        }
        for(int j= 0 ;j<player.size(); j++){
            System.out.println("Player"+j+"cards is " + player.get(j).cards.toString());

        }
        for(int j= 0 ;j<player.size(); j++){
            System.out.println("Player"+j+"can Open :" + player.get(j).canOpen());
            if (true) {
                boolean[] array = player.get(j).discard();
                player.get(j).discard_cards(array);
                System.out.println("Player" + j + "cards is " + player.get(j).cards.toString());
            }
            else {
                continue;}
        }
//        System.out.println("Before discard: " +player.cards.toString());
//        boolean[] discards = humanPlayer.discard();
//        player.discardCards(discards);
//
//        System.out.println("After discard: " + player.cards.toString());

    }*/
    }

}