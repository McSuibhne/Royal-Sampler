package poker;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

public class TestHandOfCards extends TestCase{

	private DeckOfCards test_deck;
    private HandOfCards test_hand;
    
    @Before
    public void setUp(){
    	test_deck = new DeckOfCards();
    	test_hand = new HandOfCards(test_deck);
    	ArrayList<ArrayList> test_list = new ArrayList<>();
    	//Test Hearts
        PlayingCard test_AH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_4H = new PlayingCard("4", PlayingCard.HEARTS, 4, 4);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
        PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);
        PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
        PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
        PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
        PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
        PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);

        //Test Diamonds
        PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
        PlayingCard test_10D = new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10);

        //Test Clubs
        PlayingCard test_2C = new PlayingCard("2", PlayingCard.CLUBS, 2, 2);
        PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);
        PlayingCard test_8C = new PlayingCard("8", PlayingCard.CLUBS, 8, 8);
        PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);
        PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);

        //Test Spades
        PlayingCard test_AS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
        PlayingCard test_6S = new PlayingCard("6", PlayingCard.SPADES, 6, 6);
        PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
        
        //Test Hands
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_JH, test_KH, test_QH, test_AH)));   //Royal Flush
        test_list.add(new ArrayList<>(Arrays.asList(test_AH, test_3H, test_4H, test_2H, test_5H)));    //Straight Flush (Ace Low)
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_JH, test_10S, test_10C, test_10D)));//Four of a Kind
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_10C, test_9C, test_9H, test_10S))); //Full House
        test_list.add(new ArrayList<>(Arrays.asList(test_5H, test_9H, test_KH, test_3H, test_AH)));    //Flush
        test_list.add(new ArrayList<>(Arrays.asList(test_10D, test_AS, test_KH, test_QH, test_JH)));   //Straight (Ace High)
        test_list.add(new ArrayList<>(Arrays.asList(test_AS, test_3H, test_4H, test_2D, test_5H)));    //Straight (Ace Low)
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_10C, test_KH, test_QH, test_10S))); //Three of a Kind
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_9H, test_KH, test_10S, test_9C)));  //Two Pair
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_9H, test_7C, test_QH, test_9C)));    //One Pair
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_9H, test_7C, test_QH, test_AS)));    //High Hand
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_3H, test_6H, test_AS, test_7C)));    //HighHand
        test_list.add(new ArrayList<>(Arrays.asList(test_JH, test_8C, test_KH, test_7C, test_QH)));   //High Hand
        test_list.add(new ArrayList<>(Arrays.asList(test_2H, test_3H, test_5H, test_6H, test_9H)));    //Flush (Busted Straight)
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_3H, test_4H, test_6H, test_5H)));    //Straight (Busted Flush)
        test_list.add(new ArrayList<>(Arrays.asList(test_KH, test_3H, test_2H, test_6S, test_9H)));    //High Hand (Busted Flush)
        test_list.add(new ArrayList<>(Arrays.asList(test_2C, test_3H, test_7C, test_6S, test_5H)));    //High Hand (Busted Straight)
        test_list.add(new ArrayList<>(Arrays.asList(test_JH, test_8C, test_KH, test_10S, test_2H)));   //High Hand
    }

    @After
    public void tearDown() {
        test_deck = null;
        test_hand = null;
        ArrayList<ArrayList> test_list = null;
        
        //Test Hearts
        PlayingCard test_AH = null;
        PlayingCard test_2H = null;
        PlayingCard test_3H = null;
        PlayingCard test_4H = null;
        PlayingCard test_5H = null;
        PlayingCard test_6H = null;
        PlayingCard test_9H = null;
        PlayingCard test_10H = null;
        PlayingCard test_JH = null;
        PlayingCard test_QH = null;
        PlayingCard test_KH = null;

        //Test Diamonds
        PlayingCard test_2D = null;
        PlayingCard test_10D = null;

        //Test Clubs
        PlayingCard test_2C = null;
        PlayingCard test_7C = null;
        PlayingCard test_8C = null;
        PlayingCard test_9C = null;
        PlayingCard test_10C = null;

        //Test Spades
        PlayingCard test_AS = null;
        PlayingCard test_6S = null;
        PlayingCard test_10S = null;
    }

    public void testIsRoyalFlush() {
    	test_hand.testSetter(test_list.get(0));
    	assertEquals("HandOfCards testIsRoyalFlush() is returning a false.", false, test_hand.isRoyalFlush());
    }
}
