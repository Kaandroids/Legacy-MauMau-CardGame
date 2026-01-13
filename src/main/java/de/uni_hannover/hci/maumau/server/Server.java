package main.java.de.uni_hannover.hci.maumau.server;

import main.java.de.uni_hannover.hci.maumau.controller.ConnectionController;
import main.java.de.uni_hannover.hci.maumau.lib.Datapackage;
import main.java.de.uni_hannover.hci.maumau.lib.Execute;
import main.java.de.uni_hannover.hci.maumau.model.Color;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Server-Client Klasse
 *
 * Diese Klasse enthält alle Methode, die wir bei Kommunikation mit den Clients brauchen.
 * Beim Erstellen des neuen Games (unter Mainmenu - New Game) wird es automatisch vom GUI mit einem Client erstellt.
 * Server start das Game, wenn seine Capacity voll ist. Um Game zu starten, erstellt es die Klasse GameController.
 * Server hat direkte Verbindungen mit GUI und GameController.
 *
 * @author Kaan Kara
 * @version 0.0.3, 14 July 2021
 */
public class Server {

    private ArrayList<Socket> sockets;
    public int port;
    private Thread listeninThread;
    private long pingInterval = 20 * 1000; // 20 seconds
    private ServerSocket server;
    private HashMap<String, Execute> listenMethods = new HashMap<String, Execute>();
    private ConnectionController connectionController;
    public static File serverInfoFile = new File("src/main/java/de/uni_hannover/hci/maumau/server/Servers.txt");
    private static ArrayList<String> serverInfo = new ArrayList<String>();
    private boolean isGameStarted = false;
    public Color color;
    private String serverName;
    private String serverPass;
    private int serverCapacity;
    private int clientId = 0;

