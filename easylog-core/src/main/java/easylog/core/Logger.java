package easylog.core;

import java.util.function.Supplier;

/**
 * Logger interface.
 */
public interface Logger {

    /**
     * Get if the specified log level is enabled.
     *
     * @param level the log level
     * @return true if enabled
     */
    boolean isEnabled(LogLevel level);

    /**
     * Log an exception with a message for the specified log level.
     *
     * @param level     the log level
     * @param message   the message
     * @param throwable the exception
     */
    void log(LogLevel level, String message, Throwable throwable);

    /**
     * Log with a message returned by a supplier.
     *
     * @param level           the log level
     * @param messageSupplier the message supplier
     */
    void log(LogLevel level, Supplier<String> messageSupplier);
}
