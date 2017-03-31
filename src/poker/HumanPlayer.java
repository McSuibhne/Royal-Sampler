package poker;

import java.util.Scanner;

/**
 * Created by Orla on 17/03/2017.
*/
public class HumanPlayer extends PokerPlayer {

    public HumanPlayer(DeckOfCards deck){
        super(deck);
    }

        public boolean[] discardList(){

        Scanner scanner=new Scanner(System.in);


        boolean[] discard_cards = {false, false, false, false, false};
        System.out.println("Enter 1 to discard and 0 to keep");
        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){
            int j=scanner.nextInt();
            if(j==1){
                discard_cards[i] = true;
            }
            else
                discard_cards[i] =false;

        }



        return discard_cards;
    }

    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();
        HumanPlayer player = new HumanPlayer(deck);
       /* TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
         String s =player.hand.toString();
          System.out.println("Before discard: " + player.hand.toString());
         // twit.getUserTweet();
      // twit.postreply("This is your Hand"+player.hand.toString());

        boolean[] discardlist = player.discardList();
        player.discardCards(discardlist);
       // System.out.println("Discards: " + discards);
     //   twit.postreply("Hand after discard" +player.hand.toString());
        System.out.println("After discard: " + player.hand.toString());
    }
}

