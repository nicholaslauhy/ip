package lumi.task;

/**
 * Represents a simple task without any associated date or time.
 * <p>
 * A {@code Todo} task contains only a description and a completion status,
 * both inherited from {@link Task}.
 */
public class Todo extends Task {
    /**
     * Constructs a todo task with the given description.
     * <p>
     * The task is initially marked as not done.
     * @param description Description text of the task
     */
    public Todo(String description){
        super(description);
    }

    /**
     * Returns the task type icon for display purposes.
     * @return "[T]" indicating a todo task
     */
    @Override
    public String getTaskTypeIcon(){
        return "[T]";
    }

    /**
     * Returns the serialized representation of this task for storage.
     * <p>
     * Format:
     * <pre>
     * T | doneFlag | description
     * </pre>
     * where {@code doneFlag} is {@code 1} if completed and {@code 0} otherwise.
     * @return Storage-formatted string for this task
     */
    @Override
    public String toStorageString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
