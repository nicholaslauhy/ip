public class Event extends Task {
    protected String from;
    protected String to;

    // constructor
    public Event(String description, String from, String to){
        super(description);
        this.from = from;
        this.to = to;
    }

    // override previous declared "[?]"
    @Override
    public String getTaskTypeIcon(){
        return "[E]";
    }

    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description + " (from: " + from + " to: " + to + ")";
    }
}
