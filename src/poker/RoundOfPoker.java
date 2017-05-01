package poker;

import java.util.ArrayList;
import java.util.Random;

public class RoundOfPoker {
    public static final int ANTE = 1;
    public int pot;
    boolean player_folded = false;
    TwitterInterface twitter;
    ArrayList<PokerPlayer> live_players = new ArrayList<>();
    DeckOfCards deck;
    HumanPlayer human_player;

    public RoundOfPoker(ArrayList<PokerPlayer> player_list, DeckOfCards card_deck, TwitterInterface twitterInterface) {
        pot = 0;
        deck = card_deck;
        twitter = twitterInterface;
        live_players = (ArrayList<PokerPlayer>) player_list.clone();
        human_player = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
    }

    public void playRound() {
        for(int i=0; i<live_players.size(); i++){
            live_players.get(i).discards = 0;
        }
        int opener_index = -1;
        boolean player_busted = false;
        while(opener_index == -1) {
            twitter.postMessageToUser("New Round:\n"+ countChips(), human_player);
            player_busted = payAnte();
            if(player_busted) {
                break;
            }
            dealCards();
            opener_index = findOpener();
            if(opener_index == -1) {
                twitter.postMessageToUser("No one can open the betting, restarting round", human_player);
                deck.reset();
            }
        }
        int winner_index;
        if(!player_busted) {
            if(live_players.size() > 1) {
                bet(opener_index, 1);    // first betting round
                if(live_players.size() > 1) {
                    discard();
                    bet(opener_index, 2);  //second betting round
                }
            }
            winner_index = findWinner();
            twitter.postMessageToUser("Player "+live_players.get(winner_index).getName()+" has won the round and gets "+ pot +" chips!", human_player);
            live_players.get(winner_index).chips += pot;
        }
    }

    public String countChips() {
        String tweet_message = "";
        for(int i = 0; i < live_players.size(); i++) {
            String chip_string;
            if(live_players.get(i).chips == 1) {
                chip_string = " chip";
            }
            else {
                chip_string = " chips";
            }
            tweet_message += live_players.get(i).getName() +" "+ live_players.get(i).chips + chip_string +"\n";
        }

        return tweet_message;
    }

    public boolean payAnte() {
        boolean isHumanBusted = false;
        boolean ante_paid = true;
        String tweet_message = "";

        for(int i = 0; i < live_players.size(); i++) {
            if(live_players.get(i).chips >= ANTE) {
                live_players.get(i).chips -= ANTE;
                pot += ANTE;
            }
            else {
                tweet_message += live_players.get(i).getName () + " can't pay the ante and is eliminated from the round";
                ante_paid = false;
                if(i == GameOfPoker.HUMAN_INDEX) {
                    isHumanBusted = true;
                }
                else{
                    live_players.remove(i);
                }
            }
        }

        if(ante_paid){
            tweet_message = "All players can pay the " +ANTE+ " chip ante";
        }

        if(!tweet_message.equals("")){
            twitter.postMessageToUser(tweet_message, human_player);
        }

        return isHumanBusted;
    }

