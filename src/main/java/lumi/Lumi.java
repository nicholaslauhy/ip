package lumi;
import lumi.exception.LumiException;
import lumi.task.Todo;
import lumi.task.Deadline;
import lumi.task.Event;
import lumi.task.Task;
import lumi.task.TaskList;
import lumi.storage.Storage;
import lumi.parser.Parser;
import lumi.parser.ParsedCommand;

import java.util.Scanner;
public class Lumi {
    // =================== TEST MODE ====================
    private static boolean isTestMode = false;

    // ===================== HELP =======================
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
    // =================== CONSTANTS ====================
    // create divider
    private static final String DIVIDER =
            "____________________________________________________________";

    // max number of tasks
    public static final int LOW_TASK_THRESHOLD = 9;
    public static final int MID_TASK_THRESHOLD = 30;

    // new TaskList collection
    private static TaskList taskList = new TaskList();

    // storage filepath
    private static Storage storage;

    // ==================== INTROS / GOODBYE ====================
    // create a list of introductions that Lumi can use instead
    private static final String[] INTROS = {
            "Hey! Lumi here, What can I help you with?",
            "There is no ME in LUMI. How can I help you?",
            "Yo! Lumi checking in on you what's up man!",
            "I am LUMIIII, ready when you are!"
    };

    // create a list of goodbye that Lumi can use instead
    private static final String[] GOODBYE = {
            "GoodBye.. more like GoodByte",
            "You're leaving already??? Without LUMI??",
            "I will not miss you. Goodbye.",
            "BYEBYE Have a LUMInous DAY!",
            "Logging off... LUMI STYLE!",
            "Session ended womp womp...Lumi shutting down."
    };

    // method to call introduction
    public static String getIntro(){
        // for test mode
        if (isTestMode){
            return INTROS[0];
        }
        // for actual use
        int idx = (int)(Math.random() * INTROS.length);
        return INTROS[idx];
    }

    // method to call goodbye
    public static String getGoodBye(){
        // for test mode
        if (isTestMode) {
            return GOODBYE[0];
        }
        // for actual use
        int idx = (int)(Math.random() * GOODBYE.length);
        return GOODBYE[idx];
    }

    // ================= TASKS METHODS ====================
    // Count remaining tasks (done vs not done in list)
    private static int countRemainingTasks(){
        return taskList.countRemaining();
    }

    // add task method
    private static void addTask(Task task) throws LumiException{
        // add task and save in storage
        taskList.add(task);
        storage.save(taskList);

        int taskCount = taskList.size();

        System.out.println(DIVIDER);
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
        System.out.println(DIVIDER);
    }

