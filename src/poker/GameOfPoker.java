package poker;

import twitter4j.TwitterException;
import twitter4j.User;

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
    public User user;
    public long base_status_id;
    boolean game_ended = false;
    DeckOfCards deck;
    ArrayList<PokerPlayer> player_list;
    HumanPlayer human_player;

    public GameOfPoker(TwitterInterface twitter_interface, User twitter_user, long id) {
        twitter = twitter_interface;
        user = twitter_user;
        base_status_id = id;
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
        human_player = new HumanPlayer(twitter, user, base_status_id);
        player_list.add(human_player);
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+(DISCARD_MAXIMUM-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum));
            //System.out.println(player_list.get(i).name +", "+ discard_minimum);       //**For testing**
        }
    }

    public HumanPlayer getHumanPlayer(){
        return (HumanPlayer) player_list.get(HUMAN_INDEX);
    }

    public void playGame() throws TwitterException {
        Boolean game_over = false;
        String win_message = "";
        twitter.postMessageToUser("You have tweeted the RoyalSampler hashtag to play Poker\nWelcome to 5 card draw Poker!", human_player);
        String opponent_names = "";
        for(int i=HUMAN_INDEX + 1; i<player_list.size(); i++){
            opponent_names += player_list.get(i).getName() +"\n";
        }
        twitter.postMessageToUser("Your opponents are:\n"+ opponent_names, human_player);

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
                    if(!tweet_message.equals("") && tweet_message.length() > 90) {
                        twitter.postMessageToUser(tweet_message, human_player);
                        tweet_message = "";
                    }
                }
            }

            if(!tweet_message.equals("")){
                twitter.postMessageToUser(tweet_message, human_player);
            }

            if(player_list.size()==1){
                game_over = true;
                win_message = "You have won. Congratulations!\nThank you for playing";
                game_ended = true;
            }
        }
        twitter.postMessageToUser(win_message, human_player);
        twitter.endGame(human_player);
    }

    public void quitMessage(){
        twitter.postMessageToUser("You have tweeted the hashtag to end the game\nThank You for playing!", human_player);
    }

    public void interruptMessage(){
        twitter.postMessageToUser("You have tweeted the hashtag to start an new game, ending the one currently " +
                "in progress\nThank You for playing!", human_player);
    }

}
