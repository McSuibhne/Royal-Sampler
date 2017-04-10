package poker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Gavin on 03/04/2017.
 */
public class GameOfPoker {
    public static final String NAMES_FILE = "src/poker/AINames.txt";
    public static final int NAMES_FILE_LENGTH = 60;
    public static final int NUMBER_OF_BOTS = 3;
    public static final int DISCARD_MINIMUM_RANGE = 20;
    public static final int HUMAN_INDEX = 0;

    public GameOfPoker(){
        Random rand = new Random();
        ArrayList<PokerPlayer> player_list = new ArrayList<>();
        DeckOfCards deck = new DeckOfCards();
        player_list.add(new HumanPlayer(deck));
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+((100)-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(getAiName(i), discard_minimum, deck));
            //System.out.println(player_list.get(i).name +", "+ discard_minimum);       **For testing**
        }

        playGame(player_list, deck);        //Might be better to call this in the outer, main class if we make one instead of in constructor?
    }                                       // If so, player_list and deck must become class variables and not be given to playGame as arguments.

    private String getAiName(int ai_number){
        String ai_name = null;
        try{
            Random rand = new Random();
            int line_number = rand.nextInt(NAMES_FILE_LENGTH/NUMBER_OF_BOTS) + ((ai_number-1)*(NAMES_FILE_LENGTH/NUMBER_OF_BOTS));
            BufferedReader reader = new BufferedReader(new FileReader(NAMES_FILE));
            String line;
            for(int i=0; i<=line_number; i++){
                line = reader.readLine();
                if(i==line_number){
                    ai_name = line;
                }
            }
            reader.close();
        }
        catch (Exception e){
            ai_name = "CPU Player " + (ai_number);
        }

        return ai_name;
    }

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    public void playGame(ArrayList<PokerPlayer> player_list, DeckOfCards deck) {
        Boolean game_over = false;
        Scanner scan = new Scanner(System.in);

        while(!game_over){
            RoundOfPoker round = new RoundOfPoker(player_list, deck);

            for(int i=0; i<player_list.size(); i++){
                if(player_list.get(i).chips < RoundOfPoker.ANTE){
                    System.out.println(player_list.get(i).name +"Has run out of chips and is eliminated");
                    if(i==HUMAN_INDEX){
                        game_over = true;
                    }
                    player_list.remove(i);
                }
            }

            if(player_list.size()==1){
                game_over = true;
                System.out.println(player_list.get(HUMAN_INDEX).name +" has won. Congratulations!");
            }

            if(!game_over && player_list.get(HUMAN_INDEX).chips < RoundOfPoker.ANTE) {
                boolean valid_input;
                do{
                    System.out.println("Do you want to keep playing Poker? Y/N");
                    String s = scan.next();
                    valid_input = true;

                    if(s.equals("N") || s.equals("n")) {
                        game_over = true;
                    }
                    else if(!s.equals("Y") && !s.equals("y")){
                        System.out.println("Please enter a valid input");
                        valid_input = false;
                    }
                }while(!valid_input);
            }
        }
        System.out.println("Thank you for playing");
    }


    // main method initialising twitter listener and waiting for game start request
    public static void main(String[] args) {
        GameOfPoker game = new GameOfPoker();
    }
}
