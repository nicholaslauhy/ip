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
import lumi.ui.Ui;

import java.util.List;

/**
 * Entry point and main controller for Lumi, my task manager.
 * <p>
 * This class wires together the UI, Storage, Parsing and task list components
 * It is responsible for:
 * <ul>
 *     <li>Initializing dependencies (storage, tasklist)</li>
 *     <li>Loading persisted tasks at startup</li>
 *     <li>Running the input processing loop</li>
 *     <li>Dispatch user commands to appropriate actions</li>
 * </ul>
 */
public class Lumi {
    /**
     * In memory task collection for the current run of the application
     * <p>
     * This is the single source for tasks during execution and is persisted
     * via {@link Storage} after mutating commands
     */
    private static TaskList taskList = new TaskList();

    /**
     * Creates, persists and retrieves tasks from the local filesystem based on a relative path
     * <p>
     * The concrete file path depends on whether the application is started in test mode.
     */
    private static Storage storage;

    /**
     * Handles user interaction such as prompting, reading input, and displaying results
     * <p>
     * All the print statements for each command handled are stored here
     */
    private static Ui ui;

    /**
     * Computes the number of remaining tasks in the current task list not done
     * @return Number of tasks not marked as done
     */
    private static int countRemainingTasks(){
        return taskList.countRemaining();
    }

    /**
     * Adds a task to the list, saves updated list and reports change to the user.
     * @param task Task to add
     * @throws LumiException If saving to storage fails
     */
    private static void addTask(Task task) throws LumiException {
        taskList.add(task);
        storage.save(taskList);
        ui.showTaskAdded(task, taskList.size());
    }

    /**
     * Application entry point
     * <p>
     * Supports a {@code --test} flag to switch to a test storage file and test UI behavior
     * After initialization and loading tasks, it greets the user and starts the handling of commands.
     * @param args Command-line arguments; {@code --test} enables test mode
     */
    public static void main(String[] args) {
        boolean isTestMode = args.length > 0 && args[0].equals("--test");
        storage = isTestMode
                ? new Storage("./data/lumi-test.txt")
                : new Storage("./data/lumi.txt");

        ui = new Ui(isTestMode);

        try {
            taskList = storage.load();
        } catch (LumiException e) {
            taskList = new TaskList();
            ui.showError("LUMI CANNOT SAVE YOUR TASKS!! Reeeeeestarting!!");
        }

        String name = ui.askName();
        ui.printGreeting(name);
        runLoop(name);
    }

    /**
     * Runs the read-evaluate loop until the user issues a termination command like 'bye'
     * <p>
     * This method:
     * <ul>
     *     <li>Prompts for input</li>
     *     <li>Parses and executes the command</li>
     *     <li>Handles any domain errors by showing messages via the UI</li>
     * </ul>
     * @param name The user's name used for personalized prompts
     */
    private static void runLoop(String name){
        while (true){
            ui.showPrompt(name);
            String userInput = ui.readCommand();

            try {
                boolean shouldExit = handleCommand(userInput);
                if (shouldExit) {
                    break;
                }
            }
            catch (LumiException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Parses a raw user input string and executes the corresponding command
     * <p>
     * This method is the main dispatch point for all supported commands.
     * Any valid commands ran will be added to storage to save the user's progress.
     * @param userInput Raw command string entered by the user
     * @return {@code true} if the command requests the application to exit; {@code false} otherwise
     * @throws LumiException If the command is invalid, violates constraints or saving fails
     */
    private static boolean handleCommand(String userInput) throws LumiException{
        ParsedCommand cmd = Parser.parse(userInput);
        switch(cmd.type) {
            case BYE:
                ui.showBye();
                return true;

            case LIST:
                ui.showTaskList(taskList);
                return false;

            case HELP:
                ui.showHelp();
                return false;

            case MARK: {
                int taskNumber = cmd.taskNumber;
                Task task = taskList.get(taskNumber);

                if (task.isDone()) {
                    throw new LumiException("You are EXTRA! Lumi does NOT like it!!\n" + task);
                }

                task.setDone(true);
                storage.save(taskList);
                ui.showMarked(task, countRemainingTasks());
                return false;
            }
            case UNMARK: {
                int taskNumber = cmd.taskNumber;
                Task task = taskList.get(taskNumber);

                if (!task.isDone()) {
                    throw new LumiException("What are you unmarking?? You are troubling me for nothing!!\n" + task);
                }

                task.setDone(false);
                storage.save(taskList);
                ui.showUnmarked(task, countRemainingTasks());
                return false;
            }
            case DELETE: {
                int taskNumber = cmd.taskNumber;
                Task deleted = taskList.delete(taskNumber);
                storage.save(taskList);
                ui.showDeleted(deleted, taskList.size());
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

            case FIND: {
                List<Task> matches = taskList.find(cmd.keyword);
                ui.showFindResults(cmd.keyword, matches);
                return false;
            }

            default:
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
