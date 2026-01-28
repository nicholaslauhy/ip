public class Lumi {
    // create a list of introductions that Lumi can use instead
    private static final String[] INTROS = {
        "Hey! Lumi here, What can I help you with?",
        "There is no ME in LUMI. How can I help you?",
        "Yo! Lumi checking in on you what's up man!",
        "I am LUMIIII, ready when you are!"
    };

    // method to call introduction
    public static String getIntro(){
        int idx = (int)(Math.random() * INTROS.length);
        return INTROS[idx];
    }

    // create a list of goodbye that Lumi can use instead
    private static final String[] GOODBYE = {
        "GoodBye.. more like GoodByte",
        "You're leaving already??? Without LUMI??",
        "I will not miss you. Goodbye.",
        "BYEBYE Have a LUMInous DAY!",
        "Logging off... LUMI STYLE!",
        "Session ended womp womp...Lumi shutting down."
    };

    // method to call goodbye
    public static String getGoodBye(){
        int idx = (int)(Math.random() * GOODBYE.length);
        return GOODBYE[idx];
    }
    public static void main(String[] args) {
        String logo =
                  " _      _   _   __  __ ___ \n"
                + "| |    | | | | |  \\/  |_ _|\n"
                + "| |    | | | | | |\\/| || | \n"
                + "| |___ | |_| | | |  | || | \n"
                + "|_____| \\___/  |_|  |_|___|\n";
        System.out.println("Hello I am\n" + logo);
        System.out.println(getIntro());
        System.out.println();
        System.out.println(getGoodBye());
    }
}
