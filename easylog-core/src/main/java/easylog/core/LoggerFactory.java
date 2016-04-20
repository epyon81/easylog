package easylog.core;

/**
 * Interface for a factory class that created {@link Logger} objects.
 *
 * @since 1.0
 */
public interface LoggerFactory {

    /**
     * Creates a logger for a class.
     *
     * @param clazz the class to create the logger for
     * @return the class logger
     */
    Logger getLogger(Class<?> clazz);
}
