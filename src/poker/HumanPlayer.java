package poker;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Orla on 17/03/2017.
*/
public class HumanPlayer extends PokerPlayer {

    public HumanPlayer(DeckOfCards deck){
        super(deck);
    }
        public boolean getRaise() {
            Scanner scanner = new Scanner(System.in);
            String raise = scanner.nextLine();
            if (raise.equalsIgnoreCase("Raise")) {
                return true;
            } else {
                return false;
            }
        }
    public boolean getFold() {
        Scanner scanner = new Scanner(System.in);
        String fold = scanner.nextLine();
        if (fold.equalsIgnoreCase("Fold")) {
            return true;
        } else {
            return false;
        }
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

    public boolean[] discardTList(){

        boolean[] discard_cards = {false, false, false, false, false};
        TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String  status =twit.getUserTweet("OrlaCullen15");

            //Process char String str = "true false true false false";

            String[] parts = status.split(" ");

            boolean[] array = new boolean[parts.length];
            for (int i = 0; i < parts.length; i++) {
                array[i] = Boolean.parseBoolean(parts[i]);
            }
           // System.out.println(Arrays.toString(array));


        for(int i=0; i<HandOfCards.CARDS_IN_HAND; i++){

            if(array[i]==true){
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
        TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
         String s =player.hand.toString();
          System.out.println("Before discard: " + player.hand.toString());
//twit.getUserTweet("OrlaCullen15");

       twit.postreply("This is your Hand"+player.hand.toString());
       boolean[] discardlist = player.discardList();
        player.discardCards(discardlist);
        //System.out.println("Discards: " + );
        twit.postreply("Hand after discard" +player.hand.toString());
        System.out.println("After discard: " + player.hand.toString());
    }
}

