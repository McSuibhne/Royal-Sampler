package poker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends PokerPlayer {
    public static final String NAMES_FILE = "src/poker/AINames.txt";
    public static final String TELLS_FILE = "src/poker/AITells.txt";
    public static final int NAMES_FILE_LENGTH = 60;
    public static final int TELLS_FILE_LENGTH = 20;
    public String tell;
    int ai_number;
    int discard_minimum;


    public AIPlayer(int ai_index, int discard_min){
        super(null);
        ai_number = ai_index;
        discard_minimum = discard_min;
        String name_line = getAiName(ai_number);
        name = name_line.split(";")[0];
        tell = getAiTrait(ai_number, name_line.split(";")[1]);
    }

    public void getBet(int highest_bet, int betting_round, int calls_since_raise, ArrayList<PokerPlayer> live_players){
        Random random = new Random();
        if(betting_round==1){
            discards = 0;
            bluff = false;
            if(highest_bet == 0) {
                if(hand.getGameValue() < HandOfCards.ONE_PAIR_VALUE) {
                    if(hand.getGameValue() < HandOfCards.ONE_PAIR_VALUE && discard_minimum < 75) {
                        bluff = true;
                    }
                    current_bet = highest_bet;
                }
                else {
                    if(random.nextInt(ai_number + 1 + ((int) (chips/STARTING_CHIPS))) == 0){
                        current_bet = highest_bet;
                    }
                    else {
                        current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(hand.getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
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
                bluff = false;
                if(hand.getGameValue() < HandOfCards.ONE_PAIR_VALUE){
                    if(hand.isDrawingHand() && calls_since_raise == live_players.size() - 2 && highest_bet < chips/4){
                        current_bet = highest_bet;
                    }
                    else {
                        current_bet = -1;
                    }
                }
                else {
                    if((int) (chips/(double)STARTING_CHIPS/2)*(hand.getGameValue()/(HandOfCards.ONE_PAIR_VALUE/2)) > 0){
                        current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(hand.getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
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
                        bluff = false;
                    }
                    current_bet = highest_bet + chips/3;
                }
                else if(hand.getGameValue() < HandOfCards.TWO_PAIR_VALUE){
                    current_bet = highest_bet;
                }
                else{
                    current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(hand.getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
                    if(hand.getGameValue() < HandOfCards.THREE_OF_A_KIND_VALUE + 6){
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
                    if(hand.getGameValue() < HandOfCards.TWO_PAIR_VALUE) {
                        if(highest_bet - current_bet > chips / 8) {
                            current_bet = highest_bet;
                        }
                        else {
                            current_bet = -1;
                        }
                    }
                    else {
                        current_bet = highest_bet + (int) ((chips/(double)(STARTING_CHIPS/2))*(hand.getGameValue()/(double)(HandOfCards.ONE_PAIR_VALUE/2)));
                        if(hand.getGameValue() < HandOfCards.THREE_OF_A_KIND_VALUE + 6){
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

        if(current_bet < highest_bet){
            current_bet = highest_bet;
        }

        if(current_bet >= chips){
            current_bet = chips;
            all_in = true;
        }

        int most_chips = 0;
        for(int i=0; i< live_players.size(); i++){
            if(live_players.get(i).chips > most_chips){
                most_chips = live_players.get(i).chips;
            }
        }

        if(current_bet > most_chips){
            current_bet = most_chips;
        }
    }

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


    // Method calls getDiscardProbability on each card index in the hand and compares the returned values to the
    // given discard minimum.
    // As this minimum is always <100 and cannot go below 0, a card with a discard probability of 100 will always be
    // traded while a card with a discard probability of 0 can never be traded.
    // The index of each card that should be traded is recorded in a boolean array and returned.
    // All print statements are for testing and can be later removed.
    public boolean[] discard(){
        boolean[] discard_cards = {false, false, false, false, false};
        if(!bluff) {
            System.out.println("Discard probability minimum: " + discard_minimum); //Testing
            int[][] discard_probability = hand.getDiscardProbability();
            System.out.println("Discard probabilities: "); //Testing
            for(int i = 0; i < 3; i++){
                System.out.print("[");
                for(int j=0; j < 5; j++){
                    System.out.print(discard_probability[i][j] + ", ");
                }
                System.out.println("]");
            }
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
}

