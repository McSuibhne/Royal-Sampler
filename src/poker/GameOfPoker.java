package poker;

import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Gavin on 03/04/2017.
 */
public class GameOfPoker extends Thread{
    public static final int NUMBER_OF_BOTS = 4;
    public static final int DISCARD_MINIMUM_RANGE = 15;
    public static final int DISCARD_MAXIMUM = 90;
    public static final int HUMAN_INDEX = 0;
    TwitterInterface twitter;
    public String tname;
    public long tid;
    DeckOfCards deck;
    ArrayList<PokerPlayer> player_list;

    public GameOfPoker(TwitterInterface twitterInterface, String name, long id) {
        twitter = twitterInterface;
        tname = name;
        tid = id;
    }

    public void run() {
        createPlayers();
        try{
            playGame();
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    // Will wait for notice from twitter bot that a player has requested a game
    // Currently just called to start game from command line
    public void createPlayers() {
        Random rand = new Random();
        player_list = new ArrayList<>();
        deck = new DeckOfCards();
        player_list.add(new HumanPlayer(twitter, tname, tid));
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+(DISCARD_MAXIMUM-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum));
            System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
        }
    }

    public void playGame() throws TwitterException {
        HumanPlayer humanPlayer = (HumanPlayer) player_list.get(GameOfPoker.HUMAN_INDEX);
        Boolean game_over = false;
        String win_message = "";


        twitter.postMessagetoUser(humanPlayer.name +" You have tweeted the RoyalSampler hashtag to play Poker\nWelcome to 5 card draw Poker!", player_list.get(HUMAN_INDEX));
        String opponent_names = "";
        for(int i=HUMAN_INDEX + 1; i<player_list.size(); i++){
            opponent_names += player_list.get(i).getName() +"\n";
        }
        twitter.postMessagetoUser("Your opponents are:\n"+ opponent_names, player_list.get(HUMAN_INDEX));

        while(!game_over){
            deck.reset();
            RoundOfPoker round = new RoundOfPoker(player_list, deck,twitter);
            round.playRound();

            String tweet_message = "";
            for(int i=0; i<player_list.size(); i++){
                if(player_list.get(i).chips < RoundOfPoker.ANTE) {
                    tweet_message += player_list.get(i).getName() + " has run out of chips and is eliminated\n";
                    if(i == HUMAN_INDEX) {
                        win_message = "You have been eliminated from the game.\nThanks for playing, better luck next time!";
                        game_over = true;
                        break;
                    }
                    else {
                        player_list.remove(i);
                        i--;
                    }
                }

            }

            if(!tweet_message.equals("")){
                twitter.postMessagetoUser(tweet_message, humanPlayer);
            }

            if(player_list.size()==1){
                game_over = true;
                win_message = "You have won. Congratulations!\nThank you for playing";
            }
        }
        twitter.postMessagetoUser(win_message, humanPlayer);
    }

    public void quitMessage(){
        twitter.postMessagetoUser("You have tweeted the hashtag to end the game\nThank You for playing!", player_list.get(GameOfPoker.HUMAN_INDEX));
    }



}
