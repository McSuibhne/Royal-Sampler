package poker;

import java.util.Random;

public abstract class PokerPlayer {
    public static final int STARTING_CHIPS = 10;
    private DeckOfCards deck;
    public HandOfCards hand;
    public String name;
    public int chips;

    //Hand is initialized.
    public PokerPlayer(String player_name, DeckOfCards card_deck) {
        name = player_name;
        deck = card_deck;
        chips = STARTING_CHIPS;
    }

    public void deal(){
        hand = new HandOfCards(deck);
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


    //TODO: Implement getBet in both AIPlayer (with AI) and HumanPlayer (ask Twitter). getBet should return -1 to indicate a fold but
    //TODO:  should not otherwise be allowed to return an integer less than the given current bet. Note: This >=current_bet check
    //TODO:   would possibly be better placed in RoundOfCards (more coherent) but currently I think it needs to be in getBet.
    //TODO:    In summary, only return options are (a) [-1]...fold   (b) [current_bet]...call/see    (c) [>current_bet]...raise
    public abstract int getBet(int current_bet, int chips_to_call);


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

        System.out.println("After discard: " + player.hand.toString());

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