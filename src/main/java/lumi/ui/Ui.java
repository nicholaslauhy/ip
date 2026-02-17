package lumi.ui;

import lumi.task.Task;
import lumi.task.TaskList;

import java.util.Scanner;

public class Ui {
    private static final String DIVIDER = "____________________________________________________________";

    // max number of tasks
    public static final int LOW_TASK_THRESHOLD = 9;
    public static final int MID_TASK_THRESHOLD = 30;

    private static final String HELP_MESSAGE = """
            Commands you can use:
                1) list - shows all tasks
                Format : list
                Example: list
    
                2) todo - add tasks that do not have deadlines
                Format: todo <description>
                Example: todo borrow book
    
                3) deadline - for tasks that has deadline
                Format: deadline <description> /by <when>
                Example: deadline return book /by tomorrow 6pm
    
                4) event - for events between a certain time frame
                Format: event <description> /from <start> /to <end>
                Example: event meeting /from 2pm /to 3pm
    
                5) mark - to mark a task that has been completed
                Format: mark <task number>
                Example: mark 1
    
                6) unmark - to unmark a task that had not been completed
                            but was accidentally marked
                Format: unmark <task number>
                Example: unmark 2
    
                7) bye - exits the Lumi chatbot
                Format: bye
                Example: bye
    
                8) delete - deletes selected task
                Format: delete <task number>
                Example: delete 2
            """;
    private static final String[] INTROS = {
            "Hey! Lumi here, What can I help you with?",
            "There is no ME in LUMI. How can I help you?",
            "Yo! Lumi checking in on you what's up man!",
            "I am LUMIIII, ready when you are!"
    };

    private static final String[] GOODBYE = {
            "GoodBye.. more like GoodByte",
            "You're leaving already??? Without LUMI??",
            "I will not miss you. Goodbye.",
            "BYEBYE Have a LUMInous DAY!",
            "Logging off... LUMI STYLE!",
            "Session ended womp womp...Lumi shutting down."
    };

    private final Scanner scanner;
    private final boolean isTestMode;

    public Ui(boolean isTestMode){
        this.scanner = new Scanner(System.in);
        this.isTestMode = isTestMode;
    }

    // ===================== INPUT ======================
    public String readCommand(){
        return scanner.nextLine();
    }

    public String askName(){
        System.out.println("I am your Task Manager! What's your name?");
        String name = scanner.nextLine().trim();
        return name.isEmpty() ? "You" : name;
    }

    // ============= COMMON PRINTING ====================
    public void showDivider(){
        System.out.println(DIVIDER);
    }

    public void showError(String msg){
        showDivider();
        System.out.println(msg);
        showDivider();
    }

    public void showHelp(){
        showDivider();
        System.out.println(HELP_MESSAGE);
        showDivider();
    }

    // ================= GREETING =====================
    // LUMI in animated characters
    public void printGreeting(String name){
        String logo = """
         _      _   _   __  __   ___
        | |    | | | | |  \\/  | |_ _|
        | |    | | | | | |\\/| |  | |
        | |___ | |_| | | |  | |  | |
        |_____| \\___/  |_|  |_| |___|
        """;

        System.out.println("Hello " + name.toUpperCase() + ", I am\n" + logo);
        System.out.println(getIntro());
        showDivider();
    }

    public void showBye(){
        showDivider();
        System.out.println("Lumi: " + getGoodBye());
        showDivider();
    }

    private String getIntro(){
        // for test mode
        if (isTestMode){
            return INTROS[0];
        }
        // for actual use
        int idx = (int)(Math.random() * INTROS.length);
        return INTROS[idx];
    }

    private String getGoodBye(){
        // for test mode
        if (isTestMode) {
            return GOODBYE[0];
        }
        // for actual use
        int idx = (int)(Math.random() * GOODBYE.length);
        return GOODBYE[idx];
    }

    // ================= PROMPT ========================
    public void showPrompt(String name){
        System.out.println("Tasks for " + name + ":");
    }

    // =============== LIST DISPLAY ====================
    public void showTaskList(TaskList taskList){
        showDivider();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + taskList.all().get(i));
        }
        showDivider();
    }

    // =========== ADD, MARK, UNMARK, DELETE ===========
    public void showTaskAdded(Task task, int taskCount){
        showDivider();
        if (taskCount >= 1 && taskCount <= LOW_TASK_THRESHOLD){
            System.out.println("Sucks to be youuuu!! NEW TASK FOR YOU!!");
        }
        else if (taskCount > LOW_TASK_THRESHOLD && taskCount <= MID_TASK_THRESHOLD) {
            System.out.println("Wow you're truly locked out grrr");
        }
        else {
            System.out.println("You are hopeless ask your maid to do your tasks for you :p");
        }

        System.out.println(" " + task);

        if (taskCount == 1){
            System.out.println("Now you have " + taskCount + " problem to deal with...");
        }
        else if (taskCount >= 1 && taskCount <= LOW_TASK_THRESHOLD){
            System.out.println("Now you have " + taskCount + " problems to deal with...");
        }
        else if (taskCount > LOW_TASK_THRESHOLD && taskCount <= MID_TASK_THRESHOLD){
            System.out.println("What are you doing?? There are " + taskCount + " tasks left!!");
        }
        else {
            System.out.println("You have too many problems to deal with...you better lock in.");
            System.out.println("These " + taskCount + " tasks are going nowhere!");
        }
        showDivider();
    }

    public void showMarked(Task task, int remaining){
        showDivider();
        System.out.println("Good Job! I have marked this task as done:");
        System.out.println(" " + task);
        System.out.println("You have " + remaining + " tasks to go");
        showDivider();
    }

    public void showUnmarked(Task task, int remaining){
        // unmark task and give count
        showDivider();
        System.out.println("Oh no! Let me unmark this for you:");
        System.out.println(" " + task);
        System.out.println("You have " + remaining + " tasks to go");
        showDivider();
    }

    public void showDeleted(Task deleted, int sizeLeft){
        // show count for tasks left in the list - different from countRemainingTasks!!
        showDivider();
        System.out.println("Okie go on with your other tasks...");
        System.out.println(" " + deleted);
        System.out.println("You now have " + sizeLeft + " tasks in your list.");
        showDivider();
    }

}
