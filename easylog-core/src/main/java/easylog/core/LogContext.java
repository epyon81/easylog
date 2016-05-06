package easylog.core;

class LogContext {
    private LogPosition logPosition;
    private Scope scope;

    private LogContext(InvocationContext context, LogPosition logPosition, Object result, Exception exception) {
        this.logPosition = logPosition;

        scope = new Scope(context, exception, result);
    }

    public static LogContext create(InvocationContext context, LogPosition logPosition) {
        return new LogContext(context, logPosition, null, null);
    }

    public static LogContext createWithResult(InvocationContext context, LogPosition logPosition, Object result) {
        return new LogContext(context, logPosition, result, null);
    }

    public static LogContext createWithException(InvocationContext context, LogPosition logPosition, Exception exception) {
        return new LogContext(context, logPosition, null, exception);
    }

    public InvocationContext getInvocationContext() {
        return scope.getInvocationContext();
    }

    public LogPosition getLogPosition() {
        return logPosition;
    }

    public Exception getException() {
        return scope.getException();
    }

    public Scope getScope() {
        return scope;
    }
}
