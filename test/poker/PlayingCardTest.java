package poker;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

//Notes for Andy: (remove before submission)
// This class is a really simple example, the others will end up being more difficult since you'll need to make more
//  objects in the setUp. Eg: You'll need a Deck object first to pass into HandOfCards when you're doing that one.
//
//Just do the first few classes! Stop when you get to Game/Round/maybe even Players, basically anything that needs
// Twitter stuff as arguments. That stuff is a nightmare to test in a vacuum and might end up doing something weird.
//
//tearDown() is meant to remove all the objects you made in setUp, it's probably completely optional. Just make sure you
// stick an @After at the start so it doesn't delete all the objects before you get a chance to use them!
//
//There's a handy example template of a test class I used to make this one here: http://junit.sourceforge.net/doc/faq/faq.htm#tests_16
// (Halfway down the page if it doesn't link right) There might be other helpful stuff in that faq but don't feel you
//  have to read it because it's really long so I didn't.
//
//Think that's all, don't worry about making tests for methods that don't return anything. If anything's giving trouble,
// leave it. We just need enough to go "We did tests, ta-da!"
//
//Btw, you should be able to right-click the test folder and hit "run all tests" which is a handy way of seeing what
// you've got so far. :)

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
