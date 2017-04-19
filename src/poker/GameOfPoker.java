package poker;

import twitter4j.TwitterException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Gavin on 03/04/2017.
 */
public class GameOfPoker {
    public static final int NUMBER_OF_BOTS = 4;
    public static final int DISCARD_MINIMUM_RANGE = 20;
    public static final int HUMAN_INDEX = 0;
    TwitterInterface twitter;
    public String tname;
    public long tid;
    ArrayList<PokerPlayer> player_list;

    public GameOfPoker(TwitterInterface twitterInterface,String name,long id){
        twitter = twitterInterface;
        tname=name;
        tid=id;

        //Might be better to call this in the outer, main class if we make one instead of in constructor?
    }                                       // If so, player_list and deck must become class variables and not be given to playGame as arguments.

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    public void createPlayer() throws TwitterException {
        Random rand = new Random();
        player_list = new ArrayList<>();
        DeckOfCards deck = new DeckOfCards();
        player_list.add(new HumanPlayer(deck,  twitter,tname, tid));
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+((100)-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum, deck));
            System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
        }
        playGame(player_list, deck);
    }
    public void playGame(ArrayList<PokerPlayer> player_list, DeckOfCards deck) throws TwitterException {
        HumanPlayer temp=(HumanPlayer) player_list.get(0);
        twitter.postreply(temp.getName() +" You have tweeted the hashtag to play Poker\nWelcome to RoyalSampler Poker ",temp);
        Boolean game_over = false;
        Scanner scan = new Scanner(System.in);

        while(!game_over){
            deck.reset();
            twitter.postreply(temp.getName()+" New Round:",temp);
            RoundOfPoker round = new RoundOfPoker(player_list, deck,twitter);

            for(int i=0; i<player_list.size(); i++){
                if(player_list.get(i).chips < RoundOfPoker.ANTE){
                    System.out.println(player_list.get(i).getName() +" has run out of chips and is eliminated");
                    if(i==HUMAN_INDEX){
                        game_over = true;
                    }
                    player_list.remove(i);
                    i--;
                }
            }

            if(player_list.size()==1){
                game_over = true;
                System.out.println(player_list.get(HUMAN_INDEX).getName() +" has won. Congratulations!");
            }

            if(!game_over && player_list.get(HUMAN_INDEX).chips > RoundOfPoker.ANTE) {
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
        twitter.postreply (temp.getName()+" You have tweetd the hashtag to end the game \n" + "Thank You for playing",temp);
    }
 //   public void stopGame(long i){
  //
  //  }



    // main method initialising twitter listener and waiting for game start request
    public static void main(String[] args) {
/*      *//*  TwitterInterface twitterInterface = null;
        try {
            twitterInterface = new TwitterInterface();
        } catch(IOException e) {
            e.printStackTrace();
        }
        GameOfPoker gameOfPoker = new GameOfPoker(twitterInterface);
       *//**//* gameOfPoker.start();
        boolean flag;
        do {
            flag = gameOfPoker.checkwanttopla*//*y();
            gameOfPoker.runRounds(flag);
        }
        while (flag != true);
        gameOfPoker.exit();*/
    }
}
