package poker;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class AIPlayer extends PokerPlayer {
    public static final String NAMES_FILE = "src/poker/AINames.txt";
    public static final int NAMES_FILE_LENGTH = 80;
    int discard_minimum;

    public AIPlayer(int ai_number, int discard_min, DeckOfCards deck){
        super(null, deck);
        name = getAiName(ai_number);
        discard_minimum = discard_min;
    }

    public void getBet(int highest_bet){
        current_bet = highest_bet;

    }

    private String getAiName(int ai_number){
        String ai_name = null;
        try{
            Random rand = new Random();
            int line_number = rand.nextInt(NAMES_FILE_LENGTH/GameOfPoker.NUMBER_OF_BOTS) + ((ai_number-1)*(NAMES_FILE_LENGTH/GameOfPoker.NUMBER_OF_BOTS));
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
            ai_name = "CPU Player " + (ai_number);
        }
        return ai_name;
    }

    // Method calls getDiscardProbability on each card index in the hand and compares the returned values to the
    // given discard minimum.
    // As this minimum is always <100 and cannot go below 0, a card with a discard probability of 100 will always be
    // traded while a card with a discard probability of 0 can never be traded.
    // The index of each card that should be traded is recorded in a boolean array and returned.
    // All print statements are for testing and can be later removed.
    public void discards(){
        int discards = 0;
        boolean[] discard_cards = {false, false, false, false, false};
        System.out.println("Discard probability minimum: " + discard_minimum); //Testing
        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            int discard_probability = hand.getDiscardProbability(i);
            System.out.println("Card " + i + " discard probability: " + discard_probability); //Testing

            if(discard_probability > discard_minimum){
                discard_cards[i] = true;
            }
        }
        discard_cards(discard_cards);

    }
}