    public void dealCards() {
        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).deal(deck);
            //System.out.println(live_players.get(i).getName() + ":\t" + live_players.get(i).hand.toString()); //Testing only!
        }
        human_player.outputHand();
    }

    public int findOpener() {
        ArrayList<Integer> opener_indexes = new ArrayList<>();
        int opener = -1;

        for(int i = 0; i < live_players.size(); i++) {
            if(live_players.get(i).hand.getGameValue() > HandOfCards.ONE_PAIR_VALUE) {
                opener_indexes.add(i);
            }
        }

        if(opener_indexes.size() > 0) {
            Random rand = new Random();
            opener = opener_indexes.get(rand.nextInt(opener_indexes.size()));
        }

        return opener;
    }

    public void bet(int opener_index, int betting_round) {
        int highest_bet = 0;
        int calls_since_raise = 0;
        boolean betting_finished = false;

        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).current_bet = -1;
            live_players.get(i).previous_bet = 0;
        }

        if(opener_index >= live_players.size()) {
            opener_index = 0;
        }

        String tweet_message = "Betting round " + betting_round + ", " + live_players.get(opener_index).getName() + " will open\n";

        for(int i = opener_index; !betting_finished; i++) {
            PokerPlayer current_player = live_players.get(i % live_players.size());

            if((i % live_players.size() == GameOfPoker.HUMAN_INDEX && !tweet_message.equals("")) || tweet_message.length() > 75) {
                twitter.postMessageToUser(tweet_message, human_player);
                tweet_message = "";
            }

            if((highest_bet == 0 && calls_since_raise == live_players.size()) || (highest_bet > 0 && calls_since_raise == live_players.size() - 1)) {
                betting_finished = true;
                continue;
            }

            if(current_player.all_in) {
                calls_since_raise++;
                continue;
            }

            current_player.getBet(highest_bet, betting_round, calls_since_raise, pot, live_players);

            //Player has folded
            if(current_player.current_bet == -1) {
                tweet_message += current_player.getName() + " folds" + "\n";
                int index = i % live_players.size();
                if(index == 0) {
                    player_folded = true;
                }
                live_players.remove(i % live_players.size());
                i = live_players.size() + index - 1;
                if(live_players.size() <= 1) {
                    break;
                }
            }
            else {
                if(current_player.bluff) {
                    try {
                        AIPlayer temp_player = (AIPlayer) current_player;
                        tweet_message += temp_player.getName() + " " + temp_player.tell + "\n";
                    } catch(Exception e) {
                    }   //Shouldn't ever be reached, bluff is never true for a Human Player
                }
                //Player has <= chips than bet and must go all-in
                if(current_player.current_bet <= highest_bet && current_player.all_in) {
                    if(current_player.all_in) {
                        tweet_message += current_player.getName() + " goes all-in to call" + "\n";
                    }
                    current_player.chips = 0;
                    pot += (current_player.current_bet - current_player.previous_bet);
                    current_player.previous_bet = current_player.current_bet;
                    calls_since_raise++;
                }
                //Player has called
                else if(current_player.current_bet == highest_bet) {
                    if(highest_bet == 0) {
                        tweet_message += current_player.getName() + " checks\n";
                    }
                    else {
                        tweet_message += current_player.getName() + " calls\n";
                        current_player.chips -= (current_player.current_bet - current_player.previous_bet);
                        pot += (current_player.current_bet - current_player.previous_bet);
                        current_player.previous_bet = current_player.current_bet;
                    }
                    calls_since_raise++;
                }
                //Player has raised
                else {
                    if(current_player.all_in) {
                        tweet_message += current_player.getName() + " goes all-in to raise bet to " + current_player.current_bet + "\n";
                    }
                    else {
                        if(highest_bet == 0) {
                            tweet_message += current_player.getName() + " raises by " + current_player.current_bet + "\n";
                        }
                        else {
                            tweet_message += current_player.getName() + " sees " + highest_bet + " and raises by "
                                    + (current_player.current_bet - highest_bet) + " to " + current_player.current_bet + "\n";
                        }
                    }
                    highest_bet = current_player.current_bet;
                    calls_since_raise = 0;
                    current_player.chips -= (current_player.current_bet - current_player.previous_bet);
                    pot += (current_player.current_bet - current_player.previous_bet);
                    current_player.previous_bet = current_player.current_bet;
                }
            }
        }
        if(!tweet_message.equals("")) {
            twitter.postMessageToUser(tweet_message, human_player);
        }
    }

    public void discard() {
        String tweet_message = "";

        for(int i = 0; i < live_players.size(); i++) {
            //System.out.println(live_players.get(i).getName() + ":\t" + live_players.get(i).hand.toString()); //Testing only!
            live_players.get(i).discard_cards();
            //System.out.println(live_players.get(i).getName() + ":\t" + live_players.get(i).hand.toString()); //Testing only!
        }

        if(!player_folded) {
            human_player.outputHand();
        }

        for(int i = GameOfPoker.HUMAN_INDEX + 1; i < live_players.size(); i++){
            String discards_size;
            if(live_players.get(i).discards == 1){
                discards_size = " card";
            }
            else {
                discards_size = " cards";
            }
            tweet_message += live_players.get(i).getName() +" discards "+ live_players.get(i).discards + discards_size +"\n";
        }

        twitter.postMessageToUser(tweet_message, human_player);
    }

    public int findWinner() {
        int best_hand_index = 0;
        if(live_players.size() > 1) {
            String tweet_message = "";
            for(int i = 0; i < live_players.size(); i++) {
                tweet_message += live_players.get(i).getName() +" "+ live_players.get(i).hand.toString() +"\n";

                if(!tweet_message.equals("") && tweet_message.length() > 90) {
                    twitter.postMessageToUser(tweet_message, human_player);
                    tweet_message = "";
                }
            }
            if(!tweet_message.equals("")) {
                twitter.postMessageToUser(tweet_message, human_player);
            }
        }

        for(int i = best_hand_index + 1; i < live_players.size(); i++){
            if(live_players.get(i).hand.getGameValue() > live_players.get(best_hand_index).hand.getGameValue()) {
                best_hand_index = i;
            }
        }
        return best_hand_index;
    }

    public static void main(String[] args) {
        /*DeckOfCards deck = new DeckOfCards();
        ArrayList<PokerPlayer> player_list = new ArrayList<>();
        Random rand = new Random();
        TwitterInterface twitter = null;
        player_list.add(new HumanPlayer(deck, twitter));
        for(int i=GameOfPoker.HUMAN_INDEX+1; i<=GameOfPoker.NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(GameOfPoker.DISCARD_MINIMUM_RANGE)+((100)-(GameOfPoker.DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum, deck));
            System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
        }
        RoundOfPoker round = new RoundOfPoker();
        round.playRound(player_list, deck);*/
    }

}