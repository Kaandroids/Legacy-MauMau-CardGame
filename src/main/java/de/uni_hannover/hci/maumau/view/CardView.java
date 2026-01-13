package main.java.de.uni_hannover.hci.maumau.view;
import main.java.de.uni_hannover.hci.maumau.model.Color;

/**
 * This class represents the View-part of Card
 * @author Daniel Tverdunov & Justus Finke
 * @version 0.1 - 05.07.2021
 */
public class CardView {
    /**
     * this method prints the console representation of a card
     * @param color gets the color of the card
     * @param amount gets the amount of the card
     */
    public String showCard(Color color, int amount){
        String s = "" + amount;
        if(amount < 10){
            s = 0 + s;
        }
        return s + color.toString();
    }
}
