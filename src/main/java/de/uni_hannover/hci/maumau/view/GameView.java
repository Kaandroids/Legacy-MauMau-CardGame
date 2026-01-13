package main.java.de.uni_hannover.hci.maumau.view;

import main.java.de.uni_hannover.hci.maumau.controller.CardController;

/**
 * This class represents the View part of Game
 * @author Daniel Tverdunov & Justus Finke
 * @version 0.1 - 05.07.2021
 */
public class GameView {
    private CardController topCard; /** backup of the Topcard */

    /**
     * prints the console representation of the Game
     */
    public void printGame() {
        topCard.showCard();
        System.out.println("");
    }

    /**
     * setter of topCard
     * @param topCard - New CardController of new topCard
     */
    public void setTopCard(CardController topCard) {
        this.topCard = topCard;
    }

}
