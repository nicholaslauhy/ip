import java.util.Scanner;
public class Lumi {

    // create divider
    private static final String DIVIDER =
            "____________________________________________________________";

    // max number of tasks
    private static final int MAX_TASKS = 100;
    private static final String[] tasks = new String[MAX_TASKS];

    // track number of tasks
    private static int taskCount = 0;


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
        // For user input
        Scanner in = new Scanner(System.in);

        // Ask for user's name
        System.out.print("I am your Task Manager! What's your name? ");
        String name = in.nextLine().trim();

        // If user's name is empty, fallback name
        if (name.isEmpty()) {
            name = "You";
        }

        String logo =
                  " _      _   _   __  __ ___ \n"
                + "| |    | | | | |  \\/  |_ _|\n"
                + "| |    | | | | | |\\/| || | \n"
                + "| |___ | |_| | | |  | || | \n"
                + "|_____| \\___/  |_|  |_|___|\n";
        System.out.println("Hello " + name.toUpperCase() + ", I am\n" + logo);
        System.out.println(getIntro());
        System.out.println(DIVIDER);

        while (true){
            System.out.print("Tasks for " + name + ": ");
            String user_input = in.nextLine();

            // Exit condition when user says bye
            if (user_input.equalsIgnoreCase("bye")){
                System.out.println(DIVIDER);
                System.out.println("Lumi: " + getGoodBye());
                System.out.println(DIVIDER);
                break;
            }

            // List tasks
            if (user_input.equalsIgnoreCase("list")){
                System.out.println(DIVIDER);
                for (int i=0; i< taskCount; i+=1){
                    System.out.println((i+1) + ". " + tasks[i]);
                }
                System.out.println(DIVIDER);
                continue;
            }
            tasks[taskCount] = user_input;
            taskCount++;

            // Echo input
            System.out.println(DIVIDER);
            System.out.println("Lumi added: " + user_input);
            System.out.println(DIVIDER);
        }
    }
}
