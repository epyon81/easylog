package easylog.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Logging interceptor that can handle invocations of methods annotated with the {@link Log} annotation.
 *
 * @since 1.0
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
        logInvocation(context, LogPosition.BEFORE, null, null);

        Object result;

        try {
            result = context.proceed();
        } catch (Exception e) {
            logInvocation(context, LogPosition.AFTER_EXCEPTION, null, e);

            throw e;
        }

        logInvocation(context, LogPosition.AFTER, result, null);

        return result;
    }

    private void logInvocation(InvocationContext context, LogPosition logPosition, Object result, Exception exception) {
        List<Log> logs = Arrays.asList(context.getMethod().getAnnotationsByType(Log.class))
                .stream()
                .filter(log -> Arrays.asList(log.position()).contains(logPosition))
                .collect(toList());

        Scope scope = new Scope(context, exception, result);

        for (Log log : logs) {
            try {
                Logger invocationLogger = loggerFactory.getLogger(context.getTargetClass());

                if (invocationLogger.isEnabled(log.level())) {
                    String message = buildMessage(log, logPosition, scope);

                    invocationLogger.log(log.level(), message, exception);
                }
            } catch (Exception e) {
                logger.log(
                        LogLevel.WARN,
                        () -> MessageFormat.format(
                                "Error creating a log entry for ''{0}.{1}'' at position {2}:\n{3}",
                                context.getTargetClass().getName(),
                                context.getMethod().getName(),
                                logPosition,
                                formatException(e)));
            }
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

    private String parseMessage(Scope scope, String message) {
        if (logMessageParser != null) {
            return logMessageParser.parse(scope, message);
        } else {
            return message;
        }
    }

    private String getDefaultFormat(Log log, LogPosition logPosition, Scope scope) {
        String format = null;

        String targetClass = scope.getInvocationContext().getTargetClass().getName();
        String methodName = scope.getInvocationContext().getMethod().getName();

        switch (logPosition) {
            case BEFORE:
                if (log.detailed()) {
                    format = MessageFormat.format("Enter {0}.{1}({2}).",
                            targetClass,
                            methodName,
                            arrayToString(scope.getInvocationContext().getArguments()));
                } else {
                    format = MessageFormat.format("Enter {0}.{1}.",
                            targetClass,
                            methodName);
                }
                break;
            case AFTER:
                if (log.detailed()) {
                    if (scope.getInvocationContext().getMethod().getReturnType().equals(Void.TYPE)) {
                        format = MessageFormat.format("Exit {0}.{1}({2}).",
                                targetClass,
                                methodName,
                                arrayToString(scope.getInvocationContext().getArguments()));
                    } else {
                        format = MessageFormat.format("Exit {0}.{1}({2}) with result {3}.",
                                targetClass,
                                methodName,
                                arrayToString(scope.getInvocationContext().getArguments()),
                                scope.getResult());
                    }
                } else {
                    format = MessageFormat.format("Exit {0}.{1}.",
                            targetClass,
                            methodName);
                }
                break;
            case AFTER_EXCEPTION:
                if (log.detailed()) {
                    format = MessageFormat.format("Error in {0}.{1}({2}):\n{3}",
                            targetClass,
                            methodName,
                            arrayToString(scope.getInvocationContext().getArguments()),
                            formatException(scope.getException()));
                } else {
                    format = MessageFormat.format("Error in {0}.{1}:\n{2}",
                            targetClass,
                            methodName,
                            formatException(scope.getException()));
                }
                break;
        }

        return format;
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
}
