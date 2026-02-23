package lumi.task;

import java.util.ArrayList;
import java.util.List;
import lumi.exception.LumiException;

public class TaskList {
    private static final ArrayList<Task> tasks = new ArrayList<>();

    // add task into list of tasks
    public void add(Task task){
        tasks.add(task);
    }

    // get task
    public Task get(int oneBasedTaskNumber) throws LumiException{
        int index = oneBasedTaskNumber - 1;

        if (index < 0 || index >= tasks.size()){
            throw new LumiException("What invisible task are you taking?? Stop pretending like you have alot of tasks... Lumi is watching!!");
        }
        return tasks.get(index);

    }

    // delete the tasks from the list
    public Task delete(int oneBasedTaskNumber) throws LumiException {
        int index = oneBasedTaskNumber - 1;

        if (index < 0 || index >= tasks.size()){
            throw new LumiException("GAAAHHH what imaginative task are you doing!! NUMBER NON-EXISTENT");
        }
        return tasks.remove(index);
    }

    // for find
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

    // get size of list
    public int size(){
        return tasks.size();
    }

    // count remaining tasks
    public int countRemaining(){
        int remaining = 0;
        for (Task t: tasks){
            if (!t.isDone()) {
                remaining += 1;
            }
        }
        return remaining;
    }

    // get all tasks
    public ArrayList<Task> all(){
        return tasks;
    }
}
