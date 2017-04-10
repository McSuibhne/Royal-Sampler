package poker;

import java.util.*;

/**
 * AI  Poker Player Class
 */
public class AIPlayer extends  PokerPlayer {

   List<String> strings = Arrays.asList("Fred, Bruce,Hermione, Dumbledor, Annabelle ,Bart,Raven,Phoenix ,Mystique, Wolverine, Katniss, Thor, Maximus, Zena, Storm".split(","));
    ArrayList<String> used = new ArrayList<String>();
    public AIPlayer(DeckOfCards deck,String name) {
        super(deck, name);
    }

    public void setPlayer_name() {
        String name;

        Random random = new Random();
        int namePostion = random.nextInt(strings.size());
        name = strings.get(namePostion);
        boolean flag = isNameUsedAlready(name);
        while (flag) {
            namePostion = random.nextInt(strings.size());
            name = strings.get(namePostion);
            flag = isNameUsedAlready(name);
        }
            setPlayer_name(name);

    }

        boolean  isNameUsedAlready(String name){
        boolean flag=false;
        for(int j=0;j<used.size();j++){
           if (used.get(j).equals(name)){
           flag= true;}
            else{
               flag=false;
            }
        }
            used.add(name);
       return flag; }
    }







