package main.java.de.uni_hannover.hci.maumau.lib;

import java.util.ArrayList;

/**
 * Server und Clients kommunizieren miteinander durch Datapackage.
 *
 * @author  Kaan Kara
 * @version 0.0.1, 30 Juni 2021
 */
public class Datapackage extends ArrayList<Object> {

    private String name;
    private boolean isServer;
    private String befehl;
    private String message;

    /**
     * Konstruktor Methode
     *
     * @param name Das stellt den Name von Server oder Client.
     * @param isServer wenn es false ist, wurde diese Datapackage von einem Client erstellt. wenn true, wurde es von einem Server erstell.
     * @param befehl Das ist eine Identifier für listenMethods.
     * @param message Das stellt die Message dar.
     */
    public Datapackage(String name, boolean isServer, String befehl, String message){
        this.name = name;
        this.isServer = isServer;
        this.befehl = befehl;
        this.message = message;
    }

    /**
     * Getter
     *
     * @return Das stell den Name von Client oder Server.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Getter
     *
     * @return Das stell den Name von Client oder Server.
     */
    public String getBefehl(){
        return this.befehl;
    }

    /**
     * Getter
     *
     * @return Das stell den Name von Client oder Server.
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * Getter
     *
     * @return Das stell den Name von Client oder Server.
     */
    public boolean getIsServer(){
        return this.isServer;
    }

}
