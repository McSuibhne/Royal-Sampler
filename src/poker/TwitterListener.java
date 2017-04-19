package poker;

/**
 * Created by Orla on 19/04/2017.
 */

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Orla on 12/04/2017.
 */
public class TwitterListener {
    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;

    public String twittername;
    public long statusId;
    //  List<Status> listofStatuses;
    Configuration config= setConfiguration();
    StatusListener listener;
    TwitterStream stream;
    GameOfPoker game;
    ArrayList<GameOfPoker> gamelist= new ArrayList();
    TwitterStreamFactory factory;
    public Configuration setConfiguration() {
        ConfigurationBuilder configurationBuilder= new ConfigurationBuilder();
        String ai_name = null;
        try{

            BufferedReader reader = new BufferedReader(new FileReader(NAMES_FILE));
            String[] line = new String[NAMES_FILE_LENGTH];
            for(int i=0; i<NAMES_FILE_LENGTH; i++){
                line[i] = reader.readLine();

            }
            configurationBuilder.setDebugEnabled(true)
                    .setOAuthConsumerKey(line[0])
                    .setOAuthConsumerSecret(line[1])
                    .setOAuthAccessToken(line[2])
                    .setOAuthAccessTokenSecret(line[3]);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        config=configurationBuilder.build();

        return config; }

    private final Object lock = new Object();



    public GameOfPoker startGame(String[] word, TwitterInterface twitterInterface) throws TwitterException {

        stream = new TwitterStreamFactory(config).getInstance();;
        StatusListener listener = new StatusListener() {
            public void onStatus(Status status) {
                String t = "@" + status.getUser ( ).getScreenName ( );
                long id= status.getId ( );
                twittername = "@" + status.getUser ( ).getScreenName ( );
                statusId = status.getId ( );

                    if (status.getText().contains(word[0])){
                        GameOfPoker     game = new GameOfPoker (twitterInterface, twittername, statusId);
                            gamelist.add(game);

                            try {

                                game.createPlayer();
                                // game.playGame(player_list, deck);
                            } catch (TwitterException e) {
                                e.printStackTrace();
                               }
                        }

                    else if(status.getText().contains(word[1])) {

                        for (int i=0; i< gamelist.size();i++){

                            if( gamelist.get(i).tname.equals(t)){
                                //gamelist.get().sayGoodBye();
                                gamelist.remove(i);

                            }                       }
                    }


               /// boolean dealin = status.getText ( ).contains (word[]);




//                    game.notify ( );
                    System.out.println (status.getText());

                }



            public void onDeletionNotice(
                    StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:"
                        + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:"
                        + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId
                        + " upToStatusId:" + upToStatusId);
            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                System.out.println(sw.getMessage());

            }
        };


        FilterQuery fq = new FilterQuery();


        fq.track(word);


        stream.addListener(listener);
        stream.filter(fq);

        try {
            synchronized (lock) {
                lock.wait();

            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("returning statuses");
        stream.shutdown();

        return game;
    }
    public boolean stopGame(String word) throws TwitterException {

        stream = new TwitterStreamFactory (config).getInstance ( );
        final boolean[] flag = {false};

        StatusListener listener = new StatusListener ( ) {


            @Override
            public void onException(Exception ex) {

            }

            public void onStatus(Status status) {

             String t = "@" + status.getUser ( ).getScreenName ( );
                   long id= status.getId ( );
                  for (int i=0; i< gamelist.size();i++){
                 synchronized(gamelist)
                 {if( gamelist.get(i).tname.equals(t)){
                         gamelist.remove(i);
                     }}
                  }

                    stream.shutdown ( );
                  //  game.notify ( );
                    System.out.println ("unlocked");

                }


            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }
        };
        stream.addListener(listener);
        stream.filter(word);

        try {
            synchronized (lock) {
                lock.wait();

            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("returning statuses");
        stream.shutdown();
        FilterQuery fq = new FilterQuery();
        fq.track(word);
        return flag[0];
    }







    public void createGames(List list, TwitterInterface twitterInterface) throws TwitterException, IOException {

        GameOfPoker[] games = new GameOfPoker[0];
        int i=0;
        for (i = 0; i < list.size ( ); i++) {
            List names = (List) list.get (0);
            List ids = (List) list.get (1);
            for (int j = 0; j < names.size ( ); j++) {
                twittername = "@"+names.get (j).toString ( );
                for (int k = 0; k < ids.size ( ); k++) {
                    statusId = Long.parseLong (ids.get (k).toString ( ));
                }
            }

        }
        System.out.println ("This is  the " + twittername + " This is the Id " + statusId);
        GameOfPoker gameOfPoker=new GameOfPoker (twitterInterface, twittername, statusId);

    }


   /* public void stopGames(List list, TwitterInterface) throws TwitterException, IOException {
      else if (list) {
        for (int i = 0; i < list.size ( ); i++) {
            List names = (List) list.get (0);
            List ids = (List) list.get (1);
            for (int j = 0; j < names.size ( ); j++) {
                twittername = "@" + names.get (j).toString ( );
                for (int k = 0; k < ids.size ( ); k++) {
                    statusId = Long.parseLong (ids.get (k).toString ( ));
                }
            }
            System.out.println ("This is  the " + twittername + " This is the Id " + statusId);
            gameOfPoker[i].stopGame(statusId);
        }
    }*/



    public static void main(String[] args) throws TwitterException, IOException {
        TwitterInterface twitterInterface= new TwitterInterface ();
        String[] keywords = {"rsdealmein","rsdealmeout"};
        twitterInterface.startGame (keywords,twitterInterface);

       // twitterInterface.stopGame ("rsdealmeout");
    }




}