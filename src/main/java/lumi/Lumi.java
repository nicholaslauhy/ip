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

public class Lumi {
    // =================== CONSTANTS ====================
    // new TaskList collection
    private static TaskList taskList = new TaskList();

    // storage filepath
    private static Storage storage;

    // call Ui
    private static Ui ui;

    // ================= TASKS METHODS ====================
    // Count remaining tasks (done vs not done in list)
    private static int countRemainingTasks(){
        return taskList.countRemaining();
    }

    // add task method
    private static void addTask(Task task) throws LumiException {
        // add task and save in storage
        taskList.add(task);
        storage.save(taskList);
        ui.showTaskAdded(task, taskList.size());
    }

    // ====================== MAIN =========================
    public static void main(String[] args) {
        // if test mode add --test flag
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

        // "Tasks for... " loop
        runLoop(name);
    }

    // ================= HELPER METHODS ====================
    // print this till bye command given, then will exit
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

    // all new commands
    private static boolean handleCommand(String userInput) throws LumiException{
        ParsedCommand cmd = Parser.parse(userInput);
        // switch cases for simple commands
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

                // if already marked
                if (task.isDone()) {
                    throw new LumiException("You are EXTRA! Lumi does NOT like it!!\n" + task);
                }

                // set done and save in storage
                task.setDone(true);
                storage.save(taskList);

                // mark as done and give count
                ui.showMarked(task, countRemainingTasks());
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

                // mark as done and give count
                ui.showUnmarked(task, countRemainingTasks());
                return false;
            }
            case DELETE: {
                int taskNumber = cmd.taskNumber;

                // delete task, save in storage
                Task deleted = taskList.delete(taskNumber);
                storage.save(taskList);

                // delete and give count
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
