package poker;

import java.util.ArrayList;
import java.util.Arrays;


public class HandOfCards {

    //Hand Score constants chosen at 1000000 marks, as no intra-cards-type
    // ranking function can increase a score by more than 10000000.
    public static final int STRAIGHT_FLUSH_VALUE = 8000000;
    public static final int FOUR_OF_A_KIND_VALUE = 7000000;
    public static final int FULL_HOUSE_VALUE = 6000000;
    public static final int FLUSH_VALUE = 5000000;
    public static final int STRAIGHT_VALUE = 4000000;
    public static final int THREE_OF_A_KIND_VALUE = 3000000;
    public static final int TWO_PAIR_VALUE = 2000000;
    public static final int ONE_PAIR_VALUE = 1000000;
    public static final int CARDS_IN_HAND = 5;
    public static final int SUITS_IN_DECK = 4;
    public static final int WEIGHTING_VALUE = 14; //Weighting value set at 14 as this is the highest possible card value
    public PlayingCard[] card_hand = new PlayingCard[CARDS_IN_HAND];
    public DeckOfCards deck;

    //Deck is initialized and 5 cards are dealt.
    public HandOfCards(DeckOfCards card_deck) {
        deck = card_deck;
        for(int i = 0; i < CARDS_IN_HAND; i++) {
            card_hand[i] = deck.dealNext();
        }
        sort();
    }

    //Performs a Bubble Sort to quickly sort the cards.
    protected void sort(){
        for(int i = 0; i < CARDS_IN_HAND - 1; i++){
            for(int j = 0; j < CARDS_IN_HAND - 1; j++){
                if(card_hand[j].getGameValue() > card_hand[j + 1].getGameValue()){
                    PlayingCard temp_card = card_hand[j];
                    card_hand[j] = card_hand[j + 1];
                    card_hand[j + 1] = temp_card;
                }
            }
        }
    }

    //Tests for Royal Flush by checking that all suits match and card values are 10 J Q K A.
    public boolean isRoyalFlush(){
        return (card_hand[0].getCardSuit() == card_hand[1].getCardSuit() && card_hand[0].getCardSuit() == card_hand[2].getCardSuit()
                && card_hand[0].getCardSuit() == card_hand[3].getCardSuit() && card_hand[0].getCardSuit() == card_hand[4].getCardSuit()
                && card_hand[0].getGameValue() == 10 && card_hand[1].getGameValue() == 11 && card_hand[2].getGameValue() == 12
                && card_hand[3].getGameValue() == 13 && card_hand[4].getGameValue() == 14);
    }

    //Tests for Straight Flush by checking that all suits match and card values are in sequence. Also handles an ace-low
    // straight, and ensures that the method cannot return true for a royal flush.
    public boolean isStraightFlush(){
        return isStraight() && isFlush() && !isRoyalFlush();
    }

    //Tests for Four of a Kind by checking if there is a block of 4 identical cards after sorting.
    public boolean isFourOfAKind(){
        return (card_hand[0].getGameValue() == card_hand[3].getGameValue() || card_hand[1].getGameValue() == card_hand[4].getGameValue());
    }

    //Tests for Full House by checking if there is a triple where both spare cards match.
    public boolean isFullHouse(){
        return ((card_hand[0].getGameValue() == card_hand[2].getGameValue() && card_hand[3].getGameValue() == card_hand[4].getGameValue())
                || (card_hand[0].getGameValue() == card_hand[1].getGameValue() && card_hand[2].getGameValue() == card_hand[4].getGameValue()));
    }

    //Tests for Flush by checking if all cards' suits match while also ensuring that they do not form a straight.
    public boolean isFlush(){
        boolean is_flush = true;
        for(int i=0; i<CARDS_IN_HAND-1; i++) {
            if(card_hand[i].getCardSuit() != card_hand[i+1].getCardSuit()) {
                is_flush = false;
            }
        }
        return is_flush;
    }

    //Tests for Straight by checking if the cards' values form a sequence (and also handling an ace-low straight), while
    // also making sure that their suits do not all match.
    public boolean isStraight(){
        boolean is_straight = true;
        for(int i=0; i<CARDS_IN_HAND-2; i++) {
            if((card_hand[i].getGameValue() + 1) != card_hand[i + 1].getGameValue()) {
                is_straight = false;
            }
        }
        return is_straight && ((card_hand[3].getGameValue() + 1) == card_hand[4].getGameValue()
                || (card_hand[4].getGameValue() == 14 && card_hand[0].getGameValue() == 2));
    }

