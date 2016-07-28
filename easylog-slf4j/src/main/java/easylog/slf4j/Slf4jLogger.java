package easylog.slf4j;

import easylog.core.LogLevel;
import easylog.core.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Easylog logger implementation using slf4j logger.
 */
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
        return getForLogLevel(
                level,
                () -> logger::debug,
                () -> logger::error,
                () -> logger::info,
                () -> logger::trace,
                () -> logger::warn);
    }

    private <T> T getForLogLevel(LogLevel level,
                                 Supplier<T> debugSupplier,
                                 Supplier<T> errorSupplier,
                                 Supplier<T> infoSupplier,
                                 Supplier<T> traceSupplier,
                                 Supplier<T> warnSupplier) {
        switch (level) {
            case DEBUG:
                return debugSupplier.get();
            case ERROR:
                return errorSupplier.get();
            case INFO:
                return infoSupplier.get();
            case TRACE:
                return traceSupplier.get();
            case WARN:
                return warnSupplier.get();
            default:
                throw new RuntimeException("Unexpected log level: " + level);
        }
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
        return getForLogLevel(
                level,
                () -> logger::debug,
                () -> logger::error,
                () -> logger::info,
                () -> logger::trace,
                () -> logger::warn);
    }

    private Supplier<Boolean> getLoggerEnabledSupplierForLogLevel(LogLevel level) {
        return getForLogLevel(
                level,
                () -> logger::isDebugEnabled,
                () -> logger::isErrorEnabled,
                () -> logger::isInfoEnabled,
                () -> logger::isTraceEnabled,
                () -> logger::isWarnEnabled);
    }
}
