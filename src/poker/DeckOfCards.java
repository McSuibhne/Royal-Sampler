package poker;

import java.util.Random;

public class DeckOfCards {

    public static final int CARDS_IN_DECK = 52;
    private boolean deck_constructed = false;
    private int next_card_position;
    private int number_of_discards = 0;
    private PlayingCard[] card_list = new PlayingCard[CARDS_IN_DECK];

    //As per brief, the public constructor does not hold all the deck building logic. The constructor calls the private
    // buildDeck() method only if the deck has not yet been built.
    public DeckOfCards(){
        if(!deck_constructed){
            deck_constructed = true;
            buildDeck();
        }
    }

    //Constructs the sorted deck of 52 playing cards, then calls shuffle() to shuffle and initialize
    private void buildDeck(){
        int current_value;
        char[] suit_list = {PlayingCard.HEARTS, PlayingCard.DIAMONDS, PlayingCard.CLUBS, PlayingCard.SPADES};
        String[] name_list = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for(int i = 0; i < suit_list.length; i++){
            for(int j = 0; j < name_list.length; j++){
                if(name_list[j].equals("A")){
                    current_value = 14;
                }
                else{
                    current_value = j + 1;
                }

                PlayingCard current_card = new PlayingCard(name_list[j], suit_list[i], (j + 1), current_value);
                card_list[(i*name_list.length)+j] = current_card;
            }
        }
        shuffle();
    }

    //Shuffles the deck and resets counter at the beginning of each round.
    public void reset() {
        next_card_position = 0;
        number_of_discards = 0;
        buildDeck();
    }

    //Repeatedly swaps the positions of random cards 52^2 times.
    public void shuffle() {
        Random rand = new Random();
        for(int i = 0; i < card_list.length*card_list.length; i++) {
            int first_random_index = rand.nextInt(card_list.length);
            int second_random_index = rand.nextInt(card_list.length);

            PlayingCard temp_card = card_list[first_random_index];
            card_list[first_random_index] = card_list[second_random_index];
            card_list[second_random_index] = temp_card;
        }
    }

    //Deals the card at the current counter position then increments the counter so it is not dealt again. Once the end
    // of the deck is reached the counter wraps around to the front of the deck and deals the cards that have been
    // returned to the deck.
    // Once it runs out, it returns null and an error message.
    public synchronized PlayingCard dealNext() {
        PlayingCard next_card;
        if(next_card_position < card_list.length + number_of_discards) {
            next_card = card_list[next_card_position % card_list.length];
            next_card_position++;
        }
        else{
            next_card = null;
        }
        return next_card;
    }

    //Adds the returned card to the discarded list and increments the discards counter.
    public synchronized void returnCard(PlayingCard discarded_card) {
        if(discarded_card != null) {
            card_list[number_of_discards] = discarded_card;
            number_of_discards++;
        }
    }

    //Deck is initialized and all cards are dealt. Every 4th one is discarded and added to the front of the array, but
    // not dealt again until all 52 shuffled cards have been dealt. When there are no more un-dealt or returned cards,
    // a null card is returned with an error message.
    // The deck is then reset and all this is repeated to show that each deck is independent and random.
    public static void main(String[] args) {
        DeckOfCards deck = new DeckOfCards();
        PlayingCard last_card;
        for(int j = 0; j < 2; j++){
            for(int i = 0; i < 66; i++) {
                last_card = deck.dealNext();
                if(last_card != null) {
                    System.out.println((i + 1) + " " + last_card.toString());
                    if(i % 4 == 0&&i<DeckOfCards.CARDS_IN_DECK) {
                        deck.returnCard(last_card);
                    }
                }
                else{
                    System.out.println("All cards in deck have been dealt");
                }
            }
            deck.reset();
        }
    }
}
