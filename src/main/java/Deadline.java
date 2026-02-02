public class Deadline extends Task{
    protected String by;

    // constructor
    public Deadline(String description, String by){
        // take from parent class
        super(description);
        this.by = by;
    }

    // override previous declared "[?]"
    public String getTaskTypeIcon(){
        return "[D]";
    }

    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description + " (by: " + by + ")";
    }
}
