package lumi.parser;

import lumi.exception.LumiException;

import static lumi.parser.ParsedCommand.Type.*;

/**
 * Parses raw user input strings into structured {@link ParsedCommand} objects
 * <p>
 * This class translates Lumi's command-line syntax into a validated command.
 * It is responsible for:
 * <ul>
 *     <li>Recognising support commands (eg {@code todo}, {@code deadline})</li>
 *     <li>Extracting and validating required arguments (eg descriptions, task indices)</li>
 *     <li>Throwing {@link LumiException} with user-friendly messages when input is invalid</li>
 * </ul>
 * <p>
 * Parsing is case-insensitive for the command keyword itself, but preserves the original
 * user input (via {@code trimmed}) when extracting description and date/time strings.
 */
public class Parser {
    /**
     * Number of characters in the command prefix {@code "todo "}, used for extracting descriptions.
     */
    public static final int CMD_TODO_LENGTH = 5;

    /**
     * Number of characters in the command prefix {@code "mark "}, used for extracting task indices.
     */
    public static final int CMD_MARK_LENGTH = 5;

    /**
     * Number of characters in the command prefix {@code "unmark "}, used for extracting task indices.
     */
    public static final int CMD_UNMARK_LENGTH = 7;

    /**
     * Parses a raw input line into a {@link ParsedCommand}.
     * <p>
     * Supported command families:
     * <ul>
     *   <li>Simple commands: {@code bye}, {@code list}, {@code help}</li>
     *   <li>Indexed commands: {@code mark N}, {@code unmark N}, {@code delete N}</li>
     *   <li>Task creation:
     *     <ul>
     *       <li>{@code todo <desc>}</li>
     *       <li>{@code deadline <desc> /by <when>}</li>
     *       <li>{@code event <desc> /from <start> /to <end>}</li>
     *     </ul>
     *   </li>
     *   <li>Search: {@code find <keyword>}</li>
     * </ul>
     * <p>
     * If the input does not match any supported syntax, this method throws a {@link LumiException}
     * describing valid alternatives.
     *
     * @param userInput Raw command line entered by the user
     * @return Parsed command with extracted arguments
     * @throws LumiException If the input is empty, unknown, or missing/invalid arguments
     */
    public static ParsedCommand parse(String userInput) throws LumiException {
        String trimmed = userInput.trim();
        String input = trimmed.toLowerCase();

        switch (input) {
        case "bye": return ParsedCommand.simple(BYE);
        case "list": return ParsedCommand.simple(LIST);
        case "help": return ParsedCommand.simple(HELP);
        }

        if (input.isEmpty()) {
            throw new LumiException("WHAT??? ITS EMPTY???? Give me SOMETHING!!");
        }

        if (input.startsWith("mark ")) {
            Integer n = parseTaskNumber(input, CMD_MARK_LENGTH);
            if (n == null) throw new LumiException("LUMI IS ABOUT TO GET ANGRY!! GIVE ME A PROPER NUMBER");
            return ParsedCommand.indexed(MARK, n);
        }

        if (input.startsWith("unmark ")) {
            Integer n = parseTaskNumber(input, CMD_UNMARK_LENGTH);
            if (n == null) throw new LumiException("WHERES THE NUMBER AFT UNMARK?? NOBODY MESSES WITH LUMI");
            return ParsedCommand.indexed(UNMARK, n);
        }

        if (input.startsWith("todo ")) {
            String desc = trimmed.substring(CMD_TODO_LENGTH).trim();
            if (desc.isEmpty()) throw new LumiException("WHERES THE DESCRIPTION???");
            return ParsedCommand.todo(desc);
        }

        if (input.equals("deadline") || input.startsWith("deadline ")) {
            int byPos = trimmed.indexOf(" /by ");
            if (byPos == -1) {
                throw new LumiException("Lumi will only guide you ONCE! The format is: deadline <task> /by <when>!! NO MORE!!");
            }

            String desc = trimmed.substring("deadline".length(), byPos).trim();
            String by = trimmed.substring(byPos + " /by ".length()).trim();

            if (desc.isEmpty()) throw new LumiException("Wheres the ACTIVITY?? Format is: deadline <task> /by <when>... LAST CHANCE");
            if (by.isEmpty()) throw new LumiException("By when?? Format is: deadline <task> /by <when>../FOLLOW SIMPLE INSTRUCTIONS GRRR");

            return ParsedCommand.deadline(desc, by);
        }

        if (input.startsWith("event ")) {
            int fromPos = trimmed.indexOf(" /from ");
            int toPos = trimmed.indexOf(" /to ");

            if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                throw new LumiException("I will only repeat this ONCE!! Format is: event <task> /from <start> /to <end>...DONT BLOW IT!");
            }

            String desc = trimmed.substring("event".length(), fromPos).trim();
            String from = trimmed.substring(fromPos + " /from ".length(), toPos).trim();
            String to = trimmed.substring(toPos + " /to ".length()).trim();

            if (desc.isEmpty()) throw new LumiException("What are you DOING?? Event needs a DESCRIPTION!!");
            if (from.isEmpty() || to.isEmpty()) throw new LumiException("Event needs BOTH /from and /to!! GET A GRIP");

            return ParsedCommand.event(desc, from, to);
        }

        if (input.equals("delete")) {
            throw new LumiException("Do you think Lumi is a mind reader?? GIVE ME A TASK NUMBER");
        }
        if (input.startsWith("delete")) {
            Integer n = parseTaskNumber(input, "delete ".length());
            if (n == null) throw new LumiException("*facepalm* HOW CAN YOU SCREW IT UP! The format is: delete <task NUMBER>!!");
            return ParsedCommand.indexed(DELETE, n);
        }

        if (input.startsWith("find ")){
            String keyword = trimmed.substring("find ".length()).trim();
            if (keyword.isEmpty()){
                throw new LumiException("Find?? FIND WHAT?? Lumi is NOT a MIND READER!!");
            }
            return ParsedCommand.find(keyword);
        }

        throw new LumiException("""
        AH?? What is THAT?? TRY AGAIN!
        Try these instead: todo, deadline, event, list, mark, unmark, bye, delete, help
        For the syntax and list of commands, you can type 'help' to know everything
        If I don't see any of these, you are toast!!
        """);
    }

    /**
     * Extracts and parses a task number from an indexed command string.
     * <p>
     * The parser expects the integer to appear after a known prefix length,
     * e.g. for {@code "mark 3"}, the prefix length would be the length of {@code "mark "}.
     * <p>
     * Returns {@code null} when the numeric part is missing or not a valid integer.
     * <p>
     * Note: this method does not enforce bounds against the current task list size;
     * that validation is expected to be handled elsewhere when retrieving the task.
     *
     * @param input Full normalized command input (typically lowercased and trimmed)
     * @param prefixLength Number of characters to skip before reading the integer portion
     * @return Parsed task number, or {@code null} if missing/invalid
     */
    private static Integer parseTaskNumber(String input, int prefixLength) {
        String intPart = input.substring(prefixLength).trim();
        if (intPart.isEmpty()) return null;
        try {
            return Integer.parseInt(intPart);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