    //Tests for Three of a Kind by checking if there exists a block of 3 matching cards without a 4th match in the cards.
    // Also ensures that the 2 spare cards do not match each other.
    public boolean isThreeOfAKind(){
        return (((card_hand[0].getGameValue() == card_hand[2].getGameValue() && card_hand[2].getGameValue() != card_hand[3].getGameValue()
                && card_hand[3].getGameValue() != card_hand[4].getGameValue())
                || (card_hand[1].getGameValue() == card_hand[3].getGameValue() && card_hand[0].getGameValue() != card_hand[1].getGameValue()
                && card_hand[3].getGameValue() != card_hand[4].getGameValue())
                || (card_hand[2].getGameValue() == card_hand[4].getGameValue() && card_hand[0].getGameValue() != card_hand[1].getGameValue()
                && card_hand[1].getGameValue() != card_hand[2].getGameValue())));
    }

    //Tests for Two Pair by checking if there exists 2 matches between 2 different pairs cards that do not have the
    // same value, as well as one spare card that does not match either of the pairs.

    public boolean isTwoPair(){
        return ((card_hand[0].getGameValue() == card_hand[1].getGameValue() && card_hand[2].getGameValue() == card_hand[3].getGameValue()
                && card_hand[3].getGameValue() != card_hand[4].getGameValue() && card_hand[1].getGameValue() != card_hand[2].getGameValue())
                || (card_hand[0].getGameValue() == card_hand[1].getGameValue() && card_hand[3].getGameValue() == card_hand[4].getGameValue()
                && card_hand[1].getGameValue() != card_hand[2].getGameValue() && card_hand[2].getGameValue() != card_hand[3].getGameValue())
                || (card_hand[1].getGameValue() == card_hand[2].getGameValue() && card_hand[3].getGameValue() == card_hand[4].getGameValue()
                && card_hand[0].getGameValue() != card_hand[1].getGameValue() && card_hand[2].getGameValue() != card_hand[3].getGameValue()));
    }

    //Tests for One Pair by checking if there exists a match between 2 cards and no other matches in the cards.
    public boolean isOnePair(){
        return (((card_hand[0].getGameValue() == card_hand[1].getGameValue() && !(card_hand[1].getGameValue() == card_hand[2].getGameValue()
                || card_hand[2].getGameValue() == card_hand[3].getGameValue() || card_hand[3].getGameValue() == card_hand[4].getGameValue()))
                || (card_hand[1].getGameValue() == card_hand[2].getGameValue() && !(card_hand[0].getGameValue() == card_hand[1].getGameValue()
                || card_hand[2].getGameValue() == card_hand[3].getGameValue() || card_hand[3].getGameValue() == card_hand[4].getGameValue()))
                || (card_hand[2].getGameValue() == card_hand[3].getGameValue() && !(card_hand[0].getGameValue() == card_hand[1].getGameValue()
                || card_hand[1].getGameValue() == card_hand[2].getGameValue() || card_hand[3].getGameValue() == card_hand[4].getGameValue()))
                || (card_hand[3].getGameValue() == card_hand[4].getGameValue() && !(card_hand[0].getGameValue() == card_hand[1].getGameValue()
                || card_hand[1].getGameValue() == card_hand[2].getGameValue() || card_hand[2].getGameValue() == card_hand[3].getGameValue())))
        );
    }

    //Returns true for High Hand if all other tests fail.
    public boolean isHighHand(){
        return !(isRoyalFlush() ||  isStraightFlush() || isFourOfAKind() ||  isFullHouse() ||  isFlush()
                ||  isStraight() || isThreeOfAKind() ||  isTwoPair() ||  isOnePair());
    }

