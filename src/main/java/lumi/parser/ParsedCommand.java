package lumi.parser;

import lumi.exception.LumiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a validated, structured command produced by the parser.
 * <p>
 * A {@code ParsedCommand} is a data object that carries:
 * <ul>
 *   <li>the command {@link Type}</li>
 *   <li>any associated data such as descriptions or date/time ranges</li>
 * </ul>
 * <p>
 * Only a subset of fields is relevant for each command type. Unused fields are {@code null}.
 */
public class ParsedCommand {

    /**
     * Supported command categories understood by Lumi.
     */
    public enum Type {
        /** Terminates the application. */
        BYE,
        /** Displays the current task list. */
        LIST,
        /** Displays help / usage information. */
        HELP,
        /** Adds a todo task. */
        TODO,
        /** Adds a deadline task. */
        DEADLINE,
        /** Adds an event task. */
        EVENT,
        /** Marks a task as done. */
        MARK,
        /** Marks a task as not done. */
        UNMARK,
        /** Deletes a task. */
        DELETE,
        /** Finds tasks containing a keyword. */
        FIND
    }

    /**
     * The type of command to execute.
     */
    public final Type type;

    /**
     * Description text for commands that create tasks (e.g., TODO/DEADLINE/EVENT).
     * <p>
     * This is {@code null} for commands that do not require user-provided descriptions.
     */
    public final String desc;

    /**
     * Deadline date/time for a DEADLINE command.
     * <p>
     * This is {@code null} unless {@link #type} is {@link Type#DEADLINE}.
     */
    public final LocalDateTime by;

    /**
     * Start date/time for an EVENT command.
     * <p>
     * This is {@code null} unless {@link #type} is {@link Type#EVENT}.
     */
    public final LocalDateTime from;

    /**
     * End date/time for an EVENT command.
     * <p>
     * This is {@code null} unless {@link #type} is {@link Type#EVENT}.
     */
    public final LocalDateTime to;

    /**
     * 1-based task index for commands that operate on a specific task
     * (e.g., MARK/UNMARK/DELETE).
     * <p>
     * This is {@code null} for commands that do not address a specific task.
     */
    public final Integer taskNumber;

    /**
     * Keyword used for FIND commands.
     * <p>
     * This is {@code null} unless {@link #type} is {@link Type#FIND}.
     */
    public final String keyword;

    /**
     * Constructs an immutable command object.
     * <p>
     * Prefer using the static factory methods (e.g., {@link #todo(String)})
     * to create instances with the correct field combinations.
     *
     * @param type Command type
     * @param desc Description for task-creation commands (nullable)
     * @param by Deadline date/time (nullable)
     * @param from Event start date/time (nullable)
     * @param to Event end date/time (nullable)
     * @param taskNumber Index for task-targeting commands (nullable)
     * @param keyword Keyword for find commands (nullable)
     */
    private ParsedCommand(Type type, String desc, LocalDateTime by,
                          LocalDateTime from, LocalDateTime to,
                          Integer taskNumber, String keyword) {
        this.type = type;
        this.desc = desc;
        this.by = by;
        this.from = from;
        this.to = to;
        this.taskNumber = taskNumber;
        this.keyword = keyword;
    }

    /**
     * Creates a command that only needs a {@link Type} and no additional data.
     * <p>
     * Used for commands like LIST, HELP, and BYE.
     *
     * @param type Command type
     * @return Parsed command containing only the type
     */
    public static ParsedCommand simple(Type type) {
        return new ParsedCommand(type, null, null, null, null, null, null);
    }

    /**
     * Creates a TODO command.
     *
     * @param desc Task description
     * @return Parsed TODO command
     */
    public static ParsedCommand todo(String desc) {
        return new ParsedCommand(Type.TODO, desc, null, null, null, null, null);
    }

    /**
     * Creates a DEADLINE command by parsing the deadline time string.
     *
     * @param desc Task description
     * @param byString Raw deadline time string provided by the user
     * @return Parsed DEADLINE command
     * @throws LumiException If the deadline string is invalid or cannot be parsed
     */
    public static ParsedCommand deadline(String desc, String byString) throws LumiException {
        try {
            LocalDateTime byDate = DateTimeParser.parseFlexibleDateTime(byString);
            return new ParsedCommand(Type.DEADLINE, desc, byDate, null, null, null, null);
        } catch (DateTimeParseException e) {
            throw new LumiException("Use format yyyy-MM-dd HH:mm (e.g. 2019-10-15 18:00)");
        }
    }

    /**
     * Creates an EVENT command by parsing the start and end time strings.
     * <p>
     * The end time must not be before the start time.
     *
     * @param desc Task description
     * @param fromString Raw start time string provided by the user
     * @param toString Raw end time string provided by the user
     * @return Parsed EVENT command
     * @throws LumiException If either date/time cannot be parsed or if {@code to < from}
     */
    public static ParsedCommand event(String desc, String fromString, String toString)
            throws LumiException {

        LocalDateTime from = DateTimeParser.parseFlexibleDateTime(fromString);
        LocalDateTime to = DateTimeParser.parseFlexibleDateTime(toString);

        if (to.isBefore(from)) {
            throw new LumiException("Event /to cannot be before /from.");
        }

        return new ParsedCommand(Type.EVENT, desc, null, from, to, null, null);
    }

    /**
     * Creates an indexed command that targets a specific task.
     * <p>
     * Used for MARK, UNMARK, and DELETE.
     *
     * @param type Command type
     * @param taskNumber 1-based task index
     * @return Parsed indexed command
     */
    public static ParsedCommand indexed(Type type, int taskNumber) {
        return new ParsedCommand(type, null, null, null, null, taskNumber, null);
    }

    /**
     * Creates a FIND command that searches tasks by keyword.
     *
     * @param keyword Keyword to match against task text
     * @return Parsed FIND command
     */
    public static ParsedCommand find(String keyword) {
        return new ParsedCommand(Type.FIND, null, null, null, null, null, keyword);
    }
}