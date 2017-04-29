package poker;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

/** Created by Andy on 29/04/2017 **/

/**
 * Test class for PlayingCard.
 * setUp() creates two PlayingCard objects, passing to them the required parameters.
 * The @Before annotation is required to force setUp() to be executed before the tests themselves, ensuring both test
 *  card objects are already made and ready to have each of their returning methods called.
 * This verifies that all getter methods within PlayingCard function correctly.
 */
public class PlayingCardTest extends TestCase{
    private PlayingCard test_card_1;
    private PlayingCard test_card_2;

    @Before
    public void setUp(){
        test_card_1 = new PlayingCard("A", PlayingCard.HEARTS, 1, 14);
        test_card_2 = new PlayingCard("5", PlayingCard.SPADES, 5, 5);
    }

    @After
    public void tearDown() {
        test_card_1 = null;
        test_card_2 = null;
    }

    public void testGetCardName(){
        assertEquals("PlayingCard getCardName not returning correct value", "A", test_card_1.getCardName());
        assertEquals("PlayingCard getCardName not returning correct value", "5", test_card_2.getCardName());
    }

    public void testGetCardSuit(){
        assertEquals("PlayingCard getCardSuit not returning correct value", "\u2665", test_card_1.getCardSuit());
        assertEquals("PlayingCard getCardSuit not returning correct value", "\u2660", test_card_2.getCardSuit());
    }

    public void testGetFaceValue(){
        assertEquals("PlayingCard getFaceValue not returning correct value", 1, test_card_1.getFaceValue());
        assertEquals("PlayingCard getFaceValue not returning correct value", 5, test_card_2.getFaceValue());
    }

    public void testGetGameValue(){
        assertEquals("PlayingCard getGameValue not returning correct value", 14, test_card_1.getGameValue());
        assertEquals("PlayingCard getGameValue not returning correct value", 5, test_card_2.getGameValue());
    }

    public void testToString(){
        assertEquals("PlayingCard toString not giving correct string", "A\u2665", test_card_1.toString());
        assertEquals("PlayingCard toString not giving correct string", "5\u2660", test_card_2.toString());
    }
}
