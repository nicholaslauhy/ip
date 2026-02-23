package lumi.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime by;

    // format of the date time year
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    // constructor
    public Deadline(String description, LocalDateTime by){
        // take from parent class
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy(){
        return by;
    }

    // override previous declared "[?]"
    public String getTaskTypeIcon(){
        return "[D]";
    }

    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public String toStorageString(){
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
