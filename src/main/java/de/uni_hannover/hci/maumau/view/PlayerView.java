package main.java.de.uni_hannover.hci.maumau.view;
//test

import main.java.de.uni_hannover.hci.maumau.controller.CardController;

import java.util.ArrayList;

/**
 * represents the View part of Player
 * @author Daniel Tverdunov & Justus Finke
 * @version 0.1 - 05.07.2021
 */
public class PlayerView {

    /**
     * prints the console representation of the current players hand
     * @param hand - current players ArrayList hand
     */
    public void showPlayer(ArrayList<CardController> hand){
        for(CardController c : hand) {
            c.showCard();
            System.out.print("  ");
        }
    }
}
