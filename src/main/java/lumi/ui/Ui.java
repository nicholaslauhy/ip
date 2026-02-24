package lumi.ui;

import lumi.task.Task;
import lumi.task.TaskList;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all user interaction for the Lumi task manager.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Read user input from standard input</li>
 *   <li>Print prompts, status messages, and formatted task lists</li>
 *   <li>Provide deterministic output in test mode (no random greetings)</li>
 * </ul>
 * <p>
 * This class does not modify tasks directly; it only displays information and
 * collects user input.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Task count thresholds used to vary the tone of messages after adding tasks.
     */
    public static final int LOW_TASK_THRESHOLD = 9;
    public static final int MID_TASK_THRESHOLD = 30;

    /**
     * Help text shown when the user requests command usage information.
     */
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
            
                9) find - finds descriptions matching the string/substring given
                Format: find <string/substring>
                Example: find book
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

    /**
     * Scanner used to read user input from standard input.
     */
    private final Scanner scanner;

    /**
     * When enabled, disables randomized greetings/farewells for predictable outputs.
     */
    private final boolean isTestMode;

    /**
     * Constructs a UI instance.
     * @param isTestMode Whether deterministic behavior should be enabled for testing
     */
    public Ui(boolean isTestMode){
        this.scanner = new Scanner(System.in);
        this.isTestMode = isTestMode;
    }

    /**
     * Reads a single line of user input.
     * @return Raw input line entered by the user
     */
    public String readCommand(){
        return scanner.nextLine();
    }

    /**
     * Asks the user for their name.
     * <p>
     * If the user provides an empty name, a default placeholder is used.
     * @return User name (never empty)
     */
    public String askName(){
        System.out.println("I am your Task Manager! What's your name?");
        String name = scanner.nextLine().trim();
        return name.isEmpty() ? "You" : name;
    }

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

    /**
     * Prints the startup greeting and ASCII logo.
     * <p>
     * The greeting message is randomized unless test mode is enabled.
     * @param name User name used for greeting
     */
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

    /**
     * Prints a goodbye message and divider lines.
     * <p>
     * The goodbye message is randomized unless test mode is enabled.
     */
    public void showBye(){
        showDivider();
        System.out.println("Lumi: " + getGoodBye());
        showDivider();
    }

    /**
     * Selects a greeting message.
     * <p>
     * In test mode, always returns the first greeting to keep output deterministic.
     * @return Greeting message
     */
    private String getIntro(){
        // for test mode
        if (isTestMode){
            return INTROS[0];
        }
        // for actual use
        int idx = (int)(Math.random() * INTROS.length);
        return INTROS[idx];
    }

    /**
     * Selects a goodbye message.
     * <p>
     * In test mode, always returns the first goodbye message to keep output deterministic.
     *
     * @return Goodbye message
     */
    private String getGoodBye(){
        // for test mode
        if (isTestMode) {
            return GOODBYE[0];
        }
        // for actual use
        int idx = (int)(Math.random() * GOODBYE.length);
        return GOODBYE[idx];
    }

    public void showPrompt(String name){
        System.out.println("Tasks for " + name + ":");
    }

    /**
     * Displays all tasks in the given task list in a numbered format.
     * @param taskList Task list to display
     */
    public void showTaskList(TaskList taskList){
        showDivider();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + taskList.all().get(i));
        }
        showDivider();
    }

    /**
     * Displays feedback after a task is successfully added.
     * <p>
     * Message tone changes depending on the task count thresholds.
     * @param task The task that was added
     * @param taskCount Total number of tasks after adding
     */
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

    /**
     * Displays feedback when a task is marked as done.
     * @param task The task that was marked
     * @param remaining Number of incomplete tasks remaining
     */
    public void showMarked(Task task, int remaining){
        showDivider();
        System.out.println("Good Job! I have marked this task as done:");
        System.out.println(" " + task);
        System.out.println("You have " + remaining + " tasks to go");
        showDivider();
    }

    /**
     * Displays feedback when a task is marked as not done.
     * @param task The task that was unmarked
     * @param remaining Number of incomplete tasks remaining
     */
    public void showUnmarked(Task task, int remaining){
        // unmark task and give count
        showDivider();
        System.out.println("Oh no! Let me unmark this for you:");
        System.out.println(" " + task);
        System.out.println("You have " + remaining + " tasks to go");
        showDivider();
    }

    /**
     * Displays feedback when a task is deleted.
     * @param deleted The task that was removed
     * @param sizeLeft Number of tasks remaining in the list after deletion
     */
    public void showDeleted(Task deleted, int sizeLeft){
        // show count for tasks left in the list - different from countRemainingTasks!!
        showDivider();
        System.out.println("Okie go on with your other tasks...");
        System.out.println(" " + deleted);
        System.out.println("You now have " + sizeLeft + " tasks in your list.");
        showDivider();
    }

    /**
     * Displays the results of a find/search operation.
     * <p>
     * Matches are displayed as a numbered list; if none are found, a message is shown instead.
     * @param keyword Keyword used for searching
     * @param matches List of matching tasks (possibly empty)
     */
    public void showFindResults(String keyword, List<Task> matches) {
        showDivider();
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found for: " + keyword);
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + matches.get(i));
            }
        }
        showDivider();
    }
}
