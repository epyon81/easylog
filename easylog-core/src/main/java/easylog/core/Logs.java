package easylog.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation the contains multiple {@link Log} annotations.
 */
@Documented
@Retention(value = RUNTIME)
@Target(value = METHOD)
public @interface Logs {

    /**
     * @return array with log annotations
     */
    Log[] value();
}
