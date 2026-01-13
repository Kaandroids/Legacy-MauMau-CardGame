package main.java.de.uni_hannover.hci.maumau.controller;

import main.java.de.uni_hannover.hci.maumau.model.Color;
import main.java.de.uni_hannover.hci.maumau.model.GameModel;
import main.java.de.uni_hannover.hci.maumau.view.GameView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class implements the gamelogic of MauMau.
 * @author Justus Finke & Daniel Tverdunov
 * @version 0.3 05.07.2021
 */
public class GameController {
    /** Model that is being controlled */
    public GameModel model;
    /** Visible output of GameController */
    private GameView view;
    /** Scanner Object for the Console-/Server Inputs **/

    /**
     * Class constructor
     * @param amount Playercount
     */
    public GameController(int amount) {
        this.model = new GameModel(amount);
        this.view = new GameView();
    }
    /** Getter and setter for currentPlayer, currentCard and playerCount */
    public int getCurrentPlayer(){
        return model.getCurrentPlayer();
    }

    public void setCurrentPlayer(int index) {
        model.setCurrentPlayer(index);
    }

    public CardController getCurrentCard() {
        return model.getCurrentCard();
    }

    public void setCurrentCard(CardController card) {
        model.setCurrentCard(card);
    }

    public int getPlayerCount(){
        return model.getPlayerCount();
    }

    /**
     * Determines which Player will get the next turn.
     * @return Index of the next player within model.zPlayers
     */
    //boolean in Playermodel handEmpty or something
    public int getNextPlayer() {
        if(this.getCurrentPlayer() < this.getPlayerCount() - 1){
            return this.getCurrentPlayer() + 1;
        } else {
            return 0;
        }
    }

    /**
     * Checks if the hand of current player is empty
     * @return True if hand is empty, false otherwise
     */
    public boolean checkWin(){
        return model.zPlayers[this.getCurrentPlayer()].handIsEmpty();
    }

    /**
     * Checks if current player has boolean Mau set to true
     * @return False if current player has 1 card left and Mau set to false, true otherwise
     */
    public boolean checkMau() {
        if(model.zPlayers[this.getCurrentPlayer()].getHandSize() == 1) {
            return model.zPlayers[this.getCurrentPlayer()].isMau();

        }
        return true;
    }

    /**
     * Changes color of top card
     * @param c New color of top card
     */
    public void wish(Color c) {
        this.getCurrentCard().setCardColor(c);
    }

    /**
     * Sets boolean mau in current player to true
     */
    public void setMau(){
        model.zPlayers[this.getCurrentPlayer()].setMau(true);
    }

    /**
     * Sets currentPlayer to the next Player in model.zPlayers
     */
    public void nextPlayer(){
        setCurrentPlayer(this.getNextPlayer());
    }

    /**
     * Shuffles the discard pile and turns it into a new draw pile.
     * Should only be called when draw pile is empty.
     */
    public void shuffle(){
        Collections.shuffle(model.zDiscardPile);
        model.zDrawPile = model.zDiscardPile;
        model.zDiscardPile = new ArrayList<>();
    }

    /**
     * Makes player at playerIndex draw pAmount of cards from the draw pile
     * @param playerIndex Determines which player has to draw cards
     */
    public String drawCard(int playerIndex) {
            if(model.zDrawPile.isEmpty()){
                shuffle();
            }
            if(model.zDrawPile.isEmpty()){
                return null;
            }
            model.zPlayers[playerIndex].addCard(model.zDrawPile.get(0));
            model.zDrawPile.remove(0);
            model.zPlayers[playerIndex].setMau(false);
            return model.zPlayers[playerIndex].getLastCard().showCard();
    }

    /**
     * Gives the player the possibility to take a turn
     * @return true if turn taken successfully , false otherwise
     */

    public boolean playCard(int serverCommand){
        //Cardindex signal passed
        if (serverCommand <= model.zPlayers[this.getCurrentPlayer()].getHandSize() - 1 && model.zPlayers[this.getCurrentPlayer()].model.hand.get(serverCommand).isPlayable(getCurrentCard())) {
            this.getCurrentCard().setCardColor(this.getCurrentCard().getInitialColor());
            model.zDiscardPile.add(getCurrentCard());
            this.setCurrentCard(model.zPlayers[this.getCurrentPlayer()].playCard(serverCommand));
            return true;
        }
        return false;
    }

    /**
     * Checks top card of the pile
     * @return returns top card value if relevant
     */
    public int checkCard() {
        int value = this.getCurrentCard().getCardAmount();
        if(value == 1) {
            return 1;
        } else if(value == 7) {
            return 7;
        } else if(value == 8) {
            return 8;
        } else if(value == 11) {
            return 11;
        }
        return 0;
    }

    /**
     * Updates the view/ the representation of the Board
     */
    public void updateView(){
        view.setTopCard(this.getCurrentCard());
    }
}
