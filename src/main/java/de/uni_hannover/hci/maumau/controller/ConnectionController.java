package main.java.de.uni_hannover.hci.maumau.controller;
import main.java.de.uni_hannover.hci.maumau.model.Color;
import main.java.de.uni_hannover.hci.maumau.server.Server;

/**
 * This class serves as a connection between server and game logic.
 * @author Daniel Tverdunov, Justus Finke, Kaan Kara
 * @version 13.07.2021
 */
public class ConnectionController {
    /** game logic*/
    public GameController gameController;
    /** Server*/
    public Server server;

    /**
     * Class constructor
     * @param server Server
     * @param amount Player count
     */
    public ConnectionController(Server server, int amount) {
        this.server = server;
        this.gameController = new GameController(amount);
        setup();
    }

    /**
     * Sends all information that is needed to the server. Shouldn't be called outside of constructor.
     */
    private void setup(){
        server.chancePlayedCard(gameController.getCurrentCard().showCard());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < gameController.model.zPlayers.length; ++i){
            PlayerController player = gameController.model.zPlayers[i];
            for(CardController card : player.model.hand){
                server.addCard(i, card.showCard());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * This method takes the user input and turns it into actions in the game's logic.
     * @param signal -2 = mau / -1 draw / else try to play the card at signal
     */
    //Server is supposed to call this method which in turn will call necessary server and game methods
    public void takeAction(int signal) {
        //mau = -2, draw = -1, else try card on index signal
        if (signal == -2) {
            gameController.setMau();
        } else if (signal == -1) {
            String s = gameController.drawCard(gameController.getCurrentPlayer());
            if (s != null) {
                server.addCard(gameController.getCurrentPlayer(), s);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameController.nextPlayer();
            //Server draw  befehl mit s als input
        } else {
            boolean success = gameController.playCard(signal);
            if (success) {
                server.removeCard(gameController.getCurrentPlayer(), gameController.getCurrentCard().showCard());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                server.chancePlayedCard(gameController.getCurrentCard().showCard());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //condition checks
                if(!gameController.checkMau()) {
                    String firstCard = gameController.drawCard(gameController.getCurrentPlayer());
                    if(firstCard != null) {
                        server.addCard(gameController.getCurrentPlayer(), firstCard);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String secondCard = gameController.drawCard(gameController.getCurrentPlayer());
                    if(secondCard != null) {
                        server.addCard(gameController.getCurrentPlayer(), secondCard);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(gameController.checkWin()){
                    server.win(gameController.getCurrentPlayer());
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                //wenn success muss noch die neue oberste karte überprüft werden
                int check = gameController.checkCard();
                if (check == 7) {
                    String firstCard = gameController.drawCard(gameController.getNextPlayer());
                    if(firstCard != null) {
                        server.addCard(gameController.getNextPlayer(), firstCard);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String secondCard = gameController.drawCard(gameController.getNextPlayer());
                    if(secondCard != null) {
                        server.addCard(gameController.getNextPlayer(), secondCard);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (check == 8) {
                    gameController.nextPlayer();
                } else if (check == 11) {
                    Color c = server.takeWish();
                    gameController.wish(c);
                }
                if (check != 1) {
                    gameController.nextPlayer();

                }

            }
        }
    }
}
