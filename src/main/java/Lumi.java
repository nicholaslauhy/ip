import java.util.Scanner;
public class Lumi {

    // create divider
    private static final String DIVIDER =
            "____________________________________________________________";

    // max number of tasks
    private static final int MAX_TASKS = 100;
    private static final Task[] tasks = new Task[MAX_TASKS];

    // track number of tasks there are
    private static int taskCount = 0;

    // Count remaining tasks
    private static int countRemainingTasks(){
        int remaining = 0;
        for (int i=0; i< taskCount; i+=1){
            if (!tasks[i].isDone()){
                remaining += 1;
            }
        }
        return remaining;
    }

    // print out the tasks in the list
    private static void printList() {
        System.out.println(DIVIDER);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        System.out.println(DIVIDER);
    }

    // check if valid task
    private static boolean isValidTaskIndex(int idx) {
        return idx >= 0 && idx < taskCount;
    }

    // add task method
    private static void addTask(Task task){
        tasks[taskCount] = task;
        taskCount++;

        System.out.println(DIVIDER);
        if (taskCount >= 1 && taskCount <= 9){
            System.out.println("Sucks to be youuuu!! NEW TASK FOR YOU!!");
        }
        else if (taskCount >= 10 && taskCount <= 30){
            System.out.println("Wow you're truly locked out grrr");
        }
        else {
            System.out.println("You are hopeless ask your maid to do your tasks for you :p");
        }

        System.out.println(" " + task);
        if (taskCount == 1){
            System.out.println("Now you have " + taskCount + " problem to deal with...");
        }
        else if (taskCount > 1 && taskCount < 30){
            System.out.println("Now you have " + taskCount + " problems to deal with...");
        }
        else if (taskCount >= 30 && taskCount <= MAX_TASKS){
            System.out.println("You have too many problems to deal with...you better lock in. These " + taskCount + " tasks are going nowhere!");
        }
        else {
            System.out.println("Now you have " + taskCount + " problems to deal with...");
        }
        System.out.println(DIVIDER);
    }

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

            // make everything lower case
            String input = user_input.trim().toLowerCase();

            // Exit condition when user says bye
            if (input.equals("bye")){
                System.out.println(DIVIDER);
                System.out.println("Lumi: " + getGoodBye());
                System.out.println(DIVIDER);
                break;
            }

            // List tasks
            if (input.equals("list")){
                printList();
                continue;
            }

            // Mark task
            if (input.startsWith("mark ")){
                int idx = Integer.parseInt(input.substring(5)) - 1;

                // if invalid task
                if (!isValidTaskIndex(idx)){
                    System.out.println(DIVIDER);
                    System.out.println("You DONUT. That's the wrong NUMBER! GIVE LUMI SOMETHING!!");
                    System.out.println(DIVIDER);
                    continue;
                }
                // if already marked
                if (tasks[idx].isDone()){
                    System.out.println(DIVIDER);
                    System.out.println("You are EXTRA! Lumi does NOT like it!!");
                    System.out.print(" " + tasks[idx]);
                    System.out.println(DIVIDER);
                    continue;
                }

                // mark task
                tasks[idx].setDone(true);

                System.out.println(DIVIDER);
                System.out.println("Good Job! I have marked this task as done:");
                System.out.println(" " + tasks[idx]);
                System.out.println("You have " + countRemainingTasks() + " tasks to go");
                System.out.println(DIVIDER);
                continue;
            }

            // Unmark task
            if (input.startsWith("unmark ")){
                int idx = Integer.parseInt(input.substring(7)) - 1;

                // invalid task number
                if (!isValidTaskIndex(idx)){
                    System.out.println(DIVIDER);
                    System.out.println("Bruhhhh! Nobody gives weird numbers to Lumi!");
                    System.out.println(DIVIDER);
                    continue;
                }

                // already unmarked
                if (!tasks[idx].isDone()){
                    System.out.println(DIVIDER);
                    System.out.println("What are you unmarking?? You are troubling me for nothing!!");
                    System.out.println(" " + tasks[idx]);
                    System.out.println(DIVIDER);
                    continue;
                }

                // unmark task
                tasks[idx].setDone(false);

                System.out.println(DIVIDER);
                System.out.println("Oh no! Let me unmark this for you:");
                System.out.println(" " + tasks[idx]);
                System.out.println("You have " + countRemainingTasks() + " tasks to go");
                System.out.println(DIVIDER);
                continue;
            }

            // Edge cases when adding task
            // 1. If input is empty
            if (input.isEmpty()){
                System.out.println(DIVIDER);
                System.out.println("WHAT??? ITS EMPTY???? Give me SOMETHING!!");
                System.out.println(DIVIDER);
                continue;
            }

            // 2. If input is full
            if (taskCount >= MAX_TASKS){
                System.out.println(DIVIDER);
                System.out.println("Lumi...is FULL!! DELETE DELETE DELETE");
                System.out.println(DIVIDER);
                continue;
            }

            // Todo
            if (input.startsWith("todo ")){
                String desc = user_input.trim().substring(5);
                if (desc.isEmpty()){
                    System.out.println(DIVIDER);
                    System.out.println("WHERES THE DESCRIPTION???");
                    System.out.println(DIVIDER);
                    continue;
                }
                addTask(new Todo(desc));
                continue;
            }

            // Deadline
            if (input.startsWith("deadline ")){
                String raw_text = user_input.trim();
                int byPos = raw_text.indexOf(" /by ");
                // if never adhere to naming
                if (byPos == -1){
                    System.out.println(DIVIDER);
                    System.out.println("Lumi will only guide you ONCE! The format is: deadline <task> /by <when>!! NO MORE!!");
                    System.out.println(DIVIDER);
                    continue;
                }
                // after "deadline "
                String desc = raw_text.substring(9, byPos).trim();
                String by = raw_text.substring(byPos + 5).trim();

                // edge cases
                if (desc.isEmpty() || by.isEmpty()){
                    System.out.println(DIVIDER);
                    System.out.println("I NEED TIME ADDING / AT THE FRONT AND A DESCRIPTION");
                    System.out.println(DIVIDER);
                    continue;
                }
                addTask(new Deadline(desc, by));
                continue;
            }

            // Event
            if (input.startsWith("event ")){
                String raw_input = user_input.trim();
                int fromPos = raw_input.indexOf(" /from ");
                int toPos = raw_input.indexOf(" /to ");

                // edge cases
                if (fromPos == -1 || toPos == -1 || toPos < fromPos){
                    System.out.println(DIVIDER);
                    System.out.println("I will only repeat this ONCE!! Format is: event <task> /from <start> /to <end>...DONT BLOW IT!");
                    System.out.println(DIVIDER);
                    continue;
                }

                String desc = raw_input.substring(6, fromPos).trim();
                String from = raw_input.substring(fromPos + 7, toPos).trim();
                String to = raw_input.substring(toPos + 5).trim();

                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    System.out.println(DIVIDER);
                    System.out.println("An event needs a /FROM, a /TO and a DESCRIPTION!!");
                    System.out.println(DIVIDER);
                    continue;
                }
                addTask(new Event(desc, from, to));
                continue;
            }

            // if command is unknown
            System.out.println(DIVIDER);
            System.out.println("AH?? What is THAT?? TRY AGAIN!");
            System.out.println("Try these instead: todo, deadline, event, list, mark, unmark, bye");
            System.out.println("If I don't see any of these, you are toast!!");
            System.out.println(DIVIDER);
        }
    }
}
