package lumi.exception;
/**
 * Represents application-specific errors in the Lumi task manager
 * <p>
 * This exception is thrown when a user command is invalid,
 * violates task state rules (ie marking an already completed task),
 * or when an operation such as storage saving fails.
 * <p>
 * Using a custom exception allows Lumi to distinguish domain errors
 * from system-level exceptions and display user-friendly error messages.
 */
public class LumiException extends Exception {
    /**
     * Constructs a LumiException with a custom and descriptive error message
     * @param message Explanation of what went wrong, displayed on interface for user
     */
    public LumiException(String message){
        super(message);
    }
}
