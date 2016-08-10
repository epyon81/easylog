package easylog.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import easylog.core.LogLevel;
import easylog.core.Logger;
import easylog.core.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class EasylogModuleTest {

    private StringBuilder log;

    @Test
    public void testLoggingInterception() throws Exception {
        log = new StringBuilder();

        Injector injector = Guice.createInjector(new EasylogModule(), new TestModule());

        TestObject testObject = injector.getInstance(TestObject.class);

        testObject.testIt();

        String expectedLog = "[INFO] Hello from testIt.\n" +
                "[INFO] Enter easylog.guice.TestObject.more().\n" +
                "[INFO] Exit easylog.guice.TestObject.more() with result 0.\n";

        assertThat(log.toString()).isEqualTo(expectedLog);
    }

    public class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(LoggerFactory.class).toInstance(new TestLoggerFactory());
        }
    }

    public class TestLogger implements Logger {

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

    public class TestLoggerFactory implements LoggerFactory {

        @Override
        public Logger getLogger(Class<?> clazz) {
            return new TestLogger();
        }
    }
}