    //All hands in this conditional use the same formula for scoring intra-cards ties, so the only difference is
    // the constant that is added. Straight flush and royal flush use the same constant as a royal flush will be
    // scored higher than any possible straight flush.
    public int getGameValue(){
        int total_hand_score = 0;

        if(isHighHand()){
            total_hand_score += getHighCardScore();
        }

        if(isOnePair()){
            total_hand_score += ONE_PAIR_VALUE;
            total_hand_score += getOnePairScore();
        }

        if(isTwoPair()){
            total_hand_score += TWO_PAIR_VALUE;
            total_hand_score += getTwoPairScore();
        }

        if(isThreeOfAKind()){
            total_hand_score += THREE_OF_A_KIND_VALUE + card_hand[2].getGameValue();
        }

        if(isStraight() && !isFlush()){
            total_hand_score += STRAIGHT_VALUE;
            total_hand_score += getHighCardScore();
        }

        if(isFlush() && !isStraight()){
            total_hand_score += FLUSH_VALUE;
            total_hand_score += getHighCardScore();
        }

        if(isFullHouse()){
            total_hand_score += FULL_HOUSE_VALUE + card_hand[2].getGameValue();
        }

        if(isFourOfAKind()){
            total_hand_score += FOUR_OF_A_KIND_VALUE + card_hand[2].getGameValue();
        }

        if(isStraightFlush() || isRoyalFlush()) {
            total_hand_score += STRAIGHT_FLUSH_VALUE;
            total_hand_score += getHighCardScore();
        }

        return total_hand_score;
    }

    //Sums the values of all cards after weighting them according to rank in the cards, the same ordering that any
    // tiebreaker will follow. Returns this sum as the cards's score.
    private int getHighCardScore(){
        int high_card_score = 0;

        high_card_score += card_hand[0].getGameValue() +  card_hand[1].getGameValue()*WEIGHTING_VALUE
                + card_hand[2].getGameValue()*WEIGHTING_VALUE*WEIGHTING_VALUE
                + card_hand[3].getGameValue()*WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE
                + card_hand[4].getGameValue()*WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE;
        return high_card_score;
    }

    //getOnePairScore method first identifies the paired cards as they should receive the most significant weight regardless
    // of their values.
    // It then weights the remaining cards in order of value before returning their sum as the cards score.
    private int getOnePairScore(){
        int one_pair_score = 0;

        if(card_hand[0].getGameValue() == card_hand[1].getGameValue()){
            one_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[0].getGameValue()
                    + WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[4].getGameValue()
                    + WEIGHTING_VALUE*card_hand[3].getGameValue()
                    + card_hand[2].getGameValue();
        }
        else if(card_hand[1].getGameValue() == card_hand[2].getGameValue()){
            one_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[1].getGameValue()
                    + WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[4].getGameValue()
                    + WEIGHTING_VALUE*card_hand[3].getGameValue()
                    + card_hand[0].getGameValue();
        }
        else if(card_hand[2].getGameValue() == card_hand[3].getGameValue()){
            one_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[2].getGameValue()
                    + WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[4].getGameValue()
                    + card_hand[0].getGameValue();
        }
        else{
            one_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[3].getGameValue()
                    + WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[2].getGameValue()
                    + WEIGHTING_VALUE*card_hand[1].getGameValue()
                    + card_hand[0].getGameValue();
        }

        return one_pair_score;
    }

    private int getTwoPairScore(){
        int two_pair_score = 0;

        if(card_hand[0].getGameValue() == card_hand[1].getGameValue() && card_hand[2].getGameValue() == card_hand[3].getGameValue()){
            two_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[2].getGameValue()
                    + WEIGHTING_VALUE*card_hand[0].getGameValue()
                    + card_hand[4].getGameValue();
        }
        else if(card_hand[0].getGameValue() == card_hand[1].getGameValue() && card_hand[3].getGameValue() == card_hand[4].getGameValue()){
            two_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[3].getGameValue()
                    + WEIGHTING_VALUE*card_hand[0].getGameValue()
                    + card_hand[2].getGameValue();
        }
        else{
            two_pair_score += WEIGHTING_VALUE*WEIGHTING_VALUE*card_hand[3].getGameValue()
                    + WEIGHTING_VALUE*card_hand[1].getGameValue() + card_hand[0].getGameValue();
        }

        return two_pair_score;
    }

