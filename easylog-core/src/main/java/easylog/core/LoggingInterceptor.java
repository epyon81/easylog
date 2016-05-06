package easylog.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Logging interceptor that can handle invocations of methods annotated with the {@link Log} annotation.
 */
public class LoggingInterceptor {
    private LoggerFactory loggerFactory;
    private Logger logger;
    private LogMessageParser logMessageParser;

    /**
     * Constructs the object.
     *
     * @param loggerFactory the logger factory which created the loggers
     */
    public LoggingInterceptor(LoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;

        logger = loggerFactory.getLogger(LoggingInterceptor.class);
    }

    private static String formatException(Exception exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        return stackTrace.toString();
    }

    private static String arrayToString(Object[] array) {
        String result = Arrays.toString(array);

        return result.substring(1, result.length() - 1);
    }

    /**
     * Sets a log message parser.
     *
     * @param logMessageParser the log message parser
     */
    public void setLogMessageParser(LogMessageParser logMessageParser) {
        this.logMessageParser = logMessageParser;
    }

    /**
     * This should be called when intercepting a logging method.
     *
     * @param context the invocation context
     * @return the result of the intercepted invocation
     * @throws Throwable exception thrown in the intercepted method
     */
    public Object intercept(InvocationContext context) throws Throwable {
        logInvocation(LogContext.create(context, LogPosition.BEFORE));

        Object result = proceedAndLogException(context);

        logInvocation(LogContext.createWithResult(context, LogPosition.AFTER, result));

        return result;
    }

    private Object proceedAndLogException(InvocationContext context) throws Throwable {
        Object result;

        try {
            result = context.proceed();
        } catch (Exception e) {
            logInvocation(LogContext.createWithException(context, LogPosition.AFTER_EXCEPTION, e));

            throw e;
        }
        return result;
    }

    private void logInvocation(LogContext logContext) {
        List<Log> logs = getInterceptedMethodLogAnnotationsForCurrentLogPosition(logContext.getInvocationContext(),
                logContext.getLogPosition());

        for (Log log : logs) {
            try {
                buildMessageAndCallLogger(logContext, log);
            } catch (Exception e) {
                logExceptionDuringInvocationLogging(logContext, e);
            }
        }
    }

    private List<Log> getInterceptedMethodLogAnnotationsForCurrentLogPosition(InvocationContext context,
                                                                              LogPosition logPosition) {
        return Arrays.asList(context.getMethod().getAnnotationsByType(Log.class))
                .stream()
                .filter(log -> Arrays.asList(log.position()).contains(logPosition))
                .collect(toList());
    }

    private void buildMessageAndCallLogger(LogContext logContext, Log log) {
        Logger invocationLogger = loggerFactory.getLogger(logContext.getInvocationContext().getTargetClass());

        if (invocationLogger.isEnabled(log.level())) {
            String message = buildMessage(log, logContext.getLogPosition(), logContext.getScope());

            invocationLogger.log(log.level(), message, logContext.getException());
        }
    }

    private String buildMessage(Log log, LogPosition logPosition, Scope scope) {
        String message;

        if (log.value().isEmpty()) {
            message = getDefaultFormat(log, logPosition, scope);
        } else {
            message = parseMessage(scope, log.value());
        }

        return message;
    }

    private String getDefaultFormat(Log log, LogPosition logPosition, Scope scope) {
        String format = null;

        switch (logPosition) {
            case BEFORE:
                format = getBeforePositionFormat(log, scope);
                break;
            case AFTER:
                format = getAfterPositionFormat(log, scope);
                break;
            case AFTER_EXCEPTION:
                format = getAfterExceptionPositionFormat(log, scope);
                break;
        }

        return format;
    }

    private String parseMessage(Scope scope, String message) {
        if (logMessageParser != null) {
            return logMessageParser.parse(scope, message);
        } else {
            return message;
        }
    }

    private String getBeforePositionFormat(Log log, Scope scope) {
        String format;
        if (log.detailed()) {
            format = MessageFormat.format("Enter {0}.{1}({2}).",
                    scope.getInvocationContext().getTargetClass().getName(),
                    scope.getInvocationContext().getMethod().getName(),
                    arrayToString(scope.getInvocationContext().getArguments()));
        } else {
            format = MessageFormat.format("Enter {0}.{1}.",
                    scope.getInvocationContext().getTargetClass().getName(),
                    scope.getInvocationContext().getMethod().getName());
        }
        return format;
    }

    private String getAfterPositionFormat(Log log, Scope scope) {
        String format;
        if (log.detailed()) {
            if (scope.getInvocationContext().getMethod().getReturnType().equals(Void.TYPE)) {
                format = MessageFormat.format("Exit {0}.{1}({2}).",
                        scope.getInvocationContext().getTargetClass().getName(),
                        scope.getInvocationContext().getMethod().getName(),
                        arrayToString(scope.getInvocationContext().getArguments()));
            } else {
                format = MessageFormat.format("Exit {0}.{1}({2}) with result {3}.",
                        scope.getInvocationContext().getTargetClass().getName(),
                        scope.getInvocationContext().getMethod().getName(),
                        arrayToString(scope.getInvocationContext().getArguments()),
                        scope.getResult());
            }
        } else {
            format = MessageFormat.format("Exit {0}.{1}.",
                    scope.getInvocationContext().getTargetClass().getName(),
                    scope.getInvocationContext().getMethod().getName());
        }
        return format;
    }

    private String getAfterExceptionPositionFormat(Log log, Scope scope) {
        String format;
        if (log.detailed()) {
            format = MessageFormat.format("Error in {0}.{1}({2}):\n{3}",
                    scope.getInvocationContext().getTargetClass().getName(),
                    scope.getInvocationContext().getMethod().getName(),
                    arrayToString(scope.getInvocationContext().getArguments()),
                    formatException(scope.getException()));
        } else {
            format = MessageFormat.format("Error in {0}.{1}:\n{2}",
                    scope.getInvocationContext().getTargetClass().getName(),
                    scope.getInvocationContext().getMethod().getName(),
                    formatException(scope.getException()));
        }
        return format;
    }

    private void logExceptionDuringInvocationLogging(LogContext logContext, Exception e) {
        logger.log(
                LogLevel.WARN,
                () -> MessageFormat.format(
                        "Error creating a log entry for ''{0}.{1}'' at position {2}:\n{3}",
                        logContext.getInvocationContext().getTargetClass().getName(),
                        logContext.getInvocationContext().getMethod().getName(),
                        logContext.getLogPosition(),
                        formatException(e)));
    }
}
