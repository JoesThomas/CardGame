import com.sun.org.apache.xpath.internal.SourceTree;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.*;


public class Deck {
    ArrayList<Card> allCards;

    public Deck() {
        File f = new File("cards.txt");
        Scanner inFile = null;
        try {
            inFile = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        allCards = new ArrayList<Card>();
        while (inFile.hasNext()) {
            Card card = new Card();
            card.load(inFile);
            allCards.add(card);
        }
    }

    public void printAll() {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < allCards.size(); j++) {
                System.out.println(allCards.get(j));
            }
            System.out.println("\nDeck has printed out\n");
        }
    }

    public String imageCard() {
        return (allCards.get(0).toString());
    }

    public void shufflePack() {
        Collections.shuffle(allCards);
        System.out.println("\nDeck has been shuffled\n");
    }

    public void dealCard() {
        if (allCards.size() > 0) {
            allCards.get(0);
            allCards.remove(0);
            System.out.println("\nCard has been dealt\n");
        } else if (allCards.size() == 0) {
            System.out.print("No more cards left");
        }
    }
}