    /*Explanation Of Discard Probability Approach:
        This method returns the probability that discarding a given card will *not disimprove* the cards significantly.

        While the link given in the assignment slides is very very useful for understanding the probabilities of
        improving a cards, this on its own is not helpful to a player.
        For example, if a player has a 3-of-a-kind, there is an 11.5% chance of improving the cards by trading the two odd cards.

        However, since the player has very little to lose by trading the two odd cards, that 11.5% is not a good indicator
        of whether he should swap them.
        Instead, this method returns the inverse of the odds of worsening the cards by swapping a card.

        eg. Say the player has a straight with 4 of the cards being of the same suit. While they could possibly end up
        with a better cards (a flush or straight flush) by trading the odd-suited card, they also run the risk of ruining
        the straight they have and being left with junk.
        As ~80% of the possible cards would make this cards worse, the method would return ~20% discard probability in this case

        All formulae are explained in comments, as well as the private helper methods it makes use of.
    */
    public int[][] getDiscardProbability(){
        int[][] probabilities = new int[][]{{0, 0, 0, 0, 0},{0, 0, 0, 0, 0},{0, 0, 0, 0, 0}};
        for(int cardPosition=0; cardPosition<CARDS_IN_HAND; cardPosition++) {
            //Calls helper method getHighCardProbability to return discard probability for the given card.
            if(isHighHand()) {
                probabilities[2][cardPosition] += getHighCardProbability(cardPosition);
            }

            //Calls helper method getOnePairProbability to return discard probability for the given card.
            if(isOnePair()) {
                int[] pair_discards = getOnePairProbability(cardPosition);
                probabilities[0][cardPosition] += pair_discards[0];
                probabilities[1][cardPosition] += pair_discards[1];
                probabilities[2][cardPosition] += pair_discards[2];
            }

            //Calls helper method getTwoPairProbability to return discard probability for the given card.
            if(isTwoPair()) {
                probabilities[2][cardPosition] += getTwoPairProbability(cardPosition);
            }

            //If the card is not part of the triple and is the highest ranked card in the cards, there's a slight chance of worsening t
            // the cards if the returned cards do not match any others and are both lower in value than the discarded card.
            // However, this should usually return a high discard probability.
            if(isThreeOfAKind()) {
                if(card_hand[cardPosition].getGameValue() != card_hand[2].getGameValue()) { //The card in the middle position will always be part of the triple
                    if(cardPosition == CARDS_IN_HAND - 1) {
                        probabilities[2][cardPosition] += (int) ((1 - ((card_hand[cardPosition].getGameValue() - 1)
                                / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND))) * 100);
                    }
                    else {
                        probabilities[2][cardPosition] += 100;     //Should definitely trade at least one card
                    }
                }
            }
            //There's a small chance of improving a straight to a flush if it is currently a broken flush. However, there
            // are many cards that could ruin the straight so this will usually return a low discard probability.
            if(isStraight() && !isFlush()) {
                int potential_flushes = isBustedFlush(cardPosition);
                if(potential_flushes > 0) {
                    probabilities[2][cardPosition] += (int) (((potential_flushes) / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND)) * 100);
                }
            }

