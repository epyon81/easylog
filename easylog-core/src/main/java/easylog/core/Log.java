package easylog.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interface, to mark methods for logging.
 *
 * @since 1.0
 */
@Documented
@Retention(value = RUNTIME)
@Target(value = METHOD)
@Repeatable(Logs.class)
public @interface Log {

    /**
     * @return the log message
     */
    String value() default "";

    /**
     * @return include arguments and return value in generated log message. Only if value is null.
     */
    boolean detailed() default false;

    /**
     * @return the log level
     */
    LogLevel level() default LogLevel.INFO;

    /**
     * @return position at which logging should be done
     */
    LogPosition[] position() default LogPosition.BEFORE;
}
