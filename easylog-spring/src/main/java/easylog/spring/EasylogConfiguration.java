package easylog.spring;

import easylog.core.LogMessageParser;
import easylog.core.LoggerFactory;
import easylog.core.LoggingInterceptor;
import easylog.el.messageparser.ElMessageParser;
import easylog.slf4j.Slf4jLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Easylog spring configuration which registers the AspectJ interceptor, the {@link Slf4jLoggerFactory}
 * and the {@link ElMessageParser}.
 *
 * @since 1.0
 */
@Configuration
@EnableAspectJAutoProxy
public class EasylogConfiguration {

    @Bean
    public LoggingInterceptor getLoggingInterceptor(LoggerFactory loggerFactory, LogMessageParser messageParser) {
        LoggingInterceptor interceptor = new LoggingInterceptor(loggerFactory);
        interceptor.setLogMessageParser(messageParser);

        return interceptor;
    }

    @Bean
    public LoggingAspect getLoggingAspect(LoggingInterceptor loggingInterceptor) {
        return new LoggingAspect(loggingInterceptor);
    }

    @Bean
    public LoggerFactory getLoggerFactory() {
        return new Slf4jLoggerFactory();
    }

    @Bean
    public LogMessageParser getLogMessageParser() {
        return new ElMessageParser();
    }
}
