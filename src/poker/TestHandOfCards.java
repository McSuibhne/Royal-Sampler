package poker;

import junit.framework.TestCase;

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
    }

    @After
    public void tearDown() {
        test_deck = null;
        test_hand = null;
    }

    public void testIsRoyalFlush() {
        PlayingCard test_AH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
        PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
        PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
        PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
        PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_JH, test_KH, test_QH, test_AH});

    	assertEquals("HandOfCards testIsRoyalFlush() is returning a false for a correct Royal Flush.", true, test_hand.isRoyalFlush());
    }

    public void testIsStraightFlush() {
        PlayingCard test_AH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_4H = new PlayingCard("4", PlayingCard.HEARTS, 4, 4);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);

        test_hand.testSetter(new PlayingCard[]{test_AH, test_3H, test_4H, test_2H, test_5H});

        assertEquals("HandOfCards testIsStraightFlush() is returning a false for a correct Straight Flush.", true, test_hand.isStraightFlush());
    }
}
