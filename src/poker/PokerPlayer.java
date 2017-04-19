package poker;

import java.io.IOException;

public abstract class PokerPlayer {
    public static final int STARTING_CHIPS = 10;
    //private DeckOfCards deck;
    public HandOfCards hand;
    protected String name;
    public int chips;
    public int current_bet;

    //Hand is initialized.
    public PokerPlayer(String player_name, DeckOfCards card_deck) {
        name=player_name;
        //deck = card_deck;
        chips = STARTING_CHIPS;
        current_bet = 0;
    }

    public String getName() {
        return name;
    }

    public synchronized void deal(DeckOfCards deck){

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
        TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // twit.postreply(getName() + "Number of cards discarded: " + discards, h.getTweetId());
        System.out.println("Number of cards discarded: " + discards);

        hand.sort();
    }

    private String setPlayer_name(String name) {
        return this.name;
    }
    //TODO: Implement getBet in both AIPlayer (with AI) and HumanPlayer (ask Twitter). getBet should change current_bet
    //TODO:  -1 to indicate a fold and otherwise place their bet amount (call or raise).
    //TODO:    In summary, only current_bet options are (a)[-1]...fold  (b)[highest_bet]...call/see  (c)[>highest_bet]...raise
    public abstract void getBet(int current_bet);


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

    public abstract void discards();
}