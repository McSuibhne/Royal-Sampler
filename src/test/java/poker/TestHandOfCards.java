package poker;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

/** Created by Andy on 29/04/2017 **/

/**
 * Test class for HandOfCards.
 * setUp() creates a HandOfCards and DeckOfCards object, passing to them the required parameters.
 * The @Before annotation is required to force setUp() to be executed before the tests themselves, ensuring both test
 *  card objects are already made and ready to have each of their returning methods called.
 * This verifies that all methods within HandOfCards function correctly.
 */

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

    	assertEquals("HandOfCards isRoyalFlush() is returning a false for a correct Royal Flush.", true, test_hand.isRoyalFlush());
    }

    public void testIsStraightFlush() {
        PlayingCard test_AH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_4H = new PlayingCard("4", PlayingCard.HEARTS, 4, 4);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);

        test_hand.testSetter(new PlayingCard[]{test_AH, test_3H, test_4H, test_2H, test_5H});

        assertEquals("HandOfCards isStraightFlush() is returning a false for a correct Straight Flush.", true, test_hand.isStraightFlush());
    }

    public void testIsFourOfAKind() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
    	PlayingCard test_10D = new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_JH, test_10S, test_10C, test_10D});

        assertEquals("HandOfCards isFourOfAKind() is returning a false for a correct Four of a Kind.", true, test_hand.isFourOfAKind());
    }

    public void testIsFullHouse() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
        PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
        PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);
        PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
        PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_10C, test_9C, test_9H, test_10S});

        assertEquals("HandOfCards isFullHouse() is returning a false for a correct Full House.", true, test_hand.isFullHouse());
    }

    public void testIsFlush() {
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
    	PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
    	PlayingCard test_AH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
        PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);

    	test_hand.testSetter(new PlayingCard[]{test_5H, test_9H, test_KH, test_3H, test_AH});
        assertEquals("HandOfCards isFlush() is returning a false for a correct Flush.", true, test_hand.isFlush());

        //Busted Straight
        test_hand.testSetter(new PlayingCard[]{test_2H, test_3H, test_5H, test_6H, test_9H});
        assertEquals("HandOfCards isFlush() is returning a false for a correct Flush (Busted Straight).", true, test_hand.isFlush());

    }

    public void testIsStraight() {
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_4H = new PlayingCard("4", PlayingCard.HEARTS, 4, 4);
        PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
    	PlayingCard test_10D = new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10);
    	PlayingCard test_AS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
        PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);

        //Ace High
        test_hand.testSetter(new PlayingCard[]{test_10D, test_AS, test_KH, test_QH, test_JH});
        assertEquals("HandOfCards isStraight() is returning a false for a correct Straight (Ace High).", true, test_hand.isStraight());

        //Ace Low
        test_hand.testSetter(new PlayingCard[]{test_AS, test_3H, test_4H, test_2D, test_5H});
        assertEquals("HandOfCards isStraight() is returning a false for a correct Straight (Ace Low).", true, test_hand.isStraight());

        //Busted Flush
        test_hand.testSetter(new PlayingCard[]{test_2D, test_3H, test_4H, test_5H, test_6H});
        assertEquals("HandOfCards isStraight() is returning a false for a correct Straight (Busted Flush).", true, test_hand.isStraight());

    }

    public void testIsThreeOfAKind() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
        PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
        PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
        PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
        PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_10C, test_KH, test_QH, test_10S});

        assertEquals("HandOfCards isThreeOfAKind() is returning a false for a correct Three of a Kind.", true, test_hand.isThreeOfAKind());
    }

    public void testIsTwoPair() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
    	PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);

    	test_hand.testSetter(new PlayingCard[]{test_10H, test_9H, test_KH, test_10S, test_9C});

        assertEquals("HandOfCards isTwoPair() is returning a false for a correct Two Pair.", true, test_hand.isTwoPair());

    }

    public void testIsOnePair() {
    	PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
    	PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
    	PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);
    	PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
    	PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);

    	test_hand.testSetter(new PlayingCard[]{test_2D, test_9H, test_7C, test_QH, test_9C});

        assertEquals("HandOfCards isOnePair() is returning a false for a correct One Pair.", true, test_hand.isOnePair());
    }

    public void testIsHighHand() {
        PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
        PlayingCard test_8C = new PlayingCard("8", PlayingCard.CLUBS, 8, 8);
        PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
        PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
    	PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
    	PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
    	PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);
    	PlayingCard test_AS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
    	PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_6S = new PlayingCard("6", PlayingCard.SPADES, 6, 6);
        PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
        PlayingCard test_2C = new PlayingCard("2", PlayingCard.CLUBS, 2, 2);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
        PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);

    	//Ace High
    	test_hand.testSetter(new PlayingCard[]{test_2D, test_3H, test_6H, test_AS, test_7C});
        assertEquals("HandOfCards isHighHand() is returning a false for a correct High Hand (Ace High).", true, test_hand.isHighHand());

        //King High
        test_hand.testSetter(new PlayingCard[]{test_JH, test_8C, test_KH, test_7C, test_QH});
        assertEquals("HandOfCards isHighHand() is returning a false for a correct High Hand (King High).", true, test_hand.isHighHand());

        //Busted Flush
        test_hand.testSetter(new PlayingCard[]{test_KH, test_3H, test_2H, test_6S, test_9H});
        assertEquals("HandOfCards isHighHand() is returning a false for a correct High Hand (Busted Flush).", true, test_hand.isHighHand());

        //Busted Straight
        test_hand.testSetter(new PlayingCard[]{test_2C, test_3H, test_7C, test_6S, test_5H});
        assertEquals("HandOfCards isHighHand() is returning a false for a correct High Hand (Busted Straight).", true, test_hand.isHighHand());

        //No Flush or Straight
        test_hand.testSetter(new PlayingCard[]{test_JH, test_8C, test_KH, test_10S, test_2H});
        assertEquals("HandOfCards isHighHand() is returning a false for a correct High Hand (No Flush / No Straight).", true, test_hand.isHighHand());

    }

    public void testGetGameValue() {
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
    	PlayingCard test_8C = new PlayingCard("8", PlayingCard.CLUBS, 8, 8);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);

    	test_hand.testSetter(new PlayingCard[]{test_JH, test_8C, test_KH, test_10S, test_2H});

        assertEquals("HandOfCards getGameValue() is returning an incorrect value for a High Hand.", 531666, test_hand.getGameValue());
    }
}