package main.java.de.uni_hannover.hci.maumau.model;

public enum Color {
    SPADE,
    CLUB,
    DIAMOND,
    HEART;

    /**
     * Override of toString to match the servers logic
     * @return s - short string representation of Color (Spade-> s)
     */
    @Override
    public String toString(){
        String s = "";
        switch(this){
            case SPADE:{
                s = "s";
                break;
            }
            case CLUB:{
                s = "c";
                break;
            }
            case DIAMOND:{
                s = "d";
                break;
            }
            case HEART:{
                s = "h";
                break;
            }
            default:
                s = "?";
        }
        return s;
    }
}

