///**
// * The Game of patience main class
// *
// * @author Joe Thomas
// * @version 3.0
// */
//

import java.io.FileWriter;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

import static jdk.nashorn.internal.objects.ArrayBufferView.length;

public class Game extends Application {
    Scanner in;
    private CardTable cardTable;
    private Deck deck;
    ArrayList<String> dealtCards;
    ArrayList<HighScore> scores;

    public Game() {
        in = new Scanner(System.in);
        deck = new Deck();
        dealtCards = new ArrayList<>();
    }

    private void printMenu() {
        System.out.println("1 -  print the pack out");
        System.out.println("2 -  shuffle");
        System.out.println("3 -  deal a card");
        System.out.println("4 -  make a move, move last pile onto previous one");
        System.out.println("5 -  make a move, move last pile back over two piles");
        System.out.println("6 -  amalgamate piles in the middle (by giving their numbers)");
        System.out.println("7 -  print the displayed cards on the command line");
        System.out.println("8 -  play for me once (if two possible moves, makes the ‘furthest’ move)");
        System.out.println("9 -  play for me many times");
        System.out.println("10 -  display top 10 results");
        System.out.println("Q - Quit");
    }

    /**
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        cardTable = new CardTable(stage);
        Runnable commandLineTask = () -> {
            String response;
            do {
                printMenu();
                System.out.print("\nWhat would you like to do:");
                response = in.nextLine().toUpperCase();
                switch (response) {
                    case "1":
                        deck.printAll();
                        break;
                    case "2":
                        deck.shufflePack();
                        break;
                    case "3":
                        dealtCards.add(deck.imageCard());
                        deck.dealCard();
                        break;
                    case "4":
                        movePilePrevious();
                        break;
                    case "5":
                        movePiles();
                        break;
                    case "6":
                        amalgamate();
                        break;
                    case "7":
                        printCommand();
                        break;
                    case "8":
                        playOnce();
                        break;
                    case "9":
                        playManyTimes();
                        break;
                    case "10":
                        displayTopTen();
                        break;
                    case "Q":
                        //Exiting
                        save();
                        break;
                    default:
                        System.out.println("Try again");
                }
                cardTable.cardDisplay(dealtCards);
                if (deck.allCards.size() == 0) {
                    System.out.println("GAME OVER");
                    break;
                }
            } while (!(response.equals("Q")));
        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();
    }

    private void movePilePrevious() {
        if (dealtCards.size() < 2) {
            System.out.println("There are not enough cards dealt so far");
            return;
        }
        checkToMoveToTheSide();
    }

    private void amalgamate() {
        System.out.println("which card is moving?");
        int card1 = in.nextInt();
        int cardOne = (card1 - 1);
        if (cardOne > dealtCards.size()) {
            System.out.println("Card not in deck");
            return;
        }
        System.out.println("which card is being replaced?");
        int card2 = in.nextInt();
        int cardTwo = (card2 - 1);
        if (cardTwo > dealtCards.size()) {
            System.out.println("Card not in deck");
            return;
        }
        String cardFirst = dealtCards.get(cardOne);
        Card firstCard = new Card(cardFirst);
        String cardSecond = dealtCards.get(cardTwo);
        Card secondCard = new Card(cardSecond);
        if (!firstCard.canMerge(secondCard)) {
            System.out.println("this isnt possible");
        }

        dealtCards.set(cardTwo, cardFirst);
        dealtCards.remove(cardFirst);
    }

    public void printCommand() {
        if (dealtCards.size() == 0) {
            System.out.println("\nNo cards have been dealt so far\n");
        } else if (dealtCards.size() > 0) {
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < dealtCards.size(); j++) {
                    System.out.println(dealtCards.get(j));
                }
                System.out.println();
                System.out.println("\nCards have been printed\n");
            }
        }
    }

    public void movePiles() {
        if (dealtCards.size() < 4) {
            System.out.println("\nThere are no cards dealt so far\n");
            return;
        }
        checkToMoveTwo();
    }

    /**
     * @return true if
     */
    boolean checkToMoveToTheSide() {
        if (dealtCards.size() <= 1) {
            System.out.println("\nNot enough cards dealt to move last pile onto the previous one\n");
            return false;
        }
        String lastFaceUpCardString = dealtCards.get(dealtCards.size() - 1);
        Card lastFaceUpCard = new Card(lastFaceUpCardString);
        String otherCardString = dealtCards.get(dealtCards.size() - 2);
        Card otherCard = new Card(otherCardString);
        if (!lastFaceUpCard.canMerge(otherCard)) {
            System.out.println("\nthis isnt possible\n");
            return false;
        }
        dealtCards.remove(otherCard.toString());
        System.out.println("\nCard has been moved\n");
        return true;
    }

    boolean checkToMoveTwo() {
        if (dealtCards.size() <= 4) {
            System.out.println("\nNot enough cards dealt to move last pile back over two piles\n");
            return false;
        }
        String twoFaceUpCardString = dealtCards.get(dealtCards.size() - 1);
        Card twoFaceUpCard = new Card(twoFaceUpCardString);
        String theOtherCardString = dealtCards.get(dealtCards.size() - 4);
        Card theOtherCard = new Card(theOtherCardString);
        if (!twoFaceUpCard.canMerge(theOtherCard)) {
            System.out.println("\nthis isn't possible\n");
            return false;
        }
        dealtCards.remove(twoFaceUpCardString);
        dealtCards.set(dealtCards.size() - 3, twoFaceUpCardString);
        System.out.println("\nCard has been moved\n");
        return true;
    }

    public void playOnce() {
        if (deck.allCards.size() > 0) {
            dealtCards.add(deck.imageCard());
            deck.dealCard();
            if (checkToMoveTwo()) {
                movePiles();
                System.out.println("\nGame has been played once\n");
            } else if (checkToMoveToTheSide()) {
                movePilePrevious();
                System.out.println("\nGame has been played once\n");
            } else if (deck.allCards.size() == 0) {
                System.out.print("\nNo more cards left\n");
                save();
            }
        }
    }

    public void playManyTimes() {
        System.out.println("Cards left to be dealt:" + deck.allCards.size());
        System.out.println("How many turns shall I play:");
        int turns = in.nextInt();
        if (turns > deck.allCards.size()) {
            System.out.println("\nNot enough cards\n");
        } else if (turns <= deck.allCards.size()) {
            for (int i = 0; i <= turns; i++) {
                playOnce();
                if ((deck.allCards.size()) == 0) {
                    System.out.println("\nNo more cards left\n");
                    save();
                    break;
                }
            }
        }
    }

    public void save() {
        try {
            BufferedWriter pw = new BufferedWriter(new FileWriter("score.txt", true));
            System.out.println("\nEnter name:\n");
            Scanner scanner = new Scanner(System.in);
            String Name = scanner.nextLine();
            pw.append(String.valueOf(dealtCards.size()) + "\n" + Name + "\n");
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayTopTen() {
        File f = new File("score.txt");
        Scanner inFile = null;
        int points;
        String name;
        scores = new ArrayList<HighScore>();
        try {
            inFile = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (inFile.hasNext()) {
            points = inFile.nextInt();
            inFile.nextLine();
            name = inFile.nextLine();
            scores.add(new HighScore(points, name));
        }
        Collections.sort(scores);
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
        System.out.println("Top 10 Scores:\n" + scores + "\n\nTop scores have been displayed\n");
    }

    // //////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        Application.launch(args);
    }
}