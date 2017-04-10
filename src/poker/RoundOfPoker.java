package poker;

import java.io.*;
import java.util.*;

/**
 * Created by Andy on 30/03/2017.
 */

public class RoundOfPoker {
    public static final int ANTE = 1;
    private int pot;

    public RoundOfPoker(ArrayList<PokerPlayer> player_list, DeckOfCards deck) {
        pot = 0;
        ArrayList<PokerPlayer> live_players = new ArrayList<>();
        live_players = (ArrayList<PokerPlayer>) player_list.clone();
        playRound(live_players, deck);   //As in GameOfPoker, it's probably better to call playRound from within the higher
        // higher class (game) instead of in the constructor.
    }

    public void playRound(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        int opener_index = -1;
        boolean player_busted = false;
        while(opener_index == -1) {
            System.out.println("New Deal:");
            countChips(live_players);
            player_busted = payAnte(live_players);
            if(player_busted) {
                break;
            }
            dealCards(live_players, deck);
            opener_index = findOpener(live_players);
            if(opener_index == -1) {
                deck.reset();
            }
        }
        if(!player_busted && live_players.size() < 1) {
            bet(opener_index, live_players, deck);
        }

    }

    public void countChips(ArrayList<PokerPlayer> live_players) {
        for(int i = 0; i < live_players.size(); i++) {
            String chip_string = "";
            if(live_players.get(i).chips == 1) {
                chip_string = " chip";
            }
            else {
                chip_string = " chips";
            }
            System.out.println(live_players.get(i).name + " has " + live_players.get(i).chips + chip_string);
        }
    }

    public boolean payAnte(ArrayList<PokerPlayer> live_players) {
        boolean isHumanBusted = false;
        for(int i = 0; i < live_players.size(); i++) {
            if(live_players.get(i).chips >= ANTE) {
                live_players.get(i).chips -= ANTE;
                pot += ANTE;
            }
            else {
                System.out.println(live_players.get(i).name + "Has run out of chips and is eliminated from the round");
                if(i == GameOfPoker.HUMAN_INDEX) {
                    isHumanBusted = true;
                }
                live_players.remove(i);
            }
        }
        return isHumanBusted;
    }

    public void dealCards(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).deal();
            try {
                HumanPlayer temp_human = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
                temp_human.outputHand();
            } catch(Exception e) {    //Should never be reached, live_players.get(0) is either always the HumanPlayer or the game is over
            }
        }
    }

    public int findOpener(ArrayList<PokerPlayer> live_players) {
        ArrayList<Integer> opener_indexes = new ArrayList<>();
        int opener = -1;
        for(int i = 0; i < live_players.size(); i++) {
            if(live_players.get(i).hand.getGameValue() > HandOfCards.ONE_PAIR_VALUE) {
                opener_indexes.add(i);
            }
        }
        if(opener_indexes.size() > 0) {
            for(int i = 0; i < opener_indexes.size(); i++) {
                if(i != 0) {
                    System.out.print(", ");
                }
                if(i == opener_indexes.size() - 1) {
                    System.out.print("and ");
                }
                System.out.print(live_players.get(opener_indexes.get(i)).name);
            }
            if(opener_indexes.size() > 0) {
                Random rand = new Random();
                opener = opener_indexes.get(rand.nextInt(opener_indexes.size()));
                System.out.print(" can open the betting");
            }
            else {
                System.out.print("No one can open the betting, restarting round");
            }
        }
        return opener;
    }

    public void bet(int opener_index, ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        ArrayList<Integer> bets = new ArrayList<>();
        int current_bet = 0;
        int player_response;
        int i = opener_index;
        System.out.print(live_players.get(i).name + " will open");
        do {
            player_response = live_players.get(i % live_players.size()).getBet(current_bet, current_bet - bets.get(i));
            if(player_response == -1) {
                live_players.remove(i % live_players.size());
                continue;
            }


        } while(i % live_players.size() != opener_index);
    }

    // Main function simulates a round of poker
    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();

        for(int shuffleCount = 0; shuffleCount < 2074; shuffleCount++) {
            deck.shuffle();
        }

        HandOfCards hand1 = new HandOfCards(deck);
        HandOfCards hand2 = new HandOfCards(deck);

        System.out.println("Hand 1: " + hand1.toString());
        System.out.println("Hand 2: " + hand2.toString());
    }
}