package poker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.nio.Buffer;

/**
 * Created by Gavin on 12/04/2017.
 */
public class Picture extends JFrame {

    PlayingCard[] hand;
    BufferedImage image;

    public Picture (PlayingCard[] handOfCards) {
        super("Card Pane");
        hand = handOfCards;
    }

    public BufferedImage createPicture() {

//        System.out.println(hand.toString()); // for testing and verification
        String cardsString[] = new String[5];

        // checks card suit and coverts to String to finish file name
        for (int i=0;i<cardsString.length;i++) {
            System.out.println(hand[i].getCardName());
            cardsString[i] = hand[i].getCardName() + "_of_";
            if ((int) hand[i].getCardSuit() == (int) ('\u2665')) { // hearts
                cardsString[i] += "hearts";
            } else if ((int) hand[i].getCardSuit() == (int) ('\u2666')) { // diamonds
                cardsString[i] += "diamonds";
            } else if ((int) hand[i].getCardSuit() == (int) ('\u2663')) { // clubs
                cardsString[i] += "clubs";
            } else if ((int) hand[i].getCardSuit() == (int) ('\u2660')) { // spades
                cardsString[i] += "spades";
            } else {
                System.out.println("ERROR IN SYMBOL TO STRING");
            }
        }

        JPanel handOfCardsPanel = new JPanel();
        handOfCardsPanel.setLayout(new BoxLayout(handOfCardsPanel, BoxLayout.X_AXIS));

        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel(); // sets up panels

        BufferedImage cardPic = null;
        BufferedImage cardPic1 = null;
        BufferedImage cardPic2 = null;
        BufferedImage cardPic3 = null;
        BufferedImage cardPic4 = null; // sets up picture

        // gets pics from files by name
        try {
            cardPic = ImageIO.read(new File("src/resources/PNG-cards-1.3/" + cardsString[0] + ".png"));
            cardPic1 = ImageIO.read(new File("src/resources/PNG-cards-1.3/" + cardsString[1] + ".png"));
            cardPic2 = ImageIO.read(new File("src/resources/PNG-cards-1.3/" + cardsString[2] + ".png"));
            cardPic3 = ImageIO.read(new File("src/resources/PNG-cards-1.3/" + cardsString[3] + ".png"));
            cardPic4 = ImageIO.read(new File("src/resources/PNG-cards-1.3/" + cardsString[4] + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // sets to JLabel and adds to each JPanel
        JLabel picLabel = new JLabel(new ImageIcon(cardPic));
        panel.add(picLabel);
        JLabel picLabel1 = new JLabel(new ImageIcon(cardPic1));
        panel1.add(picLabel1);
        JLabel picLabel2 = new JLabel(new ImageIcon(cardPic2));
        panel1.add(picLabel2);
        JLabel picLabel3 = new JLabel(new ImageIcon(cardPic3));
        panel1.add(picLabel3);
        JLabel picLabel4 = new JLabel(new ImageIcon(cardPic4));
        panel1.add(picLabel4);

        // adds to main JPanel
        handOfCardsPanel.add(panel);
        handOfCardsPanel.add(panel1);
        handOfCardsPanel.add(panel2);
        handOfCardsPanel.add(panel3);
        handOfCardsPanel.add(panel4);

        add(handOfCardsPanel);
        pack();

        // Outputs image of hand to folder twitter_output
        BufferedImage output = new BufferedImage(handOfCardsPanel.getWidth(), handOfCardsPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = output.createGraphics();
        handOfCardsPanel.print(g);
        g.dispose();
        try {
            ImageIO.write(output, "png", new File("src/twitter_output/hand_picture.png"));
            BufferedImage image = output;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setImage(image);
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return this.image;
    }


    // for testing, can be deleted before submission
/*
    public static void main(String[] args) throws IOException {
        new Picture(new HandOfCards(new DeckOfCards())).setVisible(false);
    }

*/
}

