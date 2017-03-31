/*package poker;

import twitter4j.*;
import twitter4j.api.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;*/

/**
 * Created by Orla on 20/03/2017.
 */
public class TwitterInterface {
/*
    TwitterFactory tf;
    ConfigurationBuilder cb = new ConfigurationBuilder();
    Twitter twitter;
    List<Status> tweets;
    TwitterFactory config;

    public TwitterInterface() throws IOException {

        Twitter config = getConfiguration();
    }

    public Twitter getConfiguration() {
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("se1REJHrrSYeVoW676cQRXojJ")
                .setOAuthConsumerSecret("xDucls8Mxtleyk8eKBLsBt3JDl9RtFrzuhMNSUIBAOKt1iX2NU")
                .setOAuthAccessToken("843979502158004225-9FjLlo7kDHhWeNUVFfQC0t3Rgic3FUR")
                .setOAuthAccessTokenSecret("kp2EXtlo10RXtUCLgxg8dPfvozNalzpsyCVUm5UDR2Nf4");
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        return twitter;
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


    public void getUserTweet(String user) {

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

*/


}
