public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description){
        this.description = description;
        this.isDone =false;
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

    @Override
    public String toString(){
        return getStatusIcon() + " " + description;
    }
}