            //There's a VERY small chance of improving a flush to a straight flush if it is currently a broken straight. However, there
            // are many cards that could ruin the flush so this will usually return a low discard probability.
            if(isFlush() && !isStraight()) {
                int potential_straights = isBustedStraight(cardPosition);
                if(potential_straights > 0) {
                    probabilities[2][cardPosition] += (int) (((potential_straights / SUITS_IN_DECK)
                            / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND)) * 100);
                }
            }

            //There's a small chance of improving a full house to a four of a kind. However, there are many cards
            // that could ruin the full house so this will usually return an extremely low discard probability.
            if(isFullHouse()) {
                if(card_hand[cardPosition].getGameValue() != card_hand[2].getGameValue()) {
                    probabilities[2][cardPosition] += (int) (1 / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND) * 100);
                }
            }

            //No two four of a kinds are equal so the spare card is completely useless. Because of this, it should always
            // be traded as not trading any cards can often scare away other players from betting.
            if(isFourOfAKind()) {
                if(card_hand[cardPosition].getGameValue() != card_hand[2].getGameValue()) {
                    probabilities[2][cardPosition] += 100;
                }
            }

            //There is no way to improve a Royal Flush, all discard probabilities will be 0.
        }
        return probabilities;
    }

    //In a high card cards, the method checks if the cards resembles a broken flush or straight, and trades the odd card
    // if so. Otherwise it assigns high discard probabilities to the worst 3 cards in the cards, slightly weighting the
    // scores by how strong the cards' values are.
    private int getHighCardProbability(int cardPosition){
        int probability = 0;
        boolean busted_flush = false;
        boolean busted_straight = false;
        for(int i=0; i<CARDS_IN_HAND; i++){
            if(isBustedFlush(i) > 0) {
                busted_flush = true;
                break;
            }
        }
        if(!busted_flush){
            for(int i=0; i<CARDS_IN_HAND; i++){
                if(isBustedStraight(i) > 0) {
                    busted_straight = true;
                    break;
                }
            }
        }
        if(busted_flush) {
            if(isBustedFlush(cardPosition) > 0) {
                probability += 100;
            }
        }
        else if(busted_straight){
            if(isBustedStraight(cardPosition) > 0) {
                boolean straight_buster_found = false;
                for(int i = 0; i < cardPosition; i++) {
                    if(isBustedStraight(i) > 0) {
                        straight_buster_found = true;
                    }
                }
                if(!straight_buster_found) {
                    probability += 100;
                }
            }
        }
        else {
            if(cardPosition < 3) {
                probability += (int) ((1 - ((card_hand[cardPosition].getGameValue() - 1) / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND))) * 100);
            }
        }

        return probability;
    }

    //As the chances of improving one pair cards to straight or flush are low, this will return a high discard
    //probability for the kicker cards only, which will be slightly weighted by how strong the cards are.
    private int[] getOnePairProbability(int cardPosition){
        int[] pair_probabilities = new int[3];
        boolean is_in_pair = false;
        int potential_flushes = isBustedFlush(cardPosition);
        int potential_straights = isBustedStraight(cardPosition);
        for(int i = 0; i < CARDS_IN_HAND; i++) {
            if(card_hand[cardPosition].getGameValue() == card_hand[i].getGameValue() && cardPosition != i) {
                is_in_pair = true;
            }
        }
        if(potential_flushes > 0) {
            if(is_in_pair && !card_hand[cardPosition].getCardSuit().equals(card_hand[(cardPosition+2)%CARDS_IN_HAND].getCardSuit())){
                pair_probabilities[0] += (int) ((1 - (((DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND) - ((((CARDS_IN_HAND - 1)*(SUITS_IN_DECK - 1)) - 1)
                        + potential_flushes)) /(double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND)))*100);
            }
        }
        else if(potential_straights > 0){
            if(is_in_pair && cardPosition%2 == 0){      //Will only swap one of the two matching cards
                pair_probabilities[1] += (int) ((1 - (((DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND) - ((((CARDS_IN_HAND - 1)*(SUITS_IN_DECK - 1)) - 1)
                        + potential_straights)) /(double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND)))*100);
            }
        }
        else {
            if(!is_in_pair) {
                pair_probabilities[2] += (int) ((1 - ((card_hand[cardPosition].getGameValue() - 1) / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND))) * 100);
            }
        }
        return pair_probabilities;
    }

    //A two pair cannot be easily improved by discarding cards in either of the pairs. This will return a high discard
    // probability for the kicker card only, which will be slightly weighted by how strong the card is.
    private int getTwoPairProbability(int cardPosition) {
        int probability = 0;
        boolean is_kicker = true;
        for(int i=0; i<CARDS_IN_HAND; i++){
            if(card_hand[cardPosition].getGameValue() == card_hand[i].getGameValue() && i != cardPosition){
                is_kicker = false;
                break;
            }
        }
        if(is_kicker){
            probability += (int) ((1 - ((card_hand[cardPosition].getGameValue() - 1) / (double) (DeckOfCards.CARDS_IN_DECK - CARDS_IN_HAND))) * 100);
        }

        return probability;
    }

    //Creates a temporary deck and deals all 52 possible cards from it. For each new possible card that is not already
    // in the cards, it replaces the card at the position currently being evaluated and sorts the cards before checking to
    // see if this creates a straight.
    // If so, it increases the returned number of possible straights by 1.
    // After each iteration the cards is changed back to its' original state so the next change will occur in the correct place.
    // Originally this method returned a boolean but this made extra code necessary to calculate how likely it was that
    // a straight could be made. Now it performs both tasks by simply returning the number of possible straights that can be
    // made by trading the card in question, and a return value of 0 is interpreted as equivalent to false.
    private int isBustedStraight(int card_position) {
        DeckOfCards comparison_deck = new DeckOfCards();
        PlayingCard[] original_hand = card_hand.clone();
        PlayingCard original_card = card_hand[card_position];
        PlayingCard next_card = comparison_deck.dealNext();
        int potential_straights = 0;
        while(next_card != null) {
            boolean duplicate_card = false;
            for(int i = 0; i < CARDS_IN_HAND; i++) {
                if(next_card.getCardName().equals(original_card.getCardName()) || next_card.getCardName().equals(card_hand[i].getCardName())) {
                    duplicate_card = true;
                }
            }
            if(!duplicate_card) {
                card_hand[card_position] = next_card;
                sort();
                if(isStraight()) {
                    potential_straights++;
                }
            }
            next_card = comparison_deck.dealNext();
            card_hand = original_hand.clone();
        }
        return potential_straights;
    }

    //Creates a temporary deck and deals all 52 possible cards from it. For each new possible card that is not already
    // in the cards, it replaces the card at the position currently being evaluated and checks to see if this creates a
    // flush.
    // If so, it increases the returned number of possible flushes by 1.
    // Originally this method returned a boolean but this made extra code necessary to calculate how likely it was that
    // a flush could be made. Now it performs both tasks by simply returning the number of possible flushes that can be
    // made by trading the card in question, and a return value of 0 is interpreted as equivalent to false.
    private int isBustedFlush(int card_position) {
        DeckOfCards comparison_deck = new DeckOfCards();
        PlayingCard original_card = card_hand[card_position];
        PlayingCard next_card = comparison_deck.dealNext();
        int potential_flushes = 0;
        while(next_card != null) {
            boolean duplicate_card = false;
            for(int i = 0; i < CARDS_IN_HAND; i++) {
                if(next_card.getCardName().equals(original_card.getCardName()) || next_card.getCardName().equals(card_hand[i].getCardName())) {
                    duplicate_card = true;
                }
            }
            if(!duplicate_card) {
                card_hand[card_position] = next_card;
                if(isFlush()) {
                    potential_flushes++;
                }
            }
            next_card = comparison_deck.dealNext();
        }
        card_hand[card_position] = original_card;
        return potential_flushes;
    }

    public boolean isDrawingHand(){
        boolean drawing_hand = false;
        for(int i=0; i<CARDS_IN_HAND; i++){
            if(isBustedStraight(i) > 0 || isBustedFlush(i) > 0){
                drawing_hand = true;
            }
        }
        return drawing_hand;
    }

    //Small method that should be called from the PokerPlayer class, necessitating the protected access modifier.
    // The card at the given index of the deck is returned to the deck and replaced by the next one dealt from it.
    // As both methods in deck are thread-safe, this should not create any race conditions even if two players try to
    // return cards at the same time.
    protected void discardCard(int card_position){
        deck.returnCard(card_hand[card_position]);
        card_hand[card_position] = deck.dealNext();
    }

    //Simple toString method to make cards more readable
    public String toString(){
        String hand_string = "[";
        for(int i = 0; i < card_hand.length; i++){
            hand_string += card_hand[i].toString();
            if(i==card_hand.length-1){
                hand_string += "]";
            }
            else{
                hand_string += ", ";
            }
        }
        return hand_string;
    }
