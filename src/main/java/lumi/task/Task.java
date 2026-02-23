package lumi.task;
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description){
        this.description = description;
        this.isDone =false;
    }

    public String getDescription(){
        return description;
    }

    public boolean isDone(){
        return isDone;
    }

    public void setDone(boolean done){
        this.isDone = done;
    }

    public String getStatusIcon(){
        return (isDone ? "[X]" : "[ ]");
    }

    // used for "T", "D" and "E"
    public abstract String getTaskTypeIcon();

    public abstract String toStorageString();

    @Override
    public String toString(){
        return getTaskTypeIcon() + getStatusIcon() + " " + description;
    }
}
