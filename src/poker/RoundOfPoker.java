package poker;

import java.util.ArrayList;
import java.util.Random;

/**
 * The round object is created repeatedly by each GameOfPoker object and is the other main class responsible for the flow and timing
 * of the game. The structurally central "playRound" class makes use of many helper methods to accurately represent a poker game,
 * as well as the TwitterInterface object to respond to the user*/
@SuppressWarnings("StringConcatenationInLoop,ForLoopReplaceableByForEach")
class RoundOfPoker {
    static final int ANTE = 1;
    private int pot;
    private boolean player_folded = false;
    private TwitterInterface twitter;
    private ArrayList<PokerPlayer> live_players = new ArrayList<>();
    private DeckOfCards deck;
    private HumanPlayer human_player;

    /**Round constructor*/
    @SuppressWarnings("unchecked")
    RoundOfPoker(ArrayList<PokerPlayer> player_list, DeckOfCards card_deck, TwitterInterface twitterInterface) {
        pot = 0;
        deck = card_deck;
        twitter = twitterInterface;
        live_players = (ArrayList<PokerPlayer>) player_list.clone();
        human_player = (HumanPlayer) live_players.get(GameOfPoker.HUMAN_INDEX);
    }

    /**Method that governs the overall flow of the round*/
    void playRound() {
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
            twitter.postMessageToUser(live_players.get(winner_index).getName()+" has won the round and gets "+ pot +" chips!", human_player);
            live_players.get(winner_index).setChips(live_players.get(winner_index).getChips() + pot);
        }
    }

    /**Returns a String of the name and chip amount of all players at the start of each round*/
    private String countChips() {
        String tweet_message = "";
        for(int i = 0; i < live_players.size(); i++) {
            String chip_string;
            if(live_players.get(i).getChips() == 1) {
                chip_string = " chip";
            }
            else {
                chip_string = " chips";
            }
            tweet_message += live_players.get(i).getName() +" "+ live_players.get(i).getChips() + chip_string +"\n";
        }

        return tweet_message;
    }

    /**Adds the ante into the pot from each players' chips after ensuring they can afford to enter the round.*/
    private boolean payAnte() {
        boolean isHumanBusted = false;
        boolean ante_paid = true;
        String tweet_message = "";

        for(int i = 0; i < live_players.size(); i++) {
            if(live_players.get(i).getChips() >= ANTE) {
                live_players.get(i).setChips(live_players.get(i).getChips() - ANTE);
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

    /**Deals cards out to each player from the freshly-shuffled deck object initialized in the constructor*/
    private void dealCards() {
        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).deal(deck);
        }
        human_player.outputHand();
    }

    /**Attempts to find an opener (player with at least a pair of cards).
     * If no opener is found the round is restarted and the hands re-dealt.
     * If multiple possible openers are found one is selected at random to start off the betting*/
    private int findOpener() {
        ArrayList<Integer> opener_indexes = new ArrayList<>();
        int opener = -1;

        for(int i = 0; i < live_players.size(); i++) {
            if(live_players.get(i).getHand().getGameValue() > HandOfCards.ONE_PAIR_VALUE) {
                opener_indexes.add(i);
            }
        }

        if(opener_indexes.size() > 0) {
            Random rand = new Random();
            opener = opener_indexes.get(rand.nextInt(opener_indexes.size()));
        }

        return opener;
    }

    /**bet() method will loop continuously until all live players have provided the same number of chips to the pot or
     * have gone "all-in". An update is posted to the user every few actions to avoid hitting the character limit*/
    private void bet(int opener_index, int betting_round) {
        int highest_bet = 0;
        int calls_since_raise = 0;
        boolean betting_finished = false;

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

            if(current_player.isAllIn()) {
                calls_since_raise++;
                continue;
            }

            current_player.getBet(highest_bet, betting_round, calls_since_raise, pot, live_players);

            //Player has folded
            if(current_player.getCurrentBet() == -1) {
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
                if(current_player.isBluff()) {
                    try {
                        AIPlayer temp_player = (AIPlayer) current_player;
                        tweet_message += temp_player.getName() + " " + temp_player.getTell() + "\n";
                    } catch(Exception e) {
                        System.out.print(e.getMessage());   //Shouldn't ever be reached, bluff is never true for a Human Player
                    }
                }
                //Player has <= chips than bet and must go all-in
                if(current_player.getCurrentBet() <= highest_bet && current_player.isAllIn()) {
                    if(current_player.isAllIn()) {
                        tweet_message += current_player.getName() + " goes all-in to call" + "\n";
                    }
                    current_player.setChips(0);
                    pot += (current_player.getCurrentBet() - current_player.getPreviousBet());
                    current_player.setPreviousBet(current_player.getCurrentBet());
                    calls_since_raise++;
                }
                //Player has called
                else if(current_player.getCurrentBet() == highest_bet) {
                    if(highest_bet == 0) {
                        tweet_message += current_player.getName() + " checks\n";
                    }
                    else {
                        tweet_message += current_player.getName() + " calls\n";
                        current_player.setChips(current_player.getChips() - (current_player.getCurrentBet() - current_player.getPreviousBet()));
                        pot += (current_player.getCurrentBet() - current_player.getPreviousBet());
                        current_player.setPreviousBet(current_player.getCurrentBet());
                    }
                    calls_since_raise++;
                }
                //Player has raised
                else {
                    if(current_player.isAllIn()) {
                        tweet_message += current_player.getName() + " goes all-in to raise bet to " + current_player.getCurrentBet() + "\n";
                    }
                    else {
                        if(highest_bet == 0) {
                            tweet_message += current_player.getName() + " raises by " + current_player.getCurrentBet() + "\n";
                        }
                        else {
                            tweet_message += current_player.getName() + " sees " + highest_bet + " and raises by "
                                    + (current_player.getCurrentBet() - highest_bet) + " to " + current_player.getCurrentBet() + "\n";
                        }
                    }
                    highest_bet = current_player.getCurrentBet();
                    calls_since_raise = 0;
                    current_player.setChips(current_player.getChips() - (current_player.getCurrentBet() - current_player.getPreviousBet()));
                    pot += (current_player.getCurrentBet() - current_player.getPreviousBet());
                    current_player.setPreviousBet(current_player.getCurrentBet());
                }
            }
        }
        if(!tweet_message.equals("")) {
            twitter.postMessageToUser(tweet_message, human_player);
        }
    }

    /**Asks each player for the cards they wish to discard and trades them for replacements, outputting to twitter
     * the new hand of the human player and the number of cards each player discarded.*/
    private void discard() {
        String tweet_message = "";

        for(int i = 0; i < live_players.size(); i++) {
            live_players.get(i).discard_cards();
        }

        if(!player_folded) {
            human_player.outputHand();
        }

        for(int i = GameOfPoker.HUMAN_INDEX + 1; i < live_players.size(); i++){
            String discards_size;
            if(live_players.get(i).getDiscards() == 1){
                discards_size = " card";
            }
            else {
                discards_size = " cards";
            }
            tweet_message += live_players.get(i).getName() +" discards "+ live_players.get(i).getDiscards() + discards_size +"\n";
        }

        twitter.postMessageToUser(tweet_message, human_player);
    }

    /**Determines which of the remaining players has the best hand, tweeting the contents of all surviving players as
     * per a normal poker "showdown"*/
    private int findWinner() {
        int best_hand_index = 0;
        if(live_players.size() > 1) {
            String tweet_message = "";
            for(int i = 0; i < live_players.size(); i++) {
                tweet_message += live_players.get(i).getName() +" "+ live_players.get(i).getHand().toString() +"\n";

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
            if(live_players.get(i).getHand().getGameValue() > live_players.get(best_hand_index).getHand().getGameValue()) {
                best_hand_index = i;
            }
        }
        return best_hand_index;
    }

}