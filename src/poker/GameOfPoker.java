package poker;

import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Random;

/**
 * Game object, created and given its own new thread by the twitter interface each time #rsdealmein is tweeted.
 * Sets up and runs through a full game of poker, creating multiple player, deck, and round objects.*/
public class GameOfPoker extends Thread{
    static final int NUMBER_OF_BOTS = 4;
    static final int DISCARD_MINIMUM_RANGE = 15;
    static final int DISCARD_MAXIMUM = 90;
    static final int HUMAN_INDEX = 0;
    private TwitterInterface twitter;
    private User user;
    private long base_status_id;
    private boolean game_ended = false;
    private DeckOfCards deck;
    private ArrayList<PokerPlayer> player_list;
    private HumanPlayer human_player;

    /**GameOfPoker constructor*/
    GameOfPoker(TwitterInterface twitter_interface, User twitter_user, long id) {
        twitter = twitter_interface;
        user = twitter_user;
        base_status_id = id;
    }

    /**Called by the start() method in the Thread superclass. Necessary for multithreading.*/
    public void run() {
        createPlayers();
        try{
            playGame();
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /**Called by run() at the beginning of the thread operation.
     * Makes a human and a variable number of bots, placing them in the player_list ArrayList.*/
    private void createPlayers() {
        Random rand = new Random();
        player_list = new ArrayList<>();
        deck = new DeckOfCards();
        human_player = new HumanPlayer(twitter, user, base_status_id);
        player_list.add(human_player);
        for(int i=HUMAN_INDEX+1; i<=NUMBER_OF_BOTS; i++){
            int discard_minimum = rand.nextInt(DISCARD_MINIMUM_RANGE)+(DISCARD_MAXIMUM-(DISCARD_MINIMUM_RANGE*i));
            player_list.add(new AIPlayer(i, discard_minimum));
        }
    }

    /**Getter for the unique twitter user object of a poker game.*/
    User getUser(){
        return user;
    }

    /**Getter for the single human object in this game.*/
    HumanPlayer getHumanPlayer(){
        return (HumanPlayer) player_list.get(HUMAN_INDEX);
    }

    /**Getter of the boolean flag to signal this game has ended naturally and should have its thread terminated by the TwitterInterface*/
    boolean isGameEnded(){
        return game_ended;
    }

    /**Method called by run() that should execute continuously within its thread for the game's duration.
     * Controls flow of game by creating rounds and governing which players are still in the game.*/
    @SuppressWarnings("StringConcatenationInLoop")
    private void playGame() throws TwitterException {
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
                if(player_list.get(i).getChips() < RoundOfPoker.ANTE) {
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
                win_message = "You have won! Congratulations!\nThank you for playing";
                game_ended = true;
            }
        }
        twitter.postMessageToUser(win_message, human_player);
        twitter.endGame(human_player);
    }

    /**Delivers message to the player if they tweet #rsdealmeout to end the game prematurely.*/
    void quitMessage(){
        twitter.postMessageToUser("You have tweeted the hashtag to end the game\nThank You for playing!", human_player);
    }

    /**Delivers message to the player if they tweet #rsdealmein while playing this game, ending the current object
     * and creating a fresh one.*/
    void interruptMessage(){
        twitter.postMessageToUser("You have tweeted the hashtag to start an new game, ending the one currently " +
                "in progress\nThank You for playing!", human_player);
    }

}
