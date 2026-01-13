package main.java.de.uni_hannover.hci.maumau.controller;

import main.java.de.uni_hannover.hci.maumau.model.Color;
import main.java.de.uni_hannover.hci.maumau.model.PlayerModel;
import main.java.de.uni_hannover.hci.maumau.view.PlayerView;

import java.util.Locale;
import java.util.Scanner;



/**
 * This class contains all actions a Player can take.
 * @author Justus Finke & Daniel Tverdunov
 * @version 0.3 - 05.07.2021
 */
public class PlayerController {
    /** Player that is being controlled */
    public PlayerModel model;
    /** Visible output of PlayerController*/
    private PlayerView view;

    /**
     * Class constructor
     */
    public PlayerController() {
        this.model = new PlayerModel();
        this.view = new PlayerView();
    }
    /** Getter and Setter */
    public String getName(){
        return model.getName();
    }

    public void setName(String name){
        model.setName(name);
    }

    /**
     * Adds a card into the player's hand.
     * @param card Card that is being added to the player's hand.
     */
    public void addCard(CardController card) {
        model.hand.add(card);
    }

    /**
     * Removes a card from the player's hand
     * @param index Determines which card is being removed
     * @return Card that is removed
     */
    public CardController playCard(int index) {
        CardController card = model.hand.get(index);
        model.hand.remove(index);
        return card;
    }

    /**
     * checks whether the hand is empty or not
     * @return  true if empty, false if not
     */
    public boolean handIsEmpty() {
        return model.hand.isEmpty();
    }

    /**
     * returns the Handsize of the current player
     * @return int of cards in hand
     */
    public int getHandSize() {
        return model.hand.size();
    }

    /**
     * return the last card of hand
     * @return CardController - last card of player hand
     */
    public CardController getLastCard(){
        return model.hand.get(this.getHandSize() - 1);
    }

    /**
     * returns whether or not the player pressed the mau button
     * @return true if pressed, false if not
     */
    public boolean isMau() {
        return model.isMaumau();
    }

    /**
     * sets mau to the new param mau
     * @param mau ??
     */
    public void setMau(boolean mau) {
        model.setMaumau(mau);
    }
    }

