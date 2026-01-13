package main.java.de.uni_hannover.hci.maumau.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Die erlaubte Ports wird unter der Datei Ports.txt gespeichert. Diese Datei muss selber erstellt werden. Wenn Datei leer ist,
 * wird neue Servers nicht erstellt. Bei Nutzung von einem Port wird diese Port auf dem Datei gelöscht. Wenn die Method server.close() angerufen wird,
 * wird Port von disem Server wieder zu der Datei hinzugefügt.
 *
 * @author Kaan Kara
 * @version 0.0.2, 04 Juli 2021
 */
public class Port {

    /*
     * Das stellt den Port dar.
     */
    public static int port;

    /**
     * Das stellt das File dar.
     */
    public static File file = new File( "src/main/java/de/uni_hannover/hci/maumau/server/ports.txt");

    /**
     * Das stellt die aktiven Servers dar.
     */
    public static ArrayList<String> ports = new ArrayList<String>();


    /**
     * Das fügt den Port zu der Datei hinzu.
     * @param server stellt den Port dar.
     */
    public static void addPort(String server) {
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.write(server + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Alle aktiven Servers werden auf dem Variable gespeichert.
     */
    public static void readPorts() {
        try {
            Scanner scan = new Scanner(file);

            while(scan.hasNextLine()){
                ports.add(scan.nextLine());
            }

            scan.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Das löscht den Port von dem Server aus der Datei.
     *
     * @param server stellt server dar. Seine Port wird aus der Datei gelöscht.
     */
    public static void deletePort(String server) {
        while(ports.size() != 0) {
            ports.remove(0);
        }
        readPorts();
        deleteFile();
        for(int i = 0; i < ports.size() - 1; i++) {
            if(!ports.get(i).toString().equals(server)) {
                FileWriter writer;
                try {
                    writer = new FileWriter(file,true);
                    writer.write(ports.get(i) + "\n");
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Das gibt einen erlaubten (benutzbar) Port zurück.
     * @return eine int um eine neue Server zu erstellen.
     */
    public static int getPort() {
        readPorts();
        int a = Integer.parseInt(ports.get(0));
        while(ports.size() != 0) {
            ports.remove(0);
        }
        return a;
    }

    /**
     * Das File wird gelöscht.
     */
    public static void deleteFile() {
        file.delete();
    }
}
