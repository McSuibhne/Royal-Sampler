package poker;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

/**
 * Test class for DeckOfCards.
 * setUp() creates two DeckOfCards objects.
 * The @Before annotation is required to force setUp() to be executed before the tests themselves, ensuring both test
 * deck objects are already made and ready to have each of their returning methods called.
 * This verifies that the dealNext() method is working correctly.
 */
public class DeckOfCardsTest extends TestCase{

    private DeckOfCards test_deck_1;
    private DeckOfCards test_deck_2;

    @Before
    public void setUp(){
    	test_deck_1 = new DeckOfCards();
    	test_deck_2 = new DeckOfCards();
    }

    @After
    public void tearDown() {
        test_deck_1 = null;
        test_deck_2 = null;
    }
    
    public void testDealNext(){
    	for(int i = 0; i<53; i++){
    		test_deck_1.dealNext();
    		test_deck_2.dealNext();
    	}
    	assertEquals("DeckOfCards dealNext is returning a null variable.", null, test_deck_1.dealNext());
    	assertEquals("DeckOfCards dealNext is returning a null variable.", null, test_deck_2.dealNext());
    }

}
