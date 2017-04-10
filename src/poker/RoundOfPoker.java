package poker;

import java.util.ArrayList;

/**
 * Created by Andy on 30/03/2017.
 */

public class RoundOfPoker {

    private int pot;
    ArrayList<PokerPlayer> players;
    DeckOfCards deckOfCards;

    // Empty constructor (not sure what to include here)
    public RoundOfPoker(DeckOfCards deck, ArrayList<PokerPlayer> player) {
        players = player;
        deckOfCards = deck;
    }

    public void createRound() {
        dealEachPlayerHand();
        int canOpen = checkifSomeoneCanOpen();
        if (canOpen == 0) {
            deckOfCards.reset();
            createRound();
        } else {
            checkforDiscard();
        }
        //doBetting();
        checkforWinner();


    }

    public void dealEachPlayerHand() {
        for (int j = 0; j < players.size(); j++) {
//players.get(j).getPlayer_name();
            System.out.println(players.get(j).getPlayer_name() + " your hand is " + players.get(j).hand.toString());

        }
    }

    public int checkifSomeoneCanOpen() {
        int j = 0;
        int canOpen = 0;
        for (j = 0; j < players.size(); j++) {
            if (players.get(j).canOpen() == true) {
                System.out.println(players.get(j).getPlayer_name() + " Can Open :" + players.get(j).canOpen());
                canOpen = canOpen + 1;
            } else {
                System.out.println(players.get(j).getPlayer_name() + " Can Open :" + players.get(j).canOpen());
            }

        }

        return canOpen;
    }

    public void doBettingcycle() {

    }

    public void checkforWinner() {

        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).getPlayer_name() + " has a hand value of " + players.get(i).hand.getGameValue());

        }
        compare();
    }

    public void checkforDiscard() {
        for (int j = 0; j < players.size(); j++) {
            // System.out.println("Player"+j+"can Open :" + players.get(j).canOpen());
            if (j == 0) {
                System.out.println("Please enter 1 to discard 0 to keep for each postion ");
                boolean[] array = players.get(j).discard();
                players.get(j).discard_cards(array);
                System.out.println(players.get(j).getPlayer_name() + "hand is " + players.get(j).hand.toString() + "After discarding");
            } else {
                boolean[] array = players.get(j).discard();
                players.get(j).discard_cards(array);
                System.out.println(players.get(j).getPlayer_name() + "hand is " + players.get(j).hand.toString() + "After discarding");
            }

        }

    }

    public void compare() {
        if ((players.get(0).hand.getGameValue() >= players.get(1).hand.getGameValue())
                && (players.get(0).hand.getGameValue() >= players.get(2).hand.getGameValue())
                && (players.get(0).hand.getGameValue() >= players.get(3).hand.getGameValue())
                && (players.get(0).hand.getGameValue() >= players.get(4).hand.getGameValue())) { // a >= b,c,d,e
            System.out.println(players.get(0).getPlayer_name() + " is the Winner of this Round: " + players.get(0).hand.getGameValue());
        } else if ((players.get(1).hand.getGameValue() >= (players.get(2).hand.getGameValue()))
                && (players.get(1).hand.getGameValue() >= (players.get(3).hand.getGameValue()))
                && ((players.get(1).hand.getGameValue() >= (players.get(4).hand.getGameValue())))) {      // b >= c,d,e
            System.out.println(players.get(1).getPlayer_name() + " is the Winner of this Round: " + players.get(1).hand.getGameValue());
        } else if ((players.get(2).hand.getGameValue() >= players.get(3).hand.getGameValue())
                && (players.get(2).hand.getGameValue() >= players.get(4).hand.getGameValue())) {                  // c >= d,e
            System.out.println(players.get(2).getPlayer_name() + " is the Winner of this Round: " + (players.get(2).hand.getGameValue()));
        } else if (players.get(3).hand.getGameValue() >= players.get(3).hand.getGameValue()) {                                // d >= e
            System.out.println(players.get(3).getPlayer_name() + " is the Winner of this Round: " + (players.get(3).hand.getGameValue()));
        } else {                                            // e > d
            System.out.println(players.get(4).getPlayer_name() + " is the Winner of this Round: " + (players.get(4).hand.getGameValue()));
        }
    }

    public int getPot() {
        return pot;
    }

    // Main function simulates a round of poker
    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();

        for (int shuffleCount = 0; shuffleCount < 2074; shuffleCount++) {
            deck.shuffle();
        }

        HandOfCards hand1 = new HandOfCards(deck);
        HandOfCards hand2 = new HandOfCards(deck);

        System.out.println("Hand 1: " + hand1.toString());
        System.out.println("Hand 2: " + hand2.toString());
    }

}
