package lumi.parser;
import lumi.exception.LumiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;


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
    public final LocalDateTime by;
    public final LocalDateTime from;
    public final LocalDateTime to;

    public final Integer taskNumber;

    private ParsedCommand(Type type, String desc, LocalDateTime by, LocalDateTime from, LocalDateTime to, Integer taskNumber){
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
    public static ParsedCommand deadline(String desc, String byString) throws LumiException {
        try {
            LocalDateTime byDate = DateTimeParser.parseFlexibleDateTime(byString);
            return new ParsedCommand(Type.DEADLINE, desc, byDate, null, null, null);
        } catch (DateTimeParseException e) {
            throw new LumiException("Use format yyyy-MM-dd HH:mm (e.g. 2019-10-15 18:00)");
        }
    }

    // event command
    public static ParsedCommand event(String desc, String fromString, String toString)
            throws LumiException {

        LocalDateTime from = DateTimeParser.parseFlexibleDateTime(fromString);
        LocalDateTime to = DateTimeParser.parseFlexibleDateTime(toString);

        if (to.isBefore(from)) {
            throw new LumiException("Event /to cannot be before /from.");
        }

        return new ParsedCommand(Type.EVENT, desc, null, from, to, null);
    }

    // commands like mark/unmark, delete
    public static ParsedCommand indexed(Type type, int taskNumber){
        return new ParsedCommand(type, null, null, null, null, taskNumber);
    }
}
