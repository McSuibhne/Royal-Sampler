package poker;

import java.io.*;
import java.util.*;

/**
 * Created by Andy on 30/03/2017.
 */

public class RoundOfPoker {
	
	private int pot;
	
	// Empty constructor (not sure what to include here)
	public RoundOfPoker(){
		
	}
	
	public int getPot(){
		return pot;
	}

	// Main function simulates a round of poker
	public static void main(String[] args) {
		DeckOfCards deck = new DeckOfCards();
		
		for (int shuffleCount = 0; shuffleCount < 2074; shuffleCount++) {
			deck.shuffle();
		}
		
		HandOfCards hand1 = new HandOfCards(deck);
		HandOfCards hand2 = new HandOfCards(deck);
		
		System.out.println("Hand 1: " + hand1.toString());
		System.out.println("Hand 2: " + hand2.toString());
	}
	
}
