package easylog.core;

/**
 * Interface for a log message parser.
 *
 * @since 1.0
 */
public interface LogMessageParser {

    /**
     * Parses a log message.
     *
     * @param scope   the scope object
     * @param message the message text
     * @return the parsed message
     */
    String parse(Scope scope, String message);
}