    /**
     * Die Daten vom eingegebenen Server wird zur Datei Servers.txt hinzugefügt.
     *
     * @param server Das stellt der Server, der zur Datei Servers.txt hinzugefügt wird, dar.
     */
    private static void addServerInfoToFile(Server server){
        try{
            FileWriter writer = new FileWriter(serverInfoFile, true);
            serverInfo.add(server.port + "," + server.serverName + "," + server.serverPass + "," + server.serverCapacity);
            writer.write(server.port + "," + server.serverName + "," + server.serverPass + "," + server.serverCapacity + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Das gibt eine ArrayList zurück, die aus den Namen von Servers besteht.
     * @return Das enthält die Namen von Servers.
     */
    public static ArrayList<String> getNames(){
        try {
            Scanner scan = new Scanner(serverInfoFile);
            ArrayList<String> toReturn = new ArrayList<String>();
            while(scan.hasNextLine()){
                String[] toAdd = scan.nextLine().split(",");
                toReturn.add(toAdd[1]);
            }

            scan.close();
            return toReturn;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Das liest die Datei Servers.txt. Dann speichert es alle Daten von den Servers in dem Variable serverInfo.
     */
    public static void readServer() {
        try {
            Scanner scan = new Scanner(serverInfoFile);

            while(scan.hasNextLine()){
                serverInfo.add(scan.nextLine());
            }

            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mit dem Name des Servers kann man seine Passwort finden.
     *
     * @param nameServer Das stellt den Name des Servers.
     * @return Das stellt das Port von eingegebenem Server.
     */
    public static int getPassfromFile(String nameServer) {
        while(serverInfo.size() != 0) {
            serverInfo.remove(0);
        }

        readServer();

        for(int i = 0; i < serverInfo.size(); i++) {
            if(serverInfo.get(i).contains(nameServer)) {
                String[] item = serverInfo.get(i).split(",");
                return Integer.parseInt(item[2]);
            }
        }
        return 0;
    }

    /**
     * Mit dem Name des Servers kann man seine Port finden.
     *
     * @param nameServer Das stellt den Name des Servers.
     * @return Das stellt das Port vom eingegebenen Server.
     */
    public static int getPortfromFile(String nameServer) {
        while(serverInfo.size() != 0) {
            serverInfo.remove(0);
        }

        readServer();

        for(int i = 0; i < serverInfo.size(); i++) {
            if(serverInfo.get(i).contains(nameServer)) {
                String[] item = serverInfo.get(i).split(",");
                return Integer.parseInt(item[0]);
            }
        }

        return 0;
    }

    /**
     * Mit dem Name des Servers kann man seine Capacity finden.
     *
     * @param nameServer Das stellt den Name des Servers.
     * @return Das stellt die Capacity vom eingegebenen Server.
     */
    public static int getCapacityfromFile(String nameServer) {
        while(serverInfo.size() != 0) {
            serverInfo.remove(0);
        }

        readServer();

        for(int i = 0; i < serverInfo.size(); i++) {
            if(serverInfo.get(i).contains(nameServer)) {
                String[] item = serverInfo.get(i).split(",");
                return Integer.parseInt(item[3]);
            }
        }

        return 0;
    }

    /**
     * Die Info des Servers wird aus der Datei Servers.txt gelöscht.
     * @param server Das stellt den Server, der gelöscht wird.
     */
    public static void deleteServer(Server server) {
        while(serverInfo.size() != 0) {
            serverInfo.remove(0);
        }

        readServer();
        deleteFile();

        for(int i = 0; i < serverInfo.size() - 1; i++) {
            if(!serverInfo.get(i).contains(Integer.toString(server.port) + "," + server.serverName + "," + server.serverPass)) {
                FileWriter writer;
                try {
                    writer = new FileWriter(serverInfoFile,true);
                    writer.write(serverInfo.get(i) + "\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Die Datei Servers.txt wird gelöscht.
     */
    public static void deleteFile() {
        serverInfoFile.delete();
    }

    /**
     * Konstruktor Methode für den Server ohne Passwort.
     *
     * @param serverName Das stellt den Name des Servers dar.
     * @param serverCapacity Das stellt die Capacity des Servers dar.
     * @throws IOException
     */
    public Server(String serverName,int serverCapacity) throws IOException {
        this.port = Port.getPort();
        this.sockets = new ArrayList<Socket>();
        this.serverName = serverName;
        this.serverPass = null;
        this.serverCapacity = serverCapacity;
        addServerInfoToFile(this);
        Port.deletePort(Integer.toString(this.port));
        preStart();
        registerLogin();
        start();
    }

    /**
     * Konstruktor Methode für den Server mit einem Passwort.
     *
     * @param serverName Das stellt den Name des Servers dar.
     * @param serverPass Das stellt das Passwort des Servers dar.
     * @param serverCapacity Das stellt die Capacity des Servers dar.
     * @throws IOException
     */
    public Server(String serverName, String serverPass, int serverCapacity) throws IOException {
        this.port = Port.getPort();
        this.sockets = new ArrayList<Socket>();
        this.serverName = serverName;
        this.serverPass = serverPass;
        this.serverCapacity = serverCapacity;
        addServerInfoToFile(this);
        Port.deletePort(Integer.toString(this.port));
        preStart();
        registerLogin();
        start();
    }

    /**
     * Im Hintergrund wird immer eine Thread laufen lassen, damit der Server immer alive bleibt.
     */
    private void startPingThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(server != null){
                    try {
                        Thread.sleep(pingInterval);
                        System.out.println("[Server] Ping");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Der Server wird erstellt. Dann werden die Methode startListening und startPingThread gestartet.
     */
    private void start(){
        try {
            server = new ServerSocket(port);
            startPingThread();
            startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Der Server wird beendet. Server Info wird aus der Datei Servers.txt gelöscht.
     * Port des Servers wird wieder zur Datei ports.txt hinzugefügt.
     */
    public void close(){
        Port.addPort(Integer.toString(this.port));
        deleteServer(this);
        try {
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listening wird gestartet. Server wartet auf Output des Clients.
     */
    private void startListening(){
        if (this.listeninThread == null && this.server != null){
            listeninThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!Thread.interrupted() && server != null){

                        try {
                            System.out.println("[Server] Waiting for connection..");
                            final Socket tSocket = server.accept();

                            // Read message from clients
                            ObjectInputStream out = new ObjectInputStream(new BufferedInputStream(tSocket.getInputStream()));
                            Object ob = out.readObject();

                            if (ob instanceof Datapackage){
                                final Datapackage msg = (Datapackage) ob;
                                System.out.println("[Server] Message received: " + msg.getMessage());

                                for(final String aktuel : listenMethods.keySet()){
                                    if (aktuel.equals(msg.getBefehl())){
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                listenMethods.get(aktuel).listen(msg, tSocket);
                                            }
                                        }).start();
                                        break;
                                    }
                                }
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
            listeninThread.start();
        }
    }

    /**
     * Bevor der Server gestartet wird, wird es ausgeführt, damit alle Methode, die auf dem Output von den Clients reagieren,
     * geladen werden. Diese Methode werden unter dem Variable listenMethods gespeichert.
     * Um eine Methode zu hinzufügen, muss die Methode registerMethode(String befehl, Execute execute) unter dieser Methode aufgerufen werden.
     */
    public void preStart() {
        // listening methods müssen hier implementiert werden.

        /**
         * Wenn ein Player auf dem Chat eine Message schickt, wird diese Methode ausgeführt.
         */
        registerMethod("message_from_client_to_server", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                Datapackage data = new Datapackage(serverName, true, "add_message", msg.getName() + ": " + msg.getMessage());
                sendMessage(data);
            }
        });

        /**
         * Wenn Player 1 eine Card abziehen möchte, wird diese Methode ausgeführt.
         */
        registerMethod("0draw_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if(connectionController.gameController.getCurrentPlayer() == 0){
                    connectionController.takeAction(-1);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 2 eine Card abziehen möchte, wird diese Methode ausgeführt.
         */
        registerMethod("1draw_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if(connectionController.gameController.getCurrentPlayer() == 1){
                    connectionController.takeAction(-1);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 3 eine Card abziehen möchte, wird diese Methode ausgeführt.
         */
        registerMethod("2draw_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if(connectionController.gameController.getCurrentPlayer() == 2){
                    connectionController.takeAction(-1);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 4 eine Card abziehen möchte, wird diese Methode ausgeführt.
         */
        registerMethod("3draw_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if(connectionController.gameController.getCurrentPlayer() == 3){
                    connectionController.takeAction(-1);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 1 eine Card spielen möchte, wird es ausgeführt.
         */
        registerMethod("0play_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                String[] str = msg.getMessage().split(",");
                String card = str[0];
                int index = Integer.parseInt(str[1]);
                if(connectionController.gameController.getCurrentPlayer() == 0){
                    connectionController.takeAction(index);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 2 eine Card spielen möchte, wird es ausgeführt.
         */
        registerMethod("1play_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                String[] str = msg.getMessage().split(",");
                String card = str[0];
                int index = Integer.parseInt(str[1]);
                if(connectionController.gameController.getCurrentPlayer() == 1){
                    connectionController.takeAction(index);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 3 eine Card spielen möchte, wird es ausgeführt.
         */
        registerMethod("2play_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                String[] str = msg.getMessage().split(",");
                String card = str[0];
                int index = Integer.parseInt(str[1]);
                if(connectionController.gameController.getCurrentPlayer() == 2){
                    connectionController.takeAction(index);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 4 eine Card spielen möchte, wird es ausgeführt.
         */
        registerMethod("3play_card", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                String[] str = msg.getMessage().split(",");
                String card = str[0];
                int index = Integer.parseInt(str[1]);
                if(connectionController.gameController.getCurrentPlayer() == 3){
                    connectionController.takeAction(index);
                    changeTurn();
                } else {
                    // TODO
                }
            }
        });

        /**
         * Wenn Player 1 mau machen möchte, wird es ausgeführt.
         */
        registerMethod("0mau", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if (connectionController.gameController.getCurrentPlayer() == 0){
                    connectionController.takeAction(-2);
                } else {
                    // TODO
                }

            }
        });

        /**
         * Wenn Player 2 mau machen möchte, wird es ausgeführt.
         */
        registerMethod("1mau", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if (connectionController.gameController.getCurrentPlayer() == 1){
                    connectionController.takeAction(-2);
                    changeTurn();
                } else {
                    // TODO
                }

            }
        });

        /**
         * Wenn Player 3 mau machen möchte, wird es ausgeführt.
         */
        registerMethod("2mau", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if (connectionController.gameController.getCurrentPlayer() == 2){
                    connectionController.takeAction(-2);
                    changeTurn();
                } else {
                    // TODO
                }

            }
        });

        /**
         * Wenn Player 4 mau machen möchte, wird es ausgeführt.
         */
        registerMethod("3mau", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if (connectionController.gameController.getCurrentPlayer() == 3){
                    connectionController.takeAction(-2);
                    changeTurn();
                } else {
                    // TODO
                }

            }
        });

        /**
         * Nachdem ein Player Cardnr. 11 gespielt hat, wird es ausgeführt.
         */
        registerMethod("set_wish", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                if (msg.getMessage().equals("spade")){
                    color = Color.SPADE;
                } else if (msg.getMessage().equals("heart")){
                    color = Color.HEART;
                } else if (msg.getMessage().equals("diamond")){
                    color = Color.DIAMOND;
                } else {
                    color = Color.CLUB;
                }
                changeTurn();
            }
        });

    }

    /**
     * Wenn ein Output des Clients an Server angekommen ist, wird der Server nach entsprechenden Befehls reagieren.
     *
     * z.B wenn das Output des Clients als Befehl say_hello ist und wir möchten darauf reagieren,
     * muss man hier registerMethod("say_hello", Execute execute) nutzen.
     * Unter dem Variable execute kann man eine Methode implementieren.
     *
     * @param befehl Das stellt das Befehl-Output des Clients dar.
     * @param execute Hier implementiert man die Methode, die von dem Server ausgeführt wird.
     */
    public void registerMethod(String befehl, Execute execute){
        if (befehl.equalsIgnoreCase("LOGIN")){
            throw new IllegalArgumentException("Befehl: " + befehl + " ist nicht erlaubt.");
        }
        listenMethods.put(befehl, execute);
    }

    /**
     * Nach der Connetion zwischen Server und Client wird es ausgeführt.
     * Server wird die ID vom Client aktualisieren.
     * Socket vom Client wird gespeichert.
     * Die Methode startGame() wird aufgerufen.
     */
    public void registerLogin(){
        listenMethods.put("LOGIN", new Execute() {
            @Override
            public void listen(Datapackage msg, Socket socket) {
                System.out.println("[Server] Befehl from Client: " + msg.getBefehl() + " Client Name:" + msg.getName() + " Message from client: " + msg.getMessage() + " client socket: " + socket.getInetAddress());
                Datapackage data = new Datapackage(serverName, true, "add_message", msg.getName() + " is connected.");
                sendMessage(data);
                Datapackage sendId = new Datapackage(serverName, true, "set_id", Integer.toString(clientId));
                sockets.add(socket);
                sendMessage(sendId, clientId);
                clientId++;
                startGame();
            }
        });
    }

    /**
     * Es prüft, ob der Server voll ist. Wenn ja, wird Game gestarted. Wenn nein, passiert nichts.
     * Diese Methode wird nach jeder Connection zwischen Client und Server von der Methode registerLogin aufgerufen.
     */
    private void startGame(){
        if(this.sockets.size() == this.serverCapacity){
            System.out.println("[Server] Game started");

            this.isGameStarted = true;
            this.connectionController = new ConnectionController(this, serverCapacity);

            Datapackage data = new Datapackage(serverName, true, "start_game", "Game started.");
            sendMessage(data);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Datapackage data2 = new Datapackage(serverName, true, "chance_turn", "Your turn.");
            sendMessage(data2, 0);
            changeTurn();
        }
    }

    /**
     * Cards werden durch diese Methode hinzugefügt. Eingegebene Card wird zur Zone vom eingegeben Player hinzugefügt.
     *
     * @param player Das stellt die Index vom Player dar.
     * @param card Das stellt die Card dar.
     */
    public void addCard(int player, String card){
        Datapackage cardInfo = new Datapackage(serverName, true, "add_your_card", card);
        Datapackage cardBack = new Datapackage(serverName, true, "add_other_card", Integer.toString(player));
        try {


            if (player == 0) {
                sendMessage(cardInfo, 0);
                sendMessage(cardBack, 1);
                sendMessage(cardBack, 2);
                sendMessage(cardBack, 3);
            } else if (player == 1) {
                sendMessage(cardBack, 0);
                sendMessage(cardInfo, 1);
                sendMessage(cardBack, 2);
                sendMessage(cardBack, 3);
            } else if (player == 2) {
                sendMessage(cardBack, 0);
                sendMessage(cardBack, 1);
                sendMessage(cardInfo, 2);
                sendMessage(cardBack, 3);
            } else if (player == 3){
                sendMessage(cardBack, 0);
                sendMessage(cardBack, 1);
                sendMessage(cardBack, 2);
                sendMessage(cardInfo, 3);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Cards werden durch diese Methode gelöscht. Eingegebene Card wird von der Zone bom eingegebenen Player gelöscht.
     *
     * @param player
     * @param card
     */
    public void removeCard(int player, String card){
        Datapackage cardInfo = new Datapackage(serverName, true, "remove_your_card", card);
        Datapackage cardBack = new Datapackage(serverName, true, "remove_other_card", Integer.toString(player));
        try {


            if (player == 0) {
                sendMessage(cardInfo, 0);
                Thread.sleep(100);
                sendMessage(cardBack, 1);
                Thread.sleep(100);
                sendMessage(cardBack, 2);
                Thread.sleep(100);
                sendMessage(cardBack, 3);
                Thread.sleep(100);
            } else if (player == 1) {
                sendMessage(cardBack, 0);
                Thread.sleep(100);
                sendMessage(cardInfo, 1);
                Thread.sleep(100);
                sendMessage(cardBack, 2);
                Thread.sleep(100);
                sendMessage(cardBack, 3);
                Thread.sleep(100);
            } else if (player == 2) {
                sendMessage(cardBack, 0);
                Thread.sleep(100);
                sendMessage(cardBack, 1);
                Thread.sleep(100);
                sendMessage(cardInfo, 2);
                Thread.sleep(100);
                sendMessage(cardBack, 3);
                Thread.sleep(100);
            } else {
                sendMessage(cardBack, 0);
                Thread.sleep(100);
                sendMessage(cardBack, 1);
                Thread.sleep(100);
                sendMessage(cardBack, 2);
                Thread.sleep(100);
                sendMessage(cardInfo, 3);
                Thread.sleep(100);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Diese Methode prüft, wer dran ist und dann aktualisiert die Zeiger von GUI.
     */
    public void changeTurn(){
        for(int i = 0; i < serverCapacity; i++){
            if(i != this.connectionController.gameController.getCurrentPlayer()){
                Datapackage data = new Datapackage(serverName, true, "change_turn", "false");
                sendMessage(data, i);
            } else {
                Datapackage data = new Datapackage(serverName, true, "change_turn", "true");
                sendMessage(data, i);
            }

        }
    }

    /**
     * Es zeigt eingegebene Notification für eingegenen Player an. Notification wird nach 10 Sekunden automatisch gelöscht.
     *
     * @param msg Das stellt die Message dar.
     * @param player Das stellt den Player dar.
     */
    public void setNotification(String msg, int player){
        Datapackage data = new Datapackage(serverName, true, "set_notification", msg);
        sendMessage(data, player);
    }

    /**
     * Diese Methode ersetzt die oberste Card durch eingegebene Card.
     * @param card Das stellt die neue oberste Card dar.
     */
    public void chancePlayedCard(String card){
        Datapackage cardInfo = new Datapackage(serverName, true, "chance_played_card", card);
        sendMessage(cardInfo);
    }

    /**
     *
     * @param isPlayer1
     */
    public void closeWishTab(int player){
        Datapackage data = new Datapackage(serverName, true, "close_wish_tab", "egal");
        sendMessage(data, player);
    }

    public void win(int playerIndex){
        // TODO
        for(int i = 0; i < serverCapacity; i++){
            if(i == connectionController.gameController.getCurrentPlayer()){
                setNotification("win", i); // winner
            } else {
                setNotification("lose", i); // loser
            }
        }
    }

    public Color takeWish(){
        return color;
    }

    /**
     * Diese Methode sendet eingebebene Datapackage an die allen Players.
     *
     * @param msg Das stellt die Datapackage dar.
     */
    private void sendMessage(Datapackage msg){
        try{
            for(Socket aktuell : sockets){
                System.out.println("[Client] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(aktuell.getOutputStream()));
                out.writeObject(msg);
                out.flush();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode sendet eingebebene Datapackage an eingegebenem Player.
     *
     * @param msg Das stellt die Datapackage dar.
     * @param player Das stellt den Player dar.
     */
    private void sendMessage(Datapackage msg, int player){
        try{
            System.out.println("[Client] " + msg.getBefehl() + " " + msg.getName() + " " + msg.getMessage());
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(sockets.get(player).getOutputStream()));
            out.writeObject(msg);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
