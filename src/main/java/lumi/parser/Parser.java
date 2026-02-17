package lumi.parser;

import lumi.exception.LumiException;

import static lumi.parser.ParsedCommand.Type.*;

public class Parser {

    public static final int CMD_TODO_LENGTH = 5;
    public static final int CMD_MARK_LENGTH = 5;
    public static final int CMD_UNMARK_LENGTH = 7;

    public static ParsedCommand parse(String userInput) throws LumiException {
        String trimmed = userInput.trim();
        String input = trimmed.toLowerCase();

        // simple commands
        switch (input) {
        case "bye": return ParsedCommand.simple(BYE);
        case "list": return ParsedCommand.simple(LIST);
        case "help": return ParsedCommand.simple(HELP);
        }

        if (input.isEmpty()) {
            throw new LumiException("WHAT??? ITS EMPTY???? Give me SOMETHING!!");
        }

        // mark
        if (input.startsWith("mark ")) {
            Integer n = parseTaskNumber(input, CMD_MARK_LENGTH);
            if (n == null) throw new LumiException("LUMI IS ABOUT TO GET ANGRY!! GIVE ME A PROPER NUMBER");
            return ParsedCommand.indexed(MARK, n);
        }

        // unmark
        if (input.startsWith("unmark ")) {
            Integer n = parseTaskNumber(input, CMD_UNMARK_LENGTH);
            if (n == null) throw new LumiException("WHERES THE NUMBER AFT UNMARK?? NOBODY MESSES WITH LUMI");
            return ParsedCommand.indexed(UNMARK, n);
        }

        // todo
        if (input.startsWith("todo ")) {
            String desc = trimmed.substring(CMD_TODO_LENGTH).trim();
            if (desc.isEmpty()) throw new LumiException("WHERES THE DESCRIPTION???");
            return ParsedCommand.todo(desc);
        }

        // deadline
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

        // event
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

        // delete
        if (input.equals("delete")) {
            throw new LumiException("Do you think Lumi is a mind reader?? GIVE ME A TASK NUMBER");
        }
        if (input.startsWith("delete")) {
            Integer n = parseTaskNumber(input, "delete ".length());
            if (n == null) throw new LumiException("*facepalm* HOW CAN YOU SCREW IT UP! The format is: delete <task NUMBER>!!");
            return ParsedCommand.indexed(DELETE, n);
        }

        throw new LumiException("""
        AH?? What is THAT?? TRY AGAIN!
        Try these instead: todo, deadline, event, list, mark, unmark, bye, delete, help
        For the syntax and list of commands, you can type 'help' to know everything
        If I don't see any of these, you are toast!!
        """);
    }

    // keep your 1-based behavior
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
