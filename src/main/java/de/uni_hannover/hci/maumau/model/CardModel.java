/**
 * the class card represents the in game cards with it's color and amount
 * @author Daniel Tverdunov
 * @version 0.1 - 22.06.2021 - 18:39
 */
package main.java.de.uni_hannover.hci.maumau.model;

/**
 * This class implements the cardModel of Card
 * @author Daniel Tverdunov & Justus Finke
 * @version 0.2 05.07.2021
 */
public class CardModel {

    private int zAmount; /**the amount of the card*/
    private Color zColor; /**the color of the card*/
    private final Color zInitialColor; /**the initial card color(mainly for jacks)**/
    /**
     * constructor of Card
     * @param pAmount - which amount from 1-13 should the card get
     * @param pColor -
     */
    CardModel(int pAmount, Color pColor){
        zAmount = pAmount;
        zColor = pColor;
        zInitialColor = pColor;
    }
    /**get and set methods*/
    public int getzAmount() {
        return zAmount;
    }

    public void setzAmount(int zAmount) {
        this.zAmount = zAmount;
    }

    public Color getzColor() {
        return zColor;
    }

    public void setzColor(Color zColor) {
        this.zColor = zColor;
    }

    public Color getzInitialColor() {
        return zInitialColor;
    }
}
