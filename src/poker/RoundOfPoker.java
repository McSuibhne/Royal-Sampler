package poker;

import java.io.*;
import java.util.*;

/**
 * Created by Andy on 30/03/2017.
 */

public class RoundOfPoker {
    public static final int ANTE = 1;
    private int pot;
    TwitterInterface twitter;
    ArrayList<PokerPlayer> live_players = new ArrayList<>();
    DeckOfCards d;
    public RoundOfPoker(ArrayList<PokerPlayer> player_list, DeckOfCards deck,TwitterInterface twitterInterface) {
        pot = 0;
        d= deck;
        twitter= twitterInterface;

        live_players = (ArrayList<PokerPlayer>) player_list.clone();
        playRound(live_players, d);   //It's probably better to call playRound from within the higher
        // higher class (game) instead of in the constructor.
    }
    public void runround(){
        playRound (live_players,d);
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
        int winner_index = 0;
        if(live_players.size() > 1) {
            bet(opener_index, live_players);    // first betting round
            discard(live_players);        //Temporarily removed to test findWinner function
            //bet(opener_index, live_players);  //second betting round
            checkForWinner(live_players);
            winner_index = findWinner(live_players);
        }

        HumanPlayer p = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
        long id= p.getTweetId();

        twitter.postreply(p.getName()+"Player" + live_players.get(winner_index).getName()+" has won the round",live_players.get(0));
        System.out.println(live_players.get(winner_index).getName() +" has won the round");
        live_players.get(winner_index).chips += pot;

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

           HumanPlayer p = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
            long id= p.getTweetId();
            twitter.postreply(p.getName()+" "+live_players.get(i).getName () + " you have " + live_players.get(i).chips + chip_string,live_players.get(0));
          //  System.out.println(live_players.get(i).getName () + " has " + live_players.get(i).chips + chip_string);
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
                System.out.println(live_players.get(i).getName () + "Has run out of chips and is eliminated from the round");
                if(i == GameOfPoker.HUMAN_INDEX) {
                    isHumanBusted = true;
                }
                live_players.remove(i);
            }
        }
        return isHumanBusted;
    }

    public void dealCards(ArrayList<PokerPlayer> live_players, DeckOfCards deck) {
       deck.reset();
        for(int i = 0; i < live_players.size(); i++) {

            live_players.get(i).deal(deck);

            System.out.println(live_players.get(i).getName() +":\t"+ live_players.get(i).hand.toString()); //Testing only!
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
                System.out.print(live_players.get(opener_indexes.get(i)).getName());
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

        HumanPlayer p = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
        long id= p.getTweetId();
twitter.postreply(p.getName()+" Player "+live_players.get(opener_index).getName() + " will open",live_players.get(0));
        System.out.println(live_players.get(opener_index).getName() + " will open");

        for(int i=opener_index; !betting_finished; i++){
            PokerPlayer current_player = live_players.get(i % live_players.size());

            if((highest_bet == 0 && calls_since_raise == live_players.size()) || (highest_bet>0 && calls_since_raise == live_players.size()-1)){
                betting_finished = true;
                continue;
            }

            current_player.getBet(highest_bet);
            if(current_player.current_bet == -1) {
                System.out.println(current_player.getName() +" folds");
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
                    System.out.println(current_player.getName() + " checks");
                }
                else{
                    System.out.println(current_player.getName() +" calls" );
                }
                calls_since_raise++;
            }
            else {
                if(highest_bet==0) {
                    System.out.println(current_player.getName() +" raises by "+ current_player.current_bet);
                }
                else {
                    System.out.println(current_player.getName() +" sees "+ highest_bet +" and raises by "+ (current_player.current_bet-highest_bet) +" to "+ current_player.current_bet);
                }
                highest_bet = current_player.current_bet;
                calls_since_raise = 0;
            }
        }
        for(int i=0; i<live_players.size(); i++){
            live_players.get(i).chips -= live_players.get(i).current_bet;           //TODO; IMPROVE THIS! TEMPORARY CODE, SHOULD BE DONE INSIDE MAIN LOOP
            pot += live_players.get(i).current_bet;
        }
    }

    public void discard(ArrayList<PokerPlayer> live_players) {
        boolean[] array;
        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).discards();
            //  live_players.get(i).discard_cards(array);
            System.out.println(live_players.get(i).getName() +":\t"+ live_players.get(i).hand.toString()); //Testing only!
            try {
                HumanPlayer temp_human = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
                temp_human.outputHand();
            }
            catch(Exception e) {    //Should never be reached, live_players.get(0) is either always the HumanPlayer or the game is over
            }
        }

    }

    public void checkForWinner(ArrayList<PokerPlayer> live_players) {
        //test code will be removed
        for (int i = 0; i < live_players.size(); i++) {

            System.out.println(live_players.get(i).getName() + " has a hand value of " + live_players.get(i).hand.getGameValue());

        }
    }

    public int findWinner(ArrayList<PokerPlayer> live_players) {
        int best_hand_index = 0;
        for(int i=1; i<live_players.size(); i++){

            if(live_players.get(i).hand.getGameValue() > live_players.get(best_hand_index).hand.getGameValue()) {
                best_hand_index = i;
            }
        }
        return best_hand_index;
    }

    public static void main(String[] args) {
//        DeckOfCards deck = new DeckOfCards();
//        ArrayList<PokerPlayer> player_list = new ArrayList<>();
//        Random rand = new Random();
//        TwitterInterface twitter = null;
//        player_list.add(new HumanPlayer(deck, twitter));
//        for(int i=GameOfPoker.HUMAN_INDEX+1; i<=GameOfPoker.NUMBER_OF_BOTS; i++){
//            int discard_minimum = rand.nextInt(GameOfPoker.DISCARD_MINIMUM_RANGE)+((100)-(GameOfPoker.DISCARD_MINIMUM_RANGE*i));
//            player_list.add(new AIPlayer(i, discard_minimum, deck));
//            System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
//        }
//        RoundOfPoker round = new RoundOfPoker(player_list, deck);
    }

}