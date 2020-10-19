import sun.util.resources.cldr.pa.CalendarData_pa_Arab_PK;


import java.util.Scanner;

public class Card {

    String cardNumber;
    String cardSuit;

    public Card(String Number, String Suit) {
        cardNumber = Number;
        cardSuit = Suit;
    }

    public Card() {
        this("0", "Unknown");
    }

    public Card(String imagePath) {
        this(getValue(imagePath), getSuit(imagePath));
    }

    public boolean canMerge(Card otherCard) {
        return this.cardSuit.equals(otherCard.cardSuit)
                || this.cardNumber.equals(otherCard.cardNumber);
    }

    private static String getValue(String imagePath) {
        String valueString = imagePath.substring(0, 1);
        if (imagePath.charAt(1) == '0') {
            valueString = imagePath.substring(0, 2);
        }
        return valueString;
    }

    private static String getSuit(String imagePath) {
        if (imagePath.length() == 2) {
            return imagePath.substring(1);
        }
        return imagePath.substring(2, 2);
    }

    public void load(Scanner infile) {
        cardNumber = infile.next();
        cardSuit = infile.next();
        infile.nextLine();
    }

    @Override
    public String toString() {
        return cardNumber + cardSuit;
    }
}