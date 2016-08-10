package easylog.spring;

import easylog.core.LogLevel;
import easylog.core.Logger;
import easylog.core.LoggerFactory;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class TestLoggerFactory implements LoggerFactory {

    private StringBuilder log = new StringBuilder();

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new TestLogger();
    }

    public String getLogText() {
        return log.toString();
    }

    private class TestLogger implements Logger {

        @Override
        public boolean isEnabled(LogLevel level) {
            return true;
        }

        @Override
        public void log(LogLevel level, String message, Throwable throwable) {
            if (throwable != null) {
                log.append(MessageFormat.format("[{0}] {1}:\n{2}", level, message, throwable));
            } else {
                log.append(MessageFormat.format("[{0}] {1}", level, message));
            }
            log.append("\n");
        }

        @Override
        public void log(LogLevel level, Supplier<String> messageSupplier) {
            log.append(MessageFormat.format("[{0}] {1}", level, messageSupplier.get()));
            log.append("\n");
        }
    }
}
