package lumi.task;

import java.util.ArrayList;
import java.util.List;
import lumi.exception.LumiException;

/**
 * Manages a collection of {@link Task} objects.
 * <p>
 * Provides operations for adding, retrieving, deleting, searching,
 * and querying task metadata (e.g., size and remaining count).
 * <p>
 * Task indices exposed to the user are 1-based, while the internal
 * list uses 0-based indexing.
 */
public class TaskList {
    /**
     * Internal storage of tasks.
     * <p>
     * Note: This list is static, meaning all {@code TaskList} instances
     * share the same underlying task collection.
     */
    private static final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the list
     * @param task Task to be added
     */
    public void add(Task task){
        tasks.add(task);
    }

    /**
     * Retrieves a task using a 1-based task number
     * @param oneBasedTaskNumber Task number as shown to the user
     * @return The corresponding {@link Task}
     * @throws LumiException If the task number is out of range
     */
    public Task get(int oneBasedTaskNumber) throws LumiException{
        int index = oneBasedTaskNumber - 1;

        if (index < 0 || index >= tasks.size()){
            throw new LumiException("What invisible task are you taking?? Stop pretending like you have alot of tasks... Lumi is watching!!");
        }
        return tasks.get(index);

    }

    /**
     * Deletes a task using a 1-based task number.
     * @param oneBasedTaskNumber Task number as shown to the user (starting from 1)
     * @return The removed {@link Task}
     * @throws LumiException If the task number is out of range
     */
    public Task delete(int oneBasedTaskNumber) throws LumiException {
        int index = oneBasedTaskNumber - 1;

        if (index < 0 || index >= tasks.size()){
            throw new LumiException("GAAAHHH what imaginative task are you doing!! NUMBER NON-EXISTENT");
        }
        return tasks.remove(index);
    }

    /**
     * Finds tasks whose descriptions contain the given keyword.
     * <p>
     * Matching is case-insensitive and based on substring containment.
     * @param keyword Keyword to search for
     * @return List of matching tasks (possibly empty)
     */
    public List<Task> find(String keyword) {
        String key = keyword.toLowerCase().trim();
        List<Task> matches = new ArrayList<>();

        for (Task t : this.all()) {   // since you already have all()
            String desc = t.getDescription().toLowerCase();
            if (desc.contains(key)) { // ✅ substring match
                matches.add(t);
            }
        }
        return matches;
    }

    /**
     * Returns the total number of tasks in the list.
     * @return Number of tasks
     */
    public int size(){
        return tasks.size();
    }

    /**
     * Counts the number of tasks that are not yet marked as done.
     * @return Number of incomplete tasks
     */
    public int countRemaining(){
        int remaining = 0;
        for (Task t: tasks){
            if (!t.isDone()) {
                remaining += 1;
            }
        }
        return remaining;
    }

    /**
     * Returns the underlying list of tasks.
     * <p>
     * Modifications to the returned list will directly affect the internal state.
     * @return Internal task list
     */
    public ArrayList<Task> all(){
        return tasks;
    }
}
