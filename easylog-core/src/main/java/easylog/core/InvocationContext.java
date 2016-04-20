package easylog.core;

import java.lang.reflect.Method;

/**
 * Interface for an invocation context.
 *
 * @since 1.0
 */
public interface InvocationContext {

    /**
     * Gets the class of the invocation target.
     *
     * @return the target class
     */
    Class<?> getTargetClass();

    /**
     * Gets the method which was invoked.
     *
     * @return the method
     */
    Method getMethod();

    /**
     * Gets the arguments with which the method was invoked.
     *
     * @return the arguments
     */
    Object[] getArguments();

    /**
     * Proceeds with the invocation on the intercepted method.
     *
     * @return the result of the original invocation
     * @throws Throwable any exception can be thrown by the intercepted method
     */
    Object proceed() throws Throwable;
}
