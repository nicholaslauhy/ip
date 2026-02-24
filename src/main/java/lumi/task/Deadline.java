package lumi.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a specific deadline.
 * <p>
 * A {@code Deadline} task has a description (inherited from {@link Task})
 * and a required due date/time indicating when the task should be completed.
 */
public class Deadline extends Task {

    /**
     * Date and time by which the task should be completed.
     */
    protected LocalDateTime by;

    /**
     * Formatter used for user-facing output of the deadline date/time.
     * <p>
     * Example format: {@code Oct 15 2019, 6:00PM}
     */
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructs a deadline task with a description and due date/time.
     *
     * @param description Task description text
     * @param by Due date/time of the task
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date/time of this deadline task.
     *
     * @return Deadline date/time
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the task type icon for display purposes.
     *
     * @return "[D]" indicating a deadline task
     */
    @Override
    public String getTaskTypeIcon() {
        return "[D]";
    }

    /**
     * Returns string representation of this deadline task
     * <p>
     * The date/time is formatted using a human-readable pattern
     * @return Formatted task string for display
     */
    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Returns the representation of this task for storage
     * <p>
     * THe date/time is stored using {@link LocalDateTime#toString()}
     * which produces ISO-8601 format for reliable parsing
     * @return Storage-formatted string for this task
     */
    @Override
    public String toStorageString(){
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
