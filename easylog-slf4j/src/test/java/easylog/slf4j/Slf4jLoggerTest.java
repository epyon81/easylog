package easylog.slf4j;

import easylog.core.LogLevel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class Slf4jLoggerTest {

    private Slf4jLogger testLogger;

    @Mock
    private Logger loggerMock;

    @Before
    public void setUp() throws Exception {
        testLogger = new Slf4jLogger(loggerMock);
    }

    @Test
    public void isEnabled() throws Exception {
        testLogger.isEnabled(LogLevel.DEBUG);
        verify(loggerMock).isDebugEnabled();

        testLogger.isEnabled(LogLevel.WARN);
        verify(loggerMock).isWarnEnabled();

        testLogger.isEnabled(LogLevel.ERROR);
        verify(loggerMock).isErrorEnabled();

        testLogger.isEnabled(LogLevel.INFO);
        verify(loggerMock).isInfoEnabled();

        testLogger.isEnabled(LogLevel.TRACE);
        verify(loggerMock).isTraceEnabled();
    }

    @Test
    public void log_exception() throws Exception {
        Throwable error = new Exception();
        String message = "Test";

        testLogger.log(LogLevel.DEBUG, message, error);
        verify(loggerMock).debug(message, error);

        testLogger.log(LogLevel.TRACE, message, error);
        verify(loggerMock).trace(message, error);

        testLogger.log(LogLevel.ERROR, message, error);
        verify(loggerMock).error(message, error);

        testLogger.log(LogLevel.INFO, message, error);
        verify(loggerMock).info(message, error);

        testLogger.log(LogLevel.WARN, message, error);
        verify(loggerMock).warn(message, error);
    }

    @Test
    public void log_message_supplier_enabled() throws Exception {
        logMessageSupplier(true);
    }

    @Test
    public void log_message_supplier_disabled() throws Exception {
        logMessageSupplier(false);
    }

    private void logMessageSupplier(boolean enabled) {
        String message = "Test";

        when(loggerMock.isDebugEnabled()).thenReturn(enabled);
        when(loggerMock.isErrorEnabled()).thenReturn(enabled);
        when(loggerMock.isInfoEnabled()).thenReturn(enabled);
        when(loggerMock.isTraceEnabled()).thenReturn(enabled);
        when(loggerMock.isWarnEnabled()).thenReturn(enabled);

        int wanted = enabled ? 1 : 0;

        testLogger.log(LogLevel.DEBUG, () -> message);
        verify(loggerMock, times(wanted)).debug(message);

        testLogger.log(LogLevel.TRACE, () -> message);
        verify(loggerMock, times(wanted)).trace(message);

        testLogger.log(LogLevel.ERROR, () -> message);
        verify(loggerMock, times(wanted)).error(message);

        testLogger.log(LogLevel.INFO, () -> message);
        verify(loggerMock, times(wanted)).info(message);

        testLogger.log(LogLevel.WARN, () -> message);
        verify(loggerMock, times(wanted)).warn(message);
    }
}