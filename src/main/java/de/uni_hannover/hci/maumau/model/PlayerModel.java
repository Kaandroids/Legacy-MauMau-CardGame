package main.java.de.uni_hannover.hci.maumau.model;

import main.java.de.uni_hannover.hci.maumau.controller.CardController;

import java.util.ArrayList;

/**
 * This class represents the player within the game
 * @author Justus Finke + Daniel Tverdunov
 * @version 0.2 - 25.06.2021
 */
public class PlayerModel {
    /** name of the player */
    private String name;

    /** whether or not the player pressed mau**/
    private boolean maumau = false;

    /** hand of the player */
    public ArrayList<CardController> hand;

    /**
     * Constructor of PlayerModel
     */
    public PlayerModel() {
        this.hand = new ArrayList<>();
    }

    /** getter and setter of name*/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /** getter setter of maumau **/
    public boolean isMaumau() {
        return maumau;
    }

    public void setMaumau(boolean maumau) {
        this.maumau = maumau;
    }
}
