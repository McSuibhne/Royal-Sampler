package poker;

import twitter4j.*;
import twitter4j.media.ImageUpload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Orla on 20/03/2017.
 */
public class TwitterInterface extends TwitterListener {

    TwitterFactory tf;
    Twitter twitter;
    List<Status> tweets;

    public TwitterInterface() throws IOException {
        tf = new TwitterFactory(config);
        twitter = tf.getInstance();
    }

    public void postreply(String answer, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;
        long replyId = humanPlayer.getTweetId();

        StatusUpdate statusReply = new StatusUpdate(answer);
        statusReply.setInReplyToStatusId(replyId);

        try {
            twitter.updateStatus(statusReply);
            System.out.println("Status reply successfull " + answer);
            List<Status> h = twitter.getHomeTimeline();
            System.out.println(h.get(0).getText());
            for (int i = 0; i < h.size(); i++) {
                if (h.get(i).getInReplyToStatusId() == replyId) {
                    humanPlayer.setTweetId(h.get(i).getId());
                    break;
                }
            }
        } catch (TwitterException ex) {
            // handle exception
        }
    }

    public void postImage(HandOfCards currentHand, String answer, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;
        File file = new File("src/twitter_output/hand_picture.png");
        long replyId = humanPlayer.getTweetId();
        ImageUpload image = null;

        StatusUpdate statusReply = new StatusUpdate(answer);
        statusReply.setInReplyToStatusId(replyId);
        statusReply.setMedia(file);

        try {
            twitter.updateStatus(statusReply);
            System.out.println("Status reply successfull " + currentHand);
            List<Status> h = twitter.getHomeTimeline();
            System.out.println(h.get(0).getText());
            for (int i = 0; i < h.size(); i++) {
                if (h.get(i).getInReplyToStatusId() == replyId) {
                    humanPlayer.setTweetId(h.get(i).getId());
                    break;
                }
            }
        } catch (TwitterException ex) {
            // handle exception
        }
    }

    public String getTweet(String word, PokerPlayer pokerPlayer) {
        HumanPlayer humanPlayer = (HumanPlayer) pokerPlayer;

        Status s = null;


        try {

            Query query = new Query(word);

            System.out.println("query string: " + query.getQuery());

            try {
                query.setCount(100);

            } catch (Throwable e) {
                // enlarge buffer error?
                query.setCount(30);

            }
            QueryResult result = null;

            try {
                result = twitter.search(query);

            } catch (TwitterException e1) {
                e1.printStackTrace();
            }
            int j = 0;
            while (j < 1) {
                TimeUnit.MINUTES.sleep(1);

                try {
                    result = twitter.search(query);

                } catch (TwitterException e1) {
                    e1.printStackTrace();
                }
                if (result.getTweets().size() >= 1) {
                    tweets = result.getTweets();
                    for (int i = 0; i < tweets.size(); i++) {
                        String name = "@" + result.getTweets().get(i).getUser().getScreenName();
                        if (name.equals(humanPlayer.getName())) {
                            System.out.println("result: " + result.getTweets().get(i).getText());
                            s = result.getTweets().get(i);
                            break;
                        }
                    }
                    System.out.println("result: " + result.getTweets().get(0).getText());
                    System.out.println("result: " + result.getTweets().size());
                }
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        String h;

        // humanPlayer.setTweetId(s.getId());
        h = s.getText();


        return h;
    }


}

