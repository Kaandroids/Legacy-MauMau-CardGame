package main.java.de.uni_hannover.hci.maumau.model;

import main.java.de.uni_hannover.hci.maumau.controller.CardController;
import main.java.de.uni_hannover.hci.maumau.controller.PlayerController;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the Gameboard and game logic of maumau
 * @author Daniel Tverdunov & Justus Finke
 * @version 0.2 - 05.07.2021
 */
public class GameModel {
    public PlayerController[] zPlayers; /** array of players **/
    public ArrayList<CardController> zDrawPile; /** Drawpile **/
    public ArrayList<CardController> zDiscardPile; /** DiscardPile **/
    private CardController zCurrentCard; /** current card on the discard pile **/
    private int zCurrentPlayer = 0; /** index of current player **/
    private final int zPlayerCount; /** amount of players ingame **/

    /** constructor of game
     * Creates the drawpile, players and fills the players hands at the
     * beginning of the game
     **/
    public GameModel(int pPlayerCount){
        if(pPlayerCount <= 0 || pPlayerCount > 4){
            System.err.print("ERR: PlayerCount must be between 1-4");
            System.exit(-1);
        }
        this.zPlayers = new PlayerController[pPlayerCount];
        this.zPlayerCount = pPlayerCount;
        this.zDiscardPile = new ArrayList<>();
        this.zDrawPile = new ArrayList<>();
        //Creating drawpile
        {
            for (Color c : Color.values()) {
                for (int i = 1; i <= 13; i++) {
                    zDrawPile.add(new CardController(new CardModel(i, c)));
                }

                Collections.shuffle(zDrawPile);
            }
            zCurrentCard = zDrawPile.get(0);
            zDrawPile.remove(0);
        }
        //Creating Players
        {
            for (int i = 0; i < pPlayerCount; i++) {
                zPlayers[i] = new PlayerController();
            }
        }
        //filling Player hands
        {
            for (PlayerController player : zPlayers) {
                for (int i = 0; i <= 5; i++) {
                    player.addCard(zDrawPile.get(0));
                    zDrawPile.remove(0);
                }
            }
        }
    }

    /** getter setter of zCurrentCard **/
    public CardController getCurrentCard() {
        return zCurrentCard;
    }

    public void setCurrentCard(CardController zCurrentCard) {
        this.zCurrentCard = zCurrentCard;
    }
    /** getter setter of zCurrentPlayer **/
    public int getCurrentPlayer() {
        return zCurrentPlayer;
    }

    public void setCurrentPlayer(int zCurrentPlayer) {
        this.zCurrentPlayer = zCurrentPlayer;
    }
    /** getter of zPlayerCount **/
    public int getPlayerCount() {
        return zPlayerCount;
    }
}
