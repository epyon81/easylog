package easylog.guice;

import easylog.core.LogMessageParser;
import easylog.core.LoggerFactory;
import easylog.core.LoggingInterceptor;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provider for an easylog {@link LoggingInterceptor}.
 *
 * @since 1.0
 */
public class LoggingInterceptorProvider implements Provider<LoggingInterceptor> {

    private LoggerFactory loggerFactory;
    @com.google.inject.Inject(optional = true)
    private LogMessageParser logMessageParser;

    /**
     * Creates the object.
     *
     * @param loggerFactory the logger factory
     */
    @Inject
    public LoggingInterceptorProvider(LoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    @Override
    public LoggingInterceptor get() {
        LoggingInterceptor interceptor = new LoggingInterceptor(loggerFactory);
        interceptor.setLogMessageParser(logMessageParser);

        return interceptor;
    }
}
