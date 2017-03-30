package poker;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Orla on 17/03/2017.
*/
public class HumanPlayer extends PokerPlayer {

    public HumanPlayer(DeckOfCards deck){
        super(deck);


    }

        public int discard(){

        Scanner scanner=new Scanner(System.in);

        int discards = 0;
        boolean[] discard_cards = {false, false, false, false, false};

        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            int j=scanner.nextInt();


            if(j<5 && j>=0){
                discard_cards[j] = true;
            }
            else if(!(j<5 && j>=0)){
                System.out.println("Entered wrong input");

            }
        }
        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            if(discard_cards[i] && discards < 3) {
                hand.discardCard(i);
                discards++;
            }
        }

        hand.sort();
        return discards;
    }

    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();
        HumanPlayer player = new HumanPlayer(deck);
        TwitterBot twit = null;
        try {
            twit = new TwitterBot();
        } catch (IOException e) {
            e.printStackTrace();
        }
  String s =player.hand.toString();
          System.out.println("Before discard: " + player.hand.toString());
       // twit.postaStatus(player.hand.toString());
        int discards = player.discard();
        System.out.println("Discards: " + discards);
        System.out.println("After discard: " + player.hand.toString());

    }
}

