package lumi.task;

/**
 * Represents a generic task in the Lumi task manager.
 * <p>
 * A {@code Task} contains:
 * <ul>
 *   <li>A textual description</li>
 *   <li>A completion status (done or not done)</li>
 * </ul>
 * <p>
 * This class provides common behavior shared by all task types
 * (e.g., {@link Todo}, {@link Deadline}, {@link Event}) and defines
 * abstract methods that subclasses must implement.
 */
public abstract class Task {
    /**
     * Text description of the task
     */
    protected String description;

    /**
     * Indicates whether the task has been marked as completed
     */
    protected boolean isDone;

    /**
     * Constructs task with the given description
     * <p>
     * Newly created tasks are initially not marked as done
     * @param description Description text of the task
     */
    public Task(String description){
        this.description = description;
        this.isDone =false;
    }

    /**
     * Returns description of the task
     * @return Task description text
     */
    public String getDescription(){
        return description;
    }

    /**
     * Returns whetehr this task has been completed
     * @return {@code true} if completed, {@code false} otherwise
     */
    public boolean isDone(){
        return isDone;
    }

    /**
     * Sets the completion status of this task
     * @param done {@code true} to mark as completed, {@code false} to mark as incomplete
     */
    public void setDone(boolean done){
        this.isDone = done;
    }

    /**
     * Returns the status icon for display purposes
     * <p>
     * {@code [X]} indicates completed; {@code [ ]} indicates incomplete
     * @return Status icon string
     */
    public String getStatusIcon(){
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Returns the task type icon for display purposes.
     * <p>
     * Implementations must return a short identifier such as:
     * <ul>
     *   <li>{@code [T]} for Todo</li>
     *   <li>{@code [D]} for Deadline</li>
     *   <li>{@code [E]} for Event</li>
     * </ul>
     *
     * @return Task type icon string
     */
    public abstract String getTaskTypeIcon();

    /**
     * Returns representation of this task for storage
     * <p>
     * Implementations must follow the format expected by {@link lumi.storage.Storage}
     * @return Storage-formatted string
     */
    public abstract String toStorageString();

    /**
     * Returns the default user-facing string representation of this task.
     * <p>
     * Subclasses may override this to include additional fields (e.g., dates).
     * @return Formatted task string for display
     */
    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description;
    }
}
