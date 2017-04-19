package poker;

import twitter4j.*;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Orla on 20/03/2017.
 */
public class TwitterInterface extends TwitterListener {

    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;
    TwitterFactory tf;


    Twitter twitter;
    List<Status> tweets;
    public TwitterInterface() throws IOException {
        tf = new TwitterFactory(config);
        twitter = tf.getInstance();
    }
    public void postaStatus(String s) {
        String testStatus = s;
        Status status = null;
        try {
            status = twitter.updateStatus(testStatus);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Failed to get timeline: " + e.getMessage());
        }

    }

    /*10-04-17:
     *  Changed return type to "String" from "void" and added "return null" at end just to remove error
     *  in HumanPlayer line 65 and allow project to run. Just placeholder code, remove or replace it whenever.
     *  -Jonathan
     */
    public String getUserTweet(String user,String word) {
        boolean flag=false;
        try {
            List<Status> statuses;
            if (user != null) {

                statuses = twitter.getUserTimeline(user);
            } else {
                user = twitter.verifyCredentials().getScreenName();
                statuses = twitter.getUserTimeline();
            }
            System.out.println("Showing @" + user + "'s user timeline.");
            for (Status status : statuses) {

                if (status.getText ().contains (word)){

                }
                // System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());

        }
        return user;
    }

    public long getStatusId(){

        return statusId;}


    public boolean searchKeywords(String searchstring, long id) {
        Status status = null;
        boolean flag=false;
        try {

            Query query = new Query(searchstring);
            QueryResult result;
            result = twitter.search(query);
            tweets = result.getTweets();

            for (int i = 0; i < tweets.size(); i++) {

                if  (tweets.get (i).getInReplyToStatusId()==id) {
                    long l = tweets.get(i).getInReplyToStatusId();

                    flag=true;

                }
                // System.out.println("@" + tweets.get(i).getUser().getScreenName() + " - " + tweets.get(i).getText() + " -" + tweets.get(i).getId());
                //  id = tweets.get(0).getId();
            }

            // System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());

        }

        return flag;
    }

    public void postreply(String answer,PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer=(HumanPlayer) pokerPlayer;
        try {
            User user = twitter.verifyCredentials();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        List<Status> statuses = null;
        try {
            statuses = twitter.getMentionsTimeline();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        //String name= statuses.get(0).getUser().getScreenName();
        long replyId= humanPlayer.getTweetId();
       ;
        StatusUpdate statusReply = new StatusUpdate(answer);
        statusReply.setInReplyToStatusId(replyId);


        try {
            twitter.updateStatus(statusReply);
            System.out.println("Status reply successfull "+ answer);
            List<Status> h= twitter.getHomeTimeline();
            System.out.println(h.get(0).getText());
            for( int i =0 ;i< h.size(); i++) {
                if(h.get(i).getInReplyToStatusId()==replyId){
                    humanPlayer.setTweetId(h.get(i).getId());
                    break;
                }
            }
        } catch (TwitterException ex) {
            // handle exception
        }

       // temp.setTweetId(getStatusId());
    }

    public static List<Status> findReplies(String answer) {
        Query query =new Query("");
        List<Status> tweets = new ArrayList<Status>();

        Twitter twitter = new TwitterFactory().getInstance();
        try {
            QueryResult result;
            do {
                result = twitter.search(query);

                for (Status tweet : result.getTweets()) {
                    // Replace this logic to check if it's a response to a known tweet
                    if (tweet.getInReplyToStatusId() > 0) {
                        tweets.add(tweet);
                    }
                }
            } while ((query = result.nextQuery()) != null);

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }

        return tweets;
    }
    public void mentions() {
        try {
            User user = twitter.verifyCredentials();
            List<Status> statuses = twitter.getMentionsTimeline();
            System.out.println("Showing @" + user.getScreenName() + "'s mentions.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());

        }
    }




    public String getName(Status tweet){

        return tweet.getUser().getScreenName();
    }



    //  public static void main(String[] args) throws IOException {
//        DeckOfCards deckOfCards=new DeckOfCards();
//        TwitterInterface twit = null;
//        try {
//            twit = new TwitterInterface();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        twit.createListener("trump");
//        twit.getStream ();
//       /* HumanPlayer player= new HumanPlayer(deckOfCards,twit);
//        long id=twit.searchKeywords("rsdealmein");
//       player.getName();
//        String anwer=  player.getName()+ " Please reply ";
//        twit.postreply(anwer, id);*/
////twit.searchKeywords("#rsdealmein");
//
////twit.postreply();*/
    //   }




}

