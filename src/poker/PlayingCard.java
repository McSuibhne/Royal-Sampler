package poker;

public class PlayingCard {
    static final String HEARTS = "\u2665";
    static final String DIAMONDS = "\u2666";
    static final String CLUBS = "\u2663";
    static final String SPADES = "\u2660";

    private String card_name;
    private String card_suit;
    private int face_value;
    private int game_value;

    /**Playing card object. Holds name, suit (in unicode), face value and game value. Will be held either in a deck or a hand.*/
    PlayingCard(String name, String suit, int face, int value) {
        card_name = name;
        card_suit = suit;
        face_value = face;
        game_value = value;
    }

    /**Public accessor for the Card Name*/
    String getCardName() {
        return card_name;
    }

    /**Public accessor for the Card Suit*/
    String getCardSuit() {
        return card_suit;
    }

    /**Public accessor for the Face Value*/
    int getFaceValue() {
        return face_value;
    }

    /**Public accessor for the Game Value*/
    int getGameValue() {
        return game_value;
    }

    /**PlayingCard toString (Card face + suit unicode)*/
    public String toString() {
        return card_name + card_suit;
    }
}
