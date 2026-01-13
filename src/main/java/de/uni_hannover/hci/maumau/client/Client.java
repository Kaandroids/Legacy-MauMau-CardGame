package main.java.de.uni_hannover.hci.maumau.client;

import main.java.de.uni_hannover.hci.maumau.controller.GameSceneController;
import main.java.de.uni_hannover.hci.maumau.controller.LoginController;
import main.java.de.uni_hannover.hci.maumau.lib.Datapackage;
import main.java.de.uni_hannover.hci.maumau.lib.Execute;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;
import java.util.HashMap;

/**
 *
 * @author Kaan Kara
 * @version 0.0.2, 28 June 2021
 */
public class Client {

    public String username;
    public Socket socket;
    private InetSocketAddress address;
    private Thread listeningThread;
    private HashMap<String, Execute> listenMethods = new HashMap<String, Execute>();
    private int timeout = 10000;
    private LoginController loginController;
    private GameSceneController gameSceneController;

    public void setGameController(GameSceneController gameSceneController){
        this.gameSceneController = gameSceneController;
    }

    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }

    public Client(String hostname, int port, String username ){
        this.username = username;
        this.address = new InetSocketAddress(hostname, port);
        preStart();
        start();
    }

    public boolean isConnected(){
        return socket != null && socket.isConnected();
    }

    public boolean isServerReachable(){
        try {
            Socket tSocket = new Socket();
            tSocket.connect(this.address);
            tSocket.isConnected();
            tSocket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void start(){
        login();
        startPingThread();
        startListening();
    }

    private void startPingThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket != null){
                    try {
                        Thread.sleep(20000);
                        System.out.println("[Client] Ping");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void startListening(){

        //Nicht restart, wenn es schon funktioniert.
        if(listeningThread != null && listeningThread.isAlive()){
            return;
        }
        listeningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {

                        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                        Object ob = in.readObject();

                        if (ob instanceof Datapackage) {

                            final Datapackage msg = (Datapackage) ob;

                            for (final String aktuel : listenMethods.keySet()) {
                                if (aktuel.equals(msg.getBefehl())) {
                                    System.out.println("[Client] Befehl from Server: " + msg.getBefehl() + " Message: " + msg.getMessage() + " Name: " + msg.getName());

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listenMethods.get(aktuel).listen(msg, socket);
                                        }
                                    }).start();
                                    break;
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listeningThread.start();
    }

    private void login(){
        // Connection
        try{
            System.out.println("[Client] Connecting..");
            if (socket != null && socket.isConnected()){
                throw new AlreadyConnectedException();
            }

            socket = new Socket();
            socket.connect(this.address, timeout);

            System.out.println("[Client] Connected " + socket.getRemoteSocketAddress());

            // Login
            try {
                System.out.println("[Client] Logging in...");
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                Datapackage loginInfo = new Datapackage(this.username, false, "LOGIN", "Client connected");
                out.writeObject(loginInfo);
                out.flush();
                System.out.println("[Client] Logged in.");
            } catch (Exception e){
                System.out.println("[Client] Login failed.");
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void preStart(){
        registerMethod("add_message", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                System.out.println("[Client-Listening] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
                gameSceneController.addMessageToArea(msg.getMessage());
            }
        });

        registerMethod("add_your_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                System.out.println("[Client-Listening] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
                gameSceneController.addCard(msg.getMessage());
            }
        });

        registerMethod("add_other_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                System.out.println("[Client-Listening] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
                gameSceneController.addOtherCard(Integer.parseInt(msg.getMessage()));
            }
        });

        registerMethod("remove_your_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                System.out.println("[Client-Listening] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
                gameSceneController.removeCard();
            }
        });

        registerMethod("remove_other_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                System.out.println("[Client-Listening] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
                gameSceneController.removeOtherCard(Integer.parseInt(msg.getMessage()));
            }
        });

        registerMethod("chance_played_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                gameSceneController.chancePlayedCard(msg.getMessage());
            }
        });

        registerMethod("set_notification", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                gameSceneController.setNotification(msg.getMessage());
            }
        });

        registerMethod("start_game", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                gameSceneController.setGameStarted();
            }
        });

        registerMethod("change_turn", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if(msg.getMessage().equals("false")){
                    gameSceneController.changeTurn(false);
                } else {
                    gameSceneController.changeTurn(true);
                }
            }
        });

        registerMethod("close_wish_tab", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                gameSceneController.changeVisiblePropertforWishMenu(false);
            }
        });

        registerMethod("set_id", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                gameSceneController.id = Integer.parseInt(msg.getMessage());
            }
        });
    }

    public void sendMessage(Datapackage msg, int timeout){
        try{
            Socket tSocket = new Socket();
            tSocket.connect(address, timeout);

            ObjectOutputStream tOut = new ObjectOutputStream(new BufferedOutputStream(tSocket.getOutputStream()));
            tOut.writeObject(msg);
            tOut.flush();
            tOut.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerMethod(String befehl, Execute execute){
        listenMethods.put(befehl, execute);
    }


}
