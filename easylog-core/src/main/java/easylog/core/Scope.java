package easylog.core;

/**
 * Scope information of a logging interception.
 */
public class Scope {
    private InvocationContext invocationContext;
    private Exception exception;
    private Object result;

    /**
     * Constructs the object.
     *
     * @param invocationContext the invocation context
     * @param exception         exception that may have occurred
     * @param result            the result of the invocation
     */
    public Scope(InvocationContext invocationContext, Exception exception, Object result) {
        this.invocationContext = invocationContext;
        this.exception = exception;
        this.result = result;
    }

    /**
     * Gets the invocation context.
     *
     * @return the invocation context
     */
    public InvocationContext getInvocationContext() {
        return invocationContext;
    }

    /**
     * Gets the exception
     *
     * @return the exception or <code>null</code> if no excetion occurred
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Gets the result of the invocation.
     *
     * @return the result
     */
    public Object getResult() {
        return result;
    }
}
