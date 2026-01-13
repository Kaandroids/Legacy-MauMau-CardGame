package main.java.de.uni_hannover.hci.maumau.controller;

import main.java.de.uni_hannover.hci.maumau.model.CardModel;
import main.java.de.uni_hannover.hci.maumau.model.Color;
import main.java.de.uni_hannover.hci.maumau.view.CardView;

/**
 * This class controls the actions that can be taken with the cards.
 * @author Justus Finke & Daniel Tverdunov
 * @version 0.1 - 30.06.21
 */
public class CardController {
    /** Card that is being controlled */
    private CardModel card;
    /** Visible output of CardController*/
    private CardView view;

    /**
     * Class constructor
     * @param card Card that is being controlled
     */
    public CardController(CardModel card) {
        this.card = card;
        this.view = new CardView();
    }

    /** Getter and setter */
    public int getCardAmount() {
        return card.getzAmount();
    }

    public Color getCardColor(){
        return card.getzColor();
    }

    public void setCardAmount(int amount){
        card.setzAmount(amount);
    }

    public void setCardColor(Color color){
        card.setzColor(color);
    }

    public Color getInitialColor() {return card.getzInitialColor();}

    /**
     * returns String representation of the card
     * @return s - String representation of the card
     */
    public String showCard(){
        return view.showCard(this.getCardColor(), this.getCardAmount());
    }

    /**
     * Determines if card can be placed on any given card
     * @param topCard Usually top card of the stack
     * @return true if card can be played, false if it can not
     */
    public boolean isPlayable(CardController topCard){
        if(this.getCardAmount() == 9) {
            return true;
        } else if(this.getCardAmount() == 11 && topCard.getCardAmount() != 11) {
            return true;
        } else if(this.getCardAmount() == 11 && topCard.getCardAmount() == 11) {
            return false;
        } else if(this.getCardAmount() == topCard.getCardAmount() || this.getCardColor() == topCard.getCardColor()){
            return true;
        } else {
            return false;
        }
    }
}
