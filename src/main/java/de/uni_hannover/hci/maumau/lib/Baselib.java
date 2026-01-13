package main.java.de.uni_hannover.hci.maumau.lib;
import java.io.IOException;

/**
 * Die HilfsMethode werden hier implementiert.
 *
 * @author kaan4
 * @version 0.0.1, 13.06.2021
 */
public class Baselib {

    /**
     * Das gibt OS (operating system) zurück als String.
     *
     * für windows -> win
     * für mac -> mac
     * für linux -> linux
     *
     * @return OS vom User
     */
    public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")){
            return "win";
        }
        else if (os.contains("osx")){
            return "mac";
        }
        else if (os.contains("nix") || os.contains("aix") || os.contains("nux")){
            return "linux";
        }
        return os;
    }


    /**
     * Das öffnet eine url auf Chrome. Es funktioniert nur für Windows.
     *
     * @param url
     */
    public static void openBrowser(String url) {

        if(getOS().equals("win")) {
            try {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome " + url});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(getOS().equals("mac")) {

        }else if(getOS().equals("linux")) {

        }else {

        }
    }


    /**
     * Das gibt Youtube Channel von dem Projekt zurück.
     * @return
     */
    public static String getYoutube() {
        return "http://youtube.com";
    }

    /**
     * Das gibt Instagram Channel von dem Projekt zurück.
     * @return
     */
    public static String getInstagram() {
        return "http://instagram.com";
    }

    /**
     * Das gibt Twitter Channel von dem Projekt zurück.
     * @return
     */
    public static String getTwitter() {
        return "http://twitter.com";
    }

    /**
     * Das gibt Facebook Channel von dem Projekt zurück.
     * @return
     */
    public static String getFacebook() {
        return "http://facebook.com";
    }

    public static void main(String[] args) {

    }
}