    // print out the tasks in the list
    private static void printList() {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + taskList.all().get(i));
        }
        System.out.println(DIVIDER);
    }

    // ====================== MAIN =========================
    public static void main(String[] args) {
        // if test mode add --test flag
        isTestMode = args.length > 0 && args[0].equals("--test");
        storage = isTestMode
                ? new Storage("./data/lumi-test.txt")
                : new Storage("./data/lumi.txt");

        // For user input
        Scanner in = new Scanner(System.in);

        try {
            taskList = storage.load();
        } catch (LumiException e){
            taskList = new TaskList();
            System.out.println(DIVIDER);
            System.out.println("LUMI CANNOT SAVE YOUR TASKS!! Reeeeeestarting!!");
            System.out.println(DIVIDER);
        }

        // Print Lumi greeting
        String name = askName(in);
        printGreeting(name);

        // "Tasks for... " loop
        runLoop(in, name);

        in.close();
    }

    // ================= HELPER METHODS ====================
    private static String askName(Scanner in){
        System.out.println("I am your Task Manager! What's your name?");
        String name = in.nextLine().trim();
        return name.isEmpty() ? "You" : name;
    }

    // LUMI in animated characters
    private static void printGreeting(String name){
        String logo = """
         _      _   _   __  __   ___
        | |    | | | | |  \\/  | |_ _|
        | |    | | | | | |\\/| |  | |
        | |___ | |_| | | |  | |  | |
        |_____| \\___/  |_|  |_| |___|
        """;

        System.out.println("Hello " + name.toUpperCase() + ", I am\n" + logo);
        System.out.println(getIntro());
        System.out.println(DIVIDER);
    }

    // print this till bye command given, then will exit
    private static void runLoop(Scanner in, String name){
        while (true){
            System.out.println("Tasks for " + name + ":");
            String userInput = in.nextLine();
            //String input = userInput.trim().toLowerCase();

            try {
                boolean shouldExit = handleCommand(userInput);
                if (shouldExit) {
                    break;
                }
            }
            catch (LumiException e) {
                System.out.println(DIVIDER);
                System.out.println(e.getMessage());
                System.out.println(DIVIDER);
            }
        }
    }

    // all new commands
    private static boolean handleCommand(String userInput) throws LumiException{
        ParsedCommand cmd = Parser.parse(userInput);
        // switch cases for simple commands
        switch(cmd.type) {
            case BYE:
                System.out.println(DIVIDER);
                System.out.println("Lumi: " + getGoodBye());
                System.out.println(DIVIDER);
                return true;

            case LIST:
                printList();
                return false;

            case HELP:
                System.out.println(DIVIDER);
                System.out.println(HELP_MESSAGE);
                System.out.println(DIVIDER);
                return false;

            case MARK: {
                int taskNumber = cmd.taskNumber;
                Task task = taskList.get(taskNumber);

                // if already marked
                if (task.isDone()) {
                    throw new LumiException("You are EXTRA! Lumi does NOT like it!!\n" + task);
                }

                // set done and save in storage
                task.setDone(true);
                storage.save(taskList);

                // mark as done and give count
                System.out.println(DIVIDER);
                System.out.println("Good Job! I have marked this task as done:");
                System.out.println(" " + task);
                System.out.println("You have " + countRemainingTasks() + " tasks to go");
                System.out.println(DIVIDER);
                return false;
            }
            case UNMARK: {
                int taskNumber = cmd.taskNumber;
                Task task = taskList.get(taskNumber);

                // already unmarked
                if (!task.isDone()) {
                    throw new LumiException("What are you unmarking?? You are troubling me for nothing!!\n" + task);
                }

                // set done, save in storage
                task.setDone(false);
                storage.save(taskList);

                // unmark task and give count
                System.out.println(DIVIDER);
                System.out.println("Oh no! Let me unmark this for you:");
                System.out.println(" " + task);
                System.out.println("You have " + countRemainingTasks() + " tasks to go");
                System.out.println(DIVIDER);
                return false;
            }
            case DELETE: {
                int taskNumber = cmd.taskNumber;

                // delete task, save in storage
                Task deleted = taskList.delete(taskNumber);
                storage.save(taskList);

                // show count for tasks left in the list - different from countRemainingTasks!!
                System.out.println(DIVIDER);
                System.out.println("Okie go on with your other tasks...");
                System.out.println(" " + deleted);
                System.out.println("You now have " + taskList.size() + " tasks in your list.");
                System.out.println(DIVIDER);
                return false;
            }

            case TODO:
                addTask(new Todo(cmd.desc));
                return false;

            case DEADLINE:
                addTask(new Deadline(cmd.desc, cmd.by));
                return false;

            case EVENT:
                addTask(new Event(cmd.desc, cmd.from, cmd.to));
                return false;

            default:
                // if command is unknown (not any of the mentioned)
                throw new LumiException("""
                AH?? What is THAT?? TRY AGAIN!
                Try these instead: todo, deadline, event, list, mark, unmark, bye, delete, help
                For the syntax and list of commands, you can type 'help' to know everything
                If I don't see any of these, you are toast!!
                """
                );
            }
        }
    }
