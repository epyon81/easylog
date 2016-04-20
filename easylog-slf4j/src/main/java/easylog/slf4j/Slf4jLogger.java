package easylog.slf4j;

import easylog.core.LogLevel;
import easylog.core.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Easylog logger implementation using slf4j logger.
 *
 * @since 1.0
 */
@SuppressWarnings("Duplicates")
public class Slf4jLogger implements Logger {

    private org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isEnabled(LogLevel level) {
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
                throw new NotImplementedException();
        }

        return supplier.get();
    }

    @Override
    public void log(LogLevel level, String message, Throwable throwable) {
        BiConsumer<String, Throwable> function;

        switch (level) {
            case DEBUG:
                function = logger::debug;
                break;
            case ERROR:
                function = logger::error;
                break;
            case INFO:
                function = logger::info;
                break;
            case TRACE:
                function = logger::trace;
                break;
            case WARN:
                function = logger::warn;
                break;
            default:
                throw new NotImplementedException();
        }

        function.accept(message, throwable);
    }

    @Override
    public void log(LogLevel level, Supplier<String> messageSupplier) {
        if (isEnabled(level)) {
            Consumer<String> function;

            switch (level) {
                case DEBUG:
                    function = logger::debug;
                    break;
                case ERROR:
                    function = logger::error;
                    break;
                case INFO:
                    function = logger::info;
                    break;
                case TRACE:
                    function = logger::trace;
                    break;
                case WARN:
                    function = logger::warn;
                    break;
                default:
                    throw new NotImplementedException();
            }

            function.accept(messageSupplier.get());
        }
    }
}
