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
 * This verifies that all getter methods within HandOfCards function correctly.
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

    public void testIsFourOfAKind() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
    	PlayingCard test_10D = new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_JH, test_10S, test_10C, test_10D});

        assertEquals("HandOfCards testIsFourOfAKind() is returning a false for a correct Four of a Kind.", true, test_hand.isFourOfAKind());
    }

    public void testIsFullHouse() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
        PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
        PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);
        PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
        PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_10C, test_9C, test_9H, test_10S});

        assertEquals("HandOfCards testIsFullHouse() is returning a false for a correct Full House.", true, test_hand.isFullHouse());
    }

    public void testIsFlush() {
    	PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
    	PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
    	PlayingCard test_AH = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);

    	test_hand.testSetter(new PlayingCard[]{test_5H, test_9H, test_KH, test_3H, test_AH});

        assertEquals("HandOfCards testIsFlush() is returning a false for a correct Flush.", true, test_hand.isFlush());
    }

    public void testIsStraightAceHigh() {
    	PlayingCard test_10D = new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10);
    	PlayingCard test_AS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);

    	test_hand.testSetter(new PlayingCard[]{test_10D, test_AS, test_KH, test_QH, test_JH});

        assertEquals("HandOfCards testIsStraightAceHigh() is returning a false for a correct Straight (Ace High).", true, test_hand.isStraight());
    }

    public void testIsStraightAceLow() {
    	PlayingCard test_AS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
    	PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
    	PlayingCard test_4H = new PlayingCard("4", PlayingCard.HEARTS, 4, 4);
    	PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
    	PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);

    	test_hand.testSetter(new PlayingCard[]{test_AS, test_3H, test_4H, test_2D, test_5H});

        assertEquals("HandOfCards testIsStraightAceLow() is returning a false for a correct Straight (Ace Low).", true, test_hand.isStraight());
    }

    public void testIsThreeOfAKind() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
        PlayingCard test_10C = new PlayingCard("10", PlayingCard.CLUBS, 10, 10);
        PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
        PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
        PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);

        test_hand.testSetter(new PlayingCard[]{test_10H, test_10C, test_KH, test_QH, test_10S});

        assertEquals("HandOfCards testIsThreeOfAKind() is returning a false for a correct Three of a Kind.", true, test_hand.isThreeOfAKind());
    }

    public void testIsTwoPair() {
    	PlayingCard test_10H = new PlayingCard("10", PlayingCard.HEARTS, 10, 10);
    	PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);

    	test_hand.testSetter(new PlayingCard[]{test_10H, test_9H, test_KH, test_10S, test_9C});

        assertEquals("HandOfCards testIsTwoPair() is returning a false for a correct Two Pair.", true, test_hand.isTwoPair());

    }

    public void testIsOnePair() {
    	PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
    	PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);
    	PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);
    	PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);
    	PlayingCard test_9C = new PlayingCard("9", PlayingCard.CLUBS, 9, 9);

    	test_hand.testSetter(new PlayingCard[]{test_2D, test_9H, test_7C, test_QH, test_9C});

        assertEquals("HandOfCards testIsOnePair() is returning a false for a correct One Pair.", true, test_hand.isOnePair());
    }

    public void testIsHighHandAceHigh() {
    	PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
    	PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
    	PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);
    	PlayingCard test_AS = new PlayingCard("A", PlayingCard.SPADES, 1, 14);
    	PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);

    	test_hand.testSetter(new PlayingCard[]{test_2D, test_3H, test_6H, test_AS, test_7C});

        assertEquals("HandOfCards testIsHighHandAceHigh() is returning a false for a correct High Hand (Ace High).", true, test_hand.isHighHand());
    }

    public void testIsHighHandKingHigh() {
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
    	PlayingCard test_8C = new PlayingCard("8", PlayingCard.CLUBS, 8, 8);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);
    	PlayingCard test_QH = new PlayingCard("Q", PlayingCard.HEARTS, 12, 12);

    	test_hand.testSetter(new PlayingCard[]{test_JH, test_8C, test_KH, test_7C, test_QH});

        assertEquals("HandOfCards testIsHighHandKingHigh() is returning a false for a correct High Hand (King High).", true, test_hand.isHighHand());
    }

    public void testIsFlushBustedStraight() {
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
        PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);
        PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);

        test_hand.testSetter(new PlayingCard[]{test_2H, test_3H, test_5H, test_6H, test_9H});

        assertEquals("HandOfCards testIsFlushBustedStraight() is returning a false for a correct Flush (Busted Straight).", true, test_hand.isFlush());
    }

    public void testIsStraightBustedFlush() {
    	PlayingCard test_2D = new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2);
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_4H = new PlayingCard("4", PlayingCard.HEARTS, 4, 4);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);
        PlayingCard test_6H = new PlayingCard("6", PlayingCard.HEARTS, 6, 6);

        test_hand.testSetter(new PlayingCard[]{test_2D, test_3H, test_4H, test_5H, test_6H});

        assertEquals("HandOfCards testIsStraightBustedFlush() is returning a false for a correct Straight (Busted Flush).", true, test_hand.isStraight());
    }

    public void testIsHighHandBustedFlush() {
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
        PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);
        PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_6S = new PlayingCard("6", PlayingCard.SPADES, 6, 6);
        PlayingCard test_9H = new PlayingCard("9", PlayingCard.HEARTS, 9, 9);

        test_hand.testSetter(new PlayingCard[]{test_KH, test_3H, test_2H, test_6S, test_9H});

        assertEquals("HandOfCards testIsHighHandBustedFlush() is returning a false for a correct High Hand (Busted Flush).", true, test_hand.isHighHand());
    }

    public void testIsHighHandBustedStraight() {
    	PlayingCard test_2C = new PlayingCard("2", PlayingCard.CLUBS, 2, 2);
    	PlayingCard test_3H = new PlayingCard("3", PlayingCard.HEARTS, 3, 3);
        PlayingCard test_7C = new PlayingCard("7", PlayingCard.CLUBS, 7, 7);
        PlayingCard test_6S = new PlayingCard("6", PlayingCard.SPADES, 6, 6);
        PlayingCard test_5H = new PlayingCard("5", PlayingCard.HEARTS, 5, 5);

        test_hand.testSetter(new PlayingCard[]{test_2C, test_3H, test_7C, test_6S, test_5H});

        assertEquals("HandOfCards testIsHighHandBustedStraight() is returning a false for a correct High Hand (Busted Straight).", true, test_hand.isHighHand());
    }

    public void testIsHighHandNoFlushNoStraight() {
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
    	PlayingCard test_8C = new PlayingCard("8", PlayingCard.CLUBS, 8, 8);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);

    	test_hand.testSetter(new PlayingCard[]{test_JH, test_8C, test_KH, test_10S, test_2H});

        assertEquals("HandOfCards testIsHighHandNoFlushNoStraight() is returning a false for a correct High Hand (No Flush / No Straight).", true, test_hand.isHighHand());
    }

    public void testGetGameValue() {
    	PlayingCard test_JH = new PlayingCard("J", PlayingCard.HEARTS, 11, 11);
    	PlayingCard test_8C = new PlayingCard("8", PlayingCard.CLUBS, 8, 8);
    	PlayingCard test_KH = new PlayingCard("K", PlayingCard.HEARTS, 13, 13);
    	PlayingCard test_10S = new PlayingCard("10", PlayingCard.SPADES, 10, 10);
    	PlayingCard test_2H = new PlayingCard("2", PlayingCard.HEARTS, 2, 2);

    	test_hand.testSetter(new PlayingCard[]{test_JH, test_8C, test_KH, test_10S, test_2H});

        assertEquals("HandOfCards testGetGameValue() is returning an incorrect value for a High Hand.", 531666, test_hand.getGameValue());
    }
}

/*
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
 */
