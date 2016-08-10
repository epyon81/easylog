package easylog.spring;

import easylog.core.LogMessageParser;
import easylog.core.LoggerFactory;
import easylog.core.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Easylog spring configuration which registers the AspectJ interceptor, the {@link LoggerFactory}
 * and the {@link LogMessageParser}.
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
    @Conditional(LoggerFactoryPresentCondition.class)
    public LoggerFactory getLoggerFactory() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (LoggerFactory) Class.forName(LoggerFactoryPresentCondition.TYPE_NAME).newInstance();
    }

    @Bean
    @Conditional(MessageParserPresentCondition.class)
    public LogMessageParser getLogMessageParser() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return (LogMessageParser) Class.forName(MessageParserPresentCondition.TYPE_NAME).newInstance();
    }
}
