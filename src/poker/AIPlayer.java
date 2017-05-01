package poker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**Each AIPlayer is given a randomly selected 80s wrestler name and a signature "tell" that they sometimes display when
 * bluffing. The likelihood they will try to bluff, as well as their approach to discarding cards is determined by the
 * random (within boundaries) discard minimum determined by the game object in which the AIPlayer is made.*/
@SuppressWarnings("ForLoopReplaceableByForEach, WeakerAccess")
public class AIPlayer extends PokerPlayer {
    static final String NAMES_FILE = "src/poker/AINames.txt";
    static final String TELLS_FILE = "src/poker/AITells.txt";
    static final int NAMES_FILE_LENGTH = 60;
    static final int TELLS_FILE_LENGTH = 20;
    private String tell;
    private int ai_number;
    private int discard_minimum;

    /**AIPlayer constructor initialises the bot with its name and tell, read in from external text files*/
    AIPlayer(int ai_index, int discard_min){
        super(null);
        ai_number = ai_index;
        discard_minimum = discard_min;
        String name_line = getAiName(ai_number);
        name = name_line.split(";")[0];
        tell = getAiTrait(ai_number, name_line.split(";")[1]);
    }

    String getTell(){
        return tell;
    }

    /**getBet receives a large amount of information about the status of the round and opponents and returns a
     * call/raise/fold decision. They may also decide to bluff, depending on their hand and discard minimum.*/
    public void getBet(int highest_bet, int betting_round, int calls_since_raise, int pot, ArrayList<PokerPlayer> live_players){
        Random random = new Random();
        if(betting_round==1){
            bluff = false;
            if(highest_bet == 0) {
                if(getHand().getGameValue() < HandOfCards.ONE_PAIR_VALUE) {
                    if(getHand().getGameValue() < HandOfCards.ONE_PAIR_VALUE) {
                        bluff = true;
                    }
                    current_bet = highest_bet;
                }
                else {
                    if(random.nextInt(ai_number + 1 + (chips/STARTING_CHIPS)) == 0){
                        current_bet = highest_bet;
                    }
                    else {
                        current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(getHand().getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
                        if(current_bet >= chips) {
                            if(chips > RoundOfPoker.ANTE) {
                                current_bet = chips - RoundOfPoker.ANTE;
                            }
                            else {
                                current_bet = chips;
                            }
                        }
                    }
                }
            }
            else {
                if(getHand().getGameValue() < HandOfCards.ONE_PAIR_VALUE){
                    if(random.nextBoolean() && discard_minimum <= GameOfPoker.DISCARD_MAXIMUM - GameOfPoker.DISCARD_MINIMUM_RANGE) {
                        bluff = true;
                        current_bet = highest_bet;
                    }
                    else {
                        bluff = false;
                        if(getHand().isDrawingHand() && calls_since_raise == live_players.size() - 2 && highest_bet < chips / 4) {
                            current_bet = highest_bet;
                        }
                        else {
                            current_bet = -1;
                        }
                    }
                }
                else {
                    bluff = false;
                    if((int) (chips/(double)STARTING_CHIPS/2)*(getHand().getGameValue()/(HandOfCards.ONE_PAIR_VALUE/2)) > 0){
                        current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(getHand().getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
                    }
                    else if(chips < highest_bet - current_bet){
                        current_bet = chips;
                        all_in = true;
                    }
                    else {
                        current_bet = highest_bet;
                    }
                    if(current_bet > chips){
                        current_bet = chips;
                        all_in = true;
                    }
                }
            }
        }
        else {
            if(highest_bet == 0){
                if(bluff){
                    if(random.nextInt(3) != 2){
                        current_bet = highest_bet + chips/3;
                    }
                    else if(highest_bet < chips/2){
                        current_bet = highest_bet;
                    }
                    else {
                        bluff = false;
                        current_bet = -1;
                    }
                }
                else if(this.getHand().getGameValue() < HandOfCards.TWO_PAIR_VALUE){
                    current_bet = highest_bet;
                }
                else{
                    current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(getHand().getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
                    if(this.getHand().getGameValue() < HandOfCards.THREE_OF_A_KIND_VALUE + 6){
                        current_bet *= 2;
                    }
                    if(current_bet > chips){
                        current_bet = chips;
                        all_in = true;
                    }
                }
            }
            else {
                int bound = highest_bet - ai_number;
                if(bound < 0){
                    bound = 1;
                }
                if(bluff && random.nextInt(bound) < GameOfPoker.NUMBER_OF_BOTS) {
                    if(highest_bet < chips / 3) {
                        current_bet = highest_bet + chips / 3;
                    }
                    else {
                        if(highest_bet <= chips) {
                            current_bet = highest_bet;
                        }
                        else {
                            current_bet = chips;
                            all_in = true;
                        }
                    }
                }
                else {
                    if(this.getHand().getGameValue() < HandOfCards.TWO_PAIR_VALUE) {
                        if(highest_bet - current_bet > chips / 8) {
                            current_bet = highest_bet;
                        }
                        else {
                            current_bet = -1;
                        }
                    }
                    else {
                        current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(getHand().getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
                        if(this.getHand().getGameValue() < HandOfCards.THREE_OF_A_KIND_VALUE + 6){
                            current_bet *= 2;
                        }
                        if(current_bet > chips){
                            current_bet = chips;
                            all_in = true;
                        }
                    }
                }
            }
        }

        int most_chips = 0;
        for(int i=0; i< live_players.size(); i++){
            if(live_players.get(i).chips > most_chips && !live_players.get(i).getName().equals(name)){
                most_chips = live_players.get(i).chips;
            }
        }

        if(current_bet > most_chips){
            current_bet = most_chips;
        }

        if(current_bet < highest_bet){
            current_bet = highest_bet;
        }

        if(current_bet >= chips){
            current_bet = highest_bet;
            all_in = true;
        }
    }

    /**Method compares the getDiscardProbability of each card index to the discard minimum for each row of the 2D array.
     * If all elements of a row are above the discard minimum, that row is selected and the discard strategy is
     * returned in a boolean array. If there are no rows with all elements above the minimum, the method will discard
     * as many kickers as it can. */
    public boolean[] discard(){
        boolean[] discard_cards = {false, false, false, false, false};
        if(!bluff) {
            int[][] discard_probability = getHand().getDiscardProbability();
            boolean discard_occurred = false;
            for(int i = 0; i < 3 && !discard_occurred; i++) {
                for(int j = 0; j < HandOfCards.CARDS_IN_HAND; j++) {
                    if(discard_probability[i][j] > discard_minimum) {
                        discard_cards[j] = true;
                        discard_occurred = true;
                    }
                }
            }
        }
        return discard_cards;
    }

    /**Reads in a random (though non-repeatable) 80s wrestler name for the player from the external AINames file.*/
    private String getAiName(int ai_number){
        String ai_name = null;
        try{
            Random rand = new Random();
            int line_number = rand.nextInt(NAMES_FILE_LENGTH/GameOfPoker.NUMBER_OF_BOTS)*GameOfPoker.NUMBER_OF_BOTS + ai_number-1;
            BufferedReader reader = new BufferedReader(new FileReader(NAMES_FILE));
            String line;
            for(int i=0; i<=line_number; i++){
                line = reader.readLine();
                if(i==line_number){
                    ai_name = line;
                }
            }
            reader.close();
        }
        catch (Exception e){
            ai_name = "CPU Player "+ (ai_number) +";male";
        }
        return ai_name;
    }

    /**Reads in a random (though non-repeatable) poker bluffing "tell" for the player from the external AITells file.*/
    private String getAiTrait(int ai_number, String bot_gender){
        String tell = null;
        try{
            Random rand = new Random();
            int line_number = rand.nextInt(TELLS_FILE_LENGTH/GameOfPoker.NUMBER_OF_BOTS)*GameOfPoker.NUMBER_OF_BOTS + ai_number-1;
            BufferedReader reader = new BufferedReader(new FileReader(TELLS_FILE));
            for(int i=0; i<=line_number; i++){
                tell = reader.readLine();
            }
            if(tell != null) {
                String pronoun;
                if(bot_gender.equals("female")){
                    pronoun = "her";
                }
                else {
                    pronoun = "his";
                }

                tell = tell.replace("$", pronoun);
            }
            reader.close();
        }
        catch (Exception e){
            tell = null;
        }

        return tell;
    }

}

