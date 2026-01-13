package main.java.de.uni_hannover.hci.maumau.lib;

import java.net.Socket;

/**
 *
 * @author Kaan kara
 * @version 0.0.1, 30 Juni 2021
 */
public interface  Execute {
    public abstract void listen(Datapackage msg, Socket socket);
}
