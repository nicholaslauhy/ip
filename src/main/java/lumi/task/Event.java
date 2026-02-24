package lumi.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that spans a time range.
 * <p>
 * An {@code Event} task has a description (inherited from {@link Task}) and a
 * start/end date-time window indicating when the event occurs.
 */
public class Event extends Task {
    /**
     * Start date/time of the event
     */
    protected LocalDateTime from;

    /**
     * End date/time of the event
     */
    protected LocalDateTime to;

    /**
     * Formatter used for user-facing output of the event date/time range
     * <p>
     * Example format: {@code Oct 15 2019, 6:00PM}
     */
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");


    /**
     * Constructs an event task with a description and a time range
     * @param description Task description text
     * @param from Start date/time of the event
     * @param to End date/time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to){
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date/time of the event
     * @return Start date/time
     */
    public LocalDateTime getFrom(){
        return from;
    }

    /**
     * Returns the end date/time of the event
     * @return End date/time
     */
    public LocalDateTime getTo(){
        return to;
    }

    /**
     * Returns the task type icon for display purposes.
     * @return "[E]" indicating an event task
     */
    @Override
    public String getTaskTypeIcon(){
        return "[E]";
    }

    /**
     * Returns the user-facing string representation of this event task
     * <p>
     * The time range is formatted using a human-readable pattern.
     * @return Formatted task string for display
     */
    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description + " (from: " + from.format(OUTPUT_FORMAT) + " to: " + to.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Returns representation of this task for storage
     * <p>
     * Date/time values are stored using {@link LocalDateTime#toString()},
     * which produces ISO-8601 format for reliable parsing
     * @return Storage-formatted string for this task
     */
    @Override
    public String toStorageString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

}