/*
    // Added by Gavin for Picture functionality
    public PlayingCard[] get() {
        return card_hand;
    }
*/
    //***FOR TESTING ONLY***
    //No setter is needed for final program, method just allows for card values to be set to test
    // unlikely hands such as Royal Flush etc. Will be removed later to ensure encapsulation.
    public void testSetter(ArrayList<PlayingCard> new_hand){
        for(int i=0; i<CARDS_IN_HAND; i++) {
            card_hand[i] = new_hand.get(i);
        }
        sort();

    }

    //Main initializes a new deck and cards, generating a random cards of cards as per the class's normal function.
    // It then creates some specific cards and assembles them into known poker hands, before placing these combinations
    // in a list of tests.
    // Main then iterates through this test list, outputting the values of all card cards types, values, and discard
    // probability methods to show that each is working as specified. Once this code is not needed, the test setter
    // can be removed.
    // Test list implemented using array lists to allow for growing list of tests as well as both title and cards members.
    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();
        HandOfCards hand = new HandOfCards(deck);
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

        //Test hands
        test_list.add(new ArrayList<>(Arrays.asList("Royal Flush:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_JH, test_KH, test_QH, test_AH)));   //Royal Flush
        test_list.add(new ArrayList<>(Arrays.asList("Straight Flush (Ace Low):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_AH, test_3H, test_4H, test_2H, test_5H)));    //Straight Flush (Ace Low)
        test_list.add(new ArrayList<>(Arrays.asList("Four of a Kind:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_JH, test_10S, test_10C, test_10D)));//Four of a Kind
        test_list.add(new ArrayList<>(Arrays.asList("Full House:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_10C, test_9C, test_9H, test_10S))); //Full House
        test_list.add(new ArrayList<>(Arrays.asList("Flush:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_5H, test_9H, test_KH, test_3H, test_AH)));    //Flush
        test_list.add(new ArrayList<>(Arrays.asList("Straight (Ace High):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_10D, test_AS, test_KH, test_QH, test_JH)));   //Straight (Ace High)
        test_list.add(new ArrayList<>(Arrays.asList("Straight (Ace Low):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_AS, test_3H, test_4H, test_2D, test_5H)));    //Straight (Ace Low)
        test_list.add(new ArrayList<>(Arrays.asList("Three of a Kind:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_10C, test_KH, test_QH, test_10S))); //Three of a Kind
        test_list.add(new ArrayList<>(Arrays.asList("Two Pair:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_10H, test_9H, test_KH, test_10S, test_9C)));  //Two Pair
        test_list.add(new ArrayList<>(Arrays.asList("One Pair:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_9H, test_7C, test_QH, test_9C)));    //One Pair
        test_list.add(new ArrayList<>(Arrays.asList("High Hand:")));
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_9H, test_7C, test_QH, test_AS)));    //High Hand
        test_list.add(new ArrayList<>(Arrays.asList("HighHand (Ace High):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_3H, test_6H, test_AS, test_7C)));    //HighHand
        test_list.add(new ArrayList<>(Arrays.asList("HighHand (King High):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_JH, test_8C, test_KH, test_7C, test_QH)));   //High Hand
        test_list.add(new ArrayList<>(Arrays.asList("Flush (Busted Straight):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_2H, test_3H, test_5H, test_6H, test_9H)));    //Flush (Busted Straight)
        test_list.add(new ArrayList<>(Arrays.asList("Straight (Busted Flush):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_2D, test_3H, test_4H, test_6H, test_5H)));    //Straight (Busted Flush)
        test_list.add(new ArrayList<>(Arrays.asList("HighHand (Busted Flush):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_KH, test_3H, test_2H, test_6S, test_9H)));    //High Hand (Busted Flush)
        test_list.add(new ArrayList<>(Arrays.asList("HighHand (Busted Straight):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_2C, test_3H, test_7C, test_6S, test_5H)));    //High Hand (Busted Straight)
        test_list.add(new ArrayList<>(Arrays.asList("HighHand (No Busted Flush or Straight):")));
        test_list.add(new ArrayList<>(Arrays.asList(test_JH, test_8C, test_KH, test_10S, test_2H)));   //High Hand

        //Test loop shows all cards classification and cards scoring code works properly and has no overlap between hands,
        // while scoring similar hands according to brief.
        // It then outputs the discard probabilities for each card.
        for(int i=0; i<test_list.size()+1; i++) {
            if(i<test_list.size()) {
                System.out.println("\n" + test_list.get(i).get(0));
                i++;

                hand.testSetter(test_list.get(i));
                System.out.println(hand.toString());

                System.out.println("GameValue: " + hand.getGameValue());
                System.out.println("isRoyalFlush: " + hand.isRoyalFlush());
                System.out.println("isStraightFlush: " + hand.isStraightFlush());
                System.out.println("isFourOfAKind: " + hand.isFourOfAKind());
                System.out.println("isFullHouse: " + hand.isFullHouse());
                System.out.println("isFlush: " + (hand.isFlush() && !hand.isStraight()));
                System.out.println("isStraight: " + (hand.isStraight() && !hand.isFlush()));
                System.out.println("isThreeOfAKind: " + hand.isThreeOfAKind());
                System.out.println("isTwoPair: " + hand.isTwoPair());
                System.out.println("isOnePair: " + hand.isOnePair());
                System.out.println("isHighHand: " + hand.isHighHand());

                for(int j=0; j<CARDS_IN_HAND; j++){
                    System.out.println("Card " + j + ": " + hand.getDiscardProbability());
                }
            }
        }
    }
}
