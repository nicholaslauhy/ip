package lumi.parser;

public class ParsedCommand {
    public enum Type {
        BYE,
        LIST,
        HELP,
        TODO,
        DEADLINE,
        EVENT,
        MARK,
        UNMARK,
        DELETE
    }

    public final Type type;
    public final String desc;
    public final String by;
    public final String from;
    public final String to;

    public final Integer taskNumber;

    private ParsedCommand(Type type, String desc, String by, String from, String to, Integer taskNumber){
        this.type = type;
        this.desc = desc;
        this.by = by;
        this.from = from;
        this.to = to;
        this.taskNumber = taskNumber;
    }

    // commands like help, list that only has the type
    public static ParsedCommand simple(Type type){
        return new ParsedCommand(type, null, null, null, null, null);
    }

    // todo command
    public static ParsedCommand todo(String desc){
        return new ParsedCommand(Type.TODO, desc, null, null, null, null);
    }

    // deadline command
    public static ParsedCommand deadline(String desc, String by){
        return new ParsedCommand(Type.DEADLINE, desc, by, null, null, null);
    }

    // event command
    public static ParsedCommand event(String desc, String from, String to){
        return new ParsedCommand(Type.EVENT, desc, null, from, to, null);
    }

    // commands like mark/unmark, delete
    public static ParsedCommand indexed(Type type, int taskNumber){
        return new ParsedCommand(type, null, null, null, null, taskNumber);
    }
}
