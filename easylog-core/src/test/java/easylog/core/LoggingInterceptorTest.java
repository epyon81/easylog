package easylog.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingInterceptorTest {

    private TestObject testObject;
    private TestLoggerFactory loggerFactory;

    @Mock
    private LogMessageParser logMessageParserMock;

    @Before
    public void before() throws Exception {
        loggerFactory = new TestLoggerFactory();

        testObject = new ByteBuddy()
                .subclass(TestObject.class)
                .name("TestProxy")
                .method(isAnnotatedWith(Log.class).or(isAnnotatedWith(Logs.class))).intercept(MethodDelegation.to(new LogInterceptor(loggerFactory)))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .newInstance();
    }

    @Test
    public void defaultDetailedBeforeAfter1() throws Exception {
        testObject.defaultDetailedBeforeAfter1("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[TRACE] Enter easylog.core.TestObject.defaultDetailedBeforeAfter1(Blah, 123).",
                "[TRACE] Exit easylog.core.TestObject.defaultDetailedBeforeAfter1(Blah, 123) with result 492.");
    }

    @Test
    public void defaultDetailedBeforeAfter2() throws Exception {
        testObject.defaultDetailedBeforeAfter2("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[TRACE] Enter easylog.core.TestObject.defaultDetailedBeforeAfter2(Blah, 123).",
                "[INFO] Exit easylog.core.TestObject.defaultDetailedBeforeAfter2(Blah, 123) with result 492.");
    }

    @Test
    public void defaultDetailedBefore() throws Exception {
        testObject.defaultDetailedBefore("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[TRACE] Enter easylog.core.TestObject.defaultDetailedBefore(Blah, 123).");
    }

    @Test
    public void defaultDetailedAfter() throws Exception {
        testObject.defaultDetailedAfter("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[INFO] Exit easylog.core.TestObject.defaultDetailedAfter(Blah, 123) with result 492.");
    }

    @Test
    public void simpleBefore() throws Exception {
        testObject.simpleBefore("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[INFO] Enter easylog.core.TestObject.simpleBefore.");
    }

    @Test
    public void simpleAfter() throws Exception {
        testObject.simpleAfter("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[INFO] Exit easylog.core.TestObject.simpleAfter.");
    }

    @Test
    public void simpleAfterException() throws Exception {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObject.simpleAfterException("Blah", 123))
                .withMessage("ERROR");

        assertThat(loggerFactory.messages).hasSize(1);
        assertThat(loggerFactory.messages.get(0)).startsWith(
                "[INFO] Error in easylog.core.TestObject.simpleAfterException:\n");
    }

    @Test
    public void detailedVoidMethodBeforeAfter() throws Exception {
        testObject.detailedVoidMethodBeforeAfter("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly(
                "[INFO] Enter easylog.core.TestObject.detailedVoidMethodBeforeAfter(Blah, 123).",
                "[INFO] Exit easylog.core.TestObject.detailedVoidMethodBeforeAfter(Blah, 123).");
    }

    @Test
    public void detailedVoidMethodAfterException() throws Exception {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObject.detailedVoidMethodAfterException("Blah", 123))
                .withMessage("ERROR");

        assertThat(loggerFactory.messages).hasSize(1);
        assertThat(loggerFactory.messages.get(0)).startsWith(
                "[INFO] Error in easylog.core.TestObject.detailedVoidMethodAfterException(Blah, 123):\n");
    }

    @Test
    public void detailedMethodAfterException() throws Exception {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObject.detailedMethodAfterException("Blah", 123))
                .withMessage("ERROR");

        assertThat(loggerFactory.messages).hasSize(1);
        assertThat(loggerFactory.messages.get(0)).startsWith(
                "[INFO] Error in easylog.core.TestObject.detailedMethodAfterException(Blah, 123):\n");
    }

    @Test
    public void customMessageBefore() throws Exception {
        testObject.customMessageBefore("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly("[INFO] B_TEST!!!");
    }

    @Test
    public void customMessageAfter() throws Exception {
        testObject.customMessageAfter("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly("[INFO] A_TEST!!!");
    }

    @Test
    public void customMessageAfterException() throws Exception {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObject.customMessageAfterException("Blah", 123))
                .withMessage("ERROR");

        assertThat(loggerFactory.messages).containsExactly("[INFO] AE_TEST!!!");
    }

    @Test
    public void messageParser() throws Exception {
        testObject = new ByteBuddy()
                .subclass(TestObject.class)
                .name("TestProxy")
                .method(isAnnotatedWith(Log.class).or(isAnnotatedWith(Logs.class))).intercept(MethodDelegation.to(new LogInterceptor(loggerFactory, logMessageParserMock)))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .newInstance();

        when(logMessageParserMock.parse(any(Scope.class), anyString())).thenReturn("LOG!");

        testObject.customMessageBefore("Blah", 123);

        assertThat(loggerFactory.messages).containsExactly("[INFO] LOG!");
    }

    @Test
    public void messageParser_throws_exception() throws Exception {
        testObject = new ByteBuddy()
                .subclass(TestObject.class)
                .name("TestProxy")
                .method(isAnnotatedWith(Log.class).or(isAnnotatedWith(Logs.class))).intercept(MethodDelegation.to(new LogInterceptor(loggerFactory, logMessageParserMock)))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .newInstance();

        when(logMessageParserMock.parse(any(Scope.class), anyString())).thenThrow(new RuntimeException("MPEX"));

        testObject.customMessageBefore("Blah", 123);

        assertThat(loggerFactory.messages).hasSize(1);
        assertThat(loggerFactory.messages.get(0)).startsWith(
                "[WARN] Error creating a log entry for 'easylog.core.TestObject.customMessageBefore' at position BEFORE:\njava.lang.RuntimeException: MPEX");
    }

    public static class TestLoggerFactory implements LoggerFactory {

        List<String> messages = new ArrayList<>();

        @Override
        public Logger getLogger(Class<?> clazz) {
            return new Logger() {
                @Override
                public boolean isEnabled(LogLevel level) {
                    return true;
                }

                @Override
                public void log(LogLevel level, String message, Throwable throwable) {
                    log(level, () -> message);
                }

                @Override
                public void log(LogLevel level, Supplier<String> messageSupplier) {
                    messages.add(MessageFormat.format("[{0}] {1}", level.toString(), messageSupplier.get()));
                }
            };
        }
    }

    public static class LogInterceptor {
        private TestLoggerFactory loggerFactory;
        private LogMessageParser logMessageParser;

        public LogInterceptor(TestLoggerFactory loggerFactory) {
            this(loggerFactory, null);
        }

        public LogInterceptor(TestLoggerFactory loggerFactory, LogMessageParser logMessageParser) {
            this.loggerFactory = loggerFactory;
            this.logMessageParser = logMessageParser;
        }

        @RuntimeType
        public Object interceptSuper(@SuperCall Callable<?> zuper, @AllArguments Object[] allArguments, @Origin Method method, @Origin Class clazz) throws Throwable {
            LoggingInterceptor interceptor = new LoggingInterceptor(loggerFactory);
            interceptor.setLogMessageParser(logMessageParser);

            return interceptor.intercept(new MyInvocationContext(method, zuper, allArguments, clazz));
        }
    }

    public static class MyInvocationContext implements InvocationContext {

        private Method method;
        private Callable<?> zuper;
        private Object[] arguments;
        private Class<?> targetClass;

        public MyInvocationContext(Method method, Callable<?> zuper, Object[] arguments, Class<?> targetClass) {
            this.method = method;
            this.zuper = zuper;
            this.arguments = arguments;
            this.targetClass = targetClass;
        }

        @Override
        public Class<?> getTargetClass() {
            return targetClass;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public Object proceed() throws Exception {
            return zuper.call();
        }
    }
}