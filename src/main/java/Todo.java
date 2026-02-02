public class Todo extends Task{
    // constructor
    public Todo(String description){
        // take from parent class
        super(description);
    }

    // override previous declared "[?]"
    @Override
    public String getTaskTypeIcon(){
        return "[T]";
    }
}
