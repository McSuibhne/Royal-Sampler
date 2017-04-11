package poker;

import java.io.*;
import java.util.*;

/**
 * Created by Andy on 30/03/2017.
 */

public class RoundOfPoker {
    public static final int ANTE = 1;
    private int pot;
    public static final int NUMBER_OF_BOTS = 4;
    public static final int DISCARD_MINIMUM_RANGE = 20;
    public static final int HUMAN_INDEX = 0;
    
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
        if(!player_busted && live_players.size() > 1) {
        	bet(opener_index, live_players);
        	//discard(live_players,deck);
            checkforWinner(live_players,deck);
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
    public void discard(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        boolean[] array;
        for(int i = 0; i < live_players.size(); i++) {
           live_players.get(i).discards();
         //  live_players.get(i).discard_cards(array);
            System.out.println(live_players.get(i).name +":\t"+ live_players.get(i).hand.toString()); //Testing only!
        }

    }
    public void checkforWinner(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        //test code will be removed
        for (int i = 0; i < live_players.size(); i++) {
            System.out.println(live_players.get(i).name + " has a hand value of " + live_players.get(i).hand.getGameValue());

        }
        compare(live_players,deck);
    }
    public void compare(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        if ((live_players.get(0).hand.getGameValue() >= live_players.get(1).hand.getGameValue()) && (live_players.get(0).hand.getGameValue() >= live_players.get(2).hand.getGameValue()) && (live_players.get(0).hand.getGameValue() >= live_players.get(3).hand.getGameValue())){ // a >= b,c,d,e

            System.out.println(live_players.get(0).name + " is the Winner of this Round: " + live_players.get(0).hand.getGameValue());
        } else if (((live_players.get(1).hand.getGameValue() >= (live_players.get(2).hand.getGameValue())) && (live_players.get(1).hand.getGameValue() >= (live_players.get(3).hand.getGameValue())) )) {      // b >= c,d,e
            System.out.println(live_players.get(1).name + " is the Winner of this Round: " + live_players.get(1).hand.getGameValue());
        } else if ((live_players.get(2).hand.getGameValue() >= live_players.get(3).hand.getGameValue()) ) {                  // c >= d,e
            System.out.println(live_players.get(2).name + " is the Winner of this Round: " + (live_players.get(2).hand.getGameValue()));

        } else {
            System.out.println(live_players.get(3).name + " is the Winner of this Round: " + (live_players.get(3).hand.getGameValue()));
        }
    }

    public void dealCards(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).deal(deck);
            System.out.println(live_players.get(i).name +":\t"+ live_players.get(i).hand.toString()); //Testing only!
        }
        try {
            HumanPlayer temp_human = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
            temp_human.outputHand();
        }
        catch(Exception e) {    //Should never be reached, live_players.get(0) is either always the HumanPlayer or the game is over
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
                    if(opener_indexes.size()>2) {
                        System.out.print(", ");
                    }
                    else {
                        System.out.print(" ");
                    }
                }
                if(opener_indexes.size() > 1 && i == opener_indexes.size() - 1) {
                    System.out.print("and ");
                }
                System.out.print(live_players.get(opener_indexes.get(i)).name);
            }
            if(opener_indexes.size() > 0) {
                Random rand = new Random();
                opener = opener_indexes.get(rand.nextInt(opener_indexes.size()));
                System.out.println(" can open the betting");
            }
            else {
                System.out.println("No one can open the betting, restarting round");
            }
        }
        return opener;
    }

    public void bet(int opener_index, ArrayList<PokerPlayer> live_players) {
        int highest_bet = 0;
        int calls_since_raise = 0;
        boolean betting_finished = false;

        System.out.println(live_players.get(opener_index).name + " will open");

        for(int i=opener_index; !betting_finished; i++){
            PokerPlayer current_player = live_players.get(i % live_players.size());

            if((highest_bet == 0 && calls_since_raise == live_players.size()) || (highest_bet>0 && calls_since_raise == live_players.size()-1)){
                betting_finished = true;
                continue;
            }

            current_player.getBet(highest_bet);
            if(current_player.current_bet == -1) {
                System.out.println(current_player.name +" folds");
                live_players.remove(i % live_players.size());
                i+=2;
                if(live_players.size() <= 1){
                    break;
                }
                else {
                    continue;
                }
            }
            else if(current_player.current_bet == highest_bet) {
                if(highest_bet == 0) {
                    System.out.println(current_player.name + " checks");
                }
                else{
                    System.out.println(current_player.name +" calls with ");
                }
                calls_since_raise++;
            }
            else {
                if(highest_bet==0) {
                    System.out.println(current_player.name +" raises by "+ current_player.current_bet);
                }
                else {
                    System.out.println(current_player.name +" sees "+ highest_bet +" and raises by "+ (current_player.current_bet-highest_bet) +" to "+ current_player.current_bet);
                }
                highest_bet = current_player.current_bet;
                calls_since_raise = 0;
            }
        }
    }

    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();
        ArrayList<PokerPlayer> player_list = new ArrayList<>();
        Random rand = new Random();
        TwitterInterface twitter = null;
        player_list.add(new HumanPlayer(deck, "human_player", twitter));
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+((100)-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum, deck));
            System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
        }
        RoundOfPoker round = new RoundOfPoker(player_list, deck);
    }

}