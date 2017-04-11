package poker;

import twitter4j.*;
import twitter4j.api.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;


import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Orla on 20/03/2017.
 */
public class TwitterInterface {
    public static final String NAMES_FILE = "src/poker/TwitterConfig.txt";
    public static final int NAMES_FILE_LENGTH = 4;
    TwitterFactory tf;

    Twitter twitter;
    List<Status> tweets;
    Configuration config= setConfiguration();

    public TwitterInterface() throws IOException {
        tf = new TwitterFactory(config);
        twitter = tf.getInstance();
    }


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
    public String getUserTweet(String user) {

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
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());

        }
        return null;
    }


    public void searchKeywords(String searchstring) {
        try {
            Query query = new Query(searchstring);
            QueryResult result;
            result = twitter.search(query);
            tweets = result.getTweets();
//tweets.get(1).getUser().getScreenName();
            for (int i =0 ; i <tweets.size(); i++) {
                System.out.println("@" + tweets.get(i).getUser().getScreenName() + " - " + tweets.get(i).getText());
            }

            // System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());

        }

    }

    public void postreply(String answer) {
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
        String name= statuses.get(0).getUser().getScreenName();
       long statusId= statuses.get(0).getQuotedStatusId();
         answer = "replying to @" + name + ": message is "+answer;
        StatusUpdate statusReply = new StatusUpdate(answer);
        statusReply.setInReplyToStatusId(statusId);
        try {
            twitter.updateStatus(statusReply);
            System.out.println("Status reply successfull "+ answer);
        } catch (TwitterException ex) {
            // handle exception
        }


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



    public static void main(String[] args) {
        TwitterInterface twit = null;
        try {
            twit = new TwitterInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
   // twit.mentions();
   //     twit.postreply("replying to tweetfingers crossed ");
//twit.searchKeywords("#rsdealmein");

//twit.postreply();
    }




}
