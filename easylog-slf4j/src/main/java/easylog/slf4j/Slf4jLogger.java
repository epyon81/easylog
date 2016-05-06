package easylog.slf4j;

import easylog.core.LogLevel;
import easylog.core.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Easylog logger implementation using slf4j logger.
 */
@SuppressWarnings("Duplicates")
public class Slf4jLogger implements Logger {

    private org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(LogLevel level, String message, Throwable throwable) {
        BiConsumer<String, Throwable> logConsumer = getMessageWithThrowableConsumerForLogLevel(level);

        logConsumer.accept(message, throwable);
    }

    private BiConsumer<String, Throwable> getMessageWithThrowableConsumerForLogLevel(LogLevel level) {
        BiConsumer<String, Throwable> logConsumer;

        switch (level) {
            case DEBUG:
                logConsumer = logger::debug;
                break;
            case ERROR:
                logConsumer = logger::error;
                break;
            case INFO:
                logConsumer = logger::info;
                break;
            case TRACE:
                logConsumer = logger::trace;
                break;
            case WARN:
                logConsumer = logger::warn;
                break;
            default:
                throw new RuntimeException("Unexpected log level: " + level);
        }
        return logConsumer;
    }

    @Override
    public void log(LogLevel level, Supplier<String> messageSupplier) {
        if (isEnabled(level)) {
            Consumer<String> logConsumer = getMessageConsumerForLogLevel(level);

            logConsumer.accept(messageSupplier.get());
        }
    }

    @Override
    public boolean isEnabled(LogLevel level) {
        Supplier<Boolean> loggerEnabledSupplier = getLoggerEnabledSupplierForLogLevel(level);

        return loggerEnabledSupplier.get();
    }

    private Consumer<String> getMessageConsumerForLogLevel(LogLevel level) {
        Consumer<String> logConsumer;

        switch (level) {
            case DEBUG:
                logConsumer = logger::debug;
                break;
            case ERROR:
                logConsumer = logger::error;
                break;
            case INFO:
                logConsumer = logger::info;
                break;
            case TRACE:
                logConsumer = logger::trace;
                break;
            case WARN:
                logConsumer = logger::warn;
                break;
            default:
                throw new RuntimeException("Unexpected log level: " + level);
        }
        return logConsumer;
    }

    private Supplier<Boolean> getLoggerEnabledSupplierForLogLevel(LogLevel level) {
        Supplier<Boolean> supplier;

        switch (level) {
            case DEBUG:
                supplier = logger::isDebugEnabled;
                break;
            case ERROR:
                supplier = logger::isErrorEnabled;
                break;
            case INFO:
                supplier = logger::isInfoEnabled;
                break;
            case TRACE:
                supplier = logger::isTraceEnabled;
                break;
            case WARN:
                supplier = logger::isWarnEnabled;
                break;
            default:
                throw new RuntimeException("Unexpected log level: " + level);
        }
        return supplier;
    }
}
