package poker;


//
public class PlayingCard {
    static public final char HEARTS = 'H';
    static public final char DIAMONDS = 'D';
    static public final char CLUBS = 'C';
    static public final char SPADES = 'S';

    private String card_name;
    private char card_suit;
    private int face_value;
    private int game_value;

    public PlayingCard(String name, char suit, int face, int value) {
        card_name = name;
        card_suit = suit;
        face_value = face;
        game_value = value;
    }

    //Public accessor for the Card Name
    public String getCardName() {
        return card_name;
    }

    //Public accessor for the Card Suit
    public char getCardSuit() {
        return card_suit;
    }

    //Public accessor for the Face Value
    public int getFaceValue() {
        return face_value;
    }

    //Public accessor for the Game Value
    public int getGameValue() {
        return game_value;
    }

    public String toString() {
        return card_name + card_suit;
    }

    //Test code builds and prints contents of a full deck to show the class can represent any member card in a 52 card deck.
    public static void main(String[] args) {
        int current_value;
        char[] suit_list = {PlayingCard.HEARTS, PlayingCard.DIAMONDS, PlayingCard.CLUBS, PlayingCard.SPADES};
        String[] name_list = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        PlayingCard[] card_list = new PlayingCard[suit_list.length*name_list.length];

        for(int i = 0; i < suit_list.length; i++){
            for(int j = 0; j < name_list.length; j++){
                if(name_list[j].equals("A")){
                    current_value = 14;
                }
                else{
                    current_value = j + 1;
                }

                PlayingCard current_card = new PlayingCard(name_list[j], suit_list[i], j + 1, current_value);
                card_list[(i*name_list.length)+j] = current_card;
            }
        }
        for(int i = 0; i < card_list.length; i++){
            System.out.println(card_list[i].toString());
        }
    }
}
