package easylog.spring;

import easylog.core.EasylogException;
import easylog.core.LogMessageParser;
import easylog.core.LoggerFactory;
import easylog.core.LoggingInterceptor;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.text.MessageFormat;
import java.util.Set;

/**
 * Easylog spring configuration which registers the AspectJ interceptor, the {@link LoggerFactory}
 * and the {@link LogMessageParser}.
 */
@Configuration
@EnableAspectJAutoProxy
public class EasylogConfiguration {

    private Reflections reflections;

    public EasylogConfiguration() {
        reflections = new Reflections("easylog");
    }

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
    public LoggerFactory getLoggerFactory() throws IllegalAccessException, InstantiationException {
        return findSingleImplementationType(LoggerFactory.class).newInstance();
    }

    private <T> Class<? extends T> findSingleImplementationType(Class<T> interfaceClass) {
        Set<Class<? extends T>> foundTypes = reflections.getSubTypesOf(interfaceClass);

        Class<? extends T> implementationType = null;

        for (Class<? extends T> type : foundTypes) {
            if (implementationType == null) {
                implementationType = type;
            } else {
                throw new EasylogException(
                        MessageFormat.format("Multiple implementations of type ''{0}'' found.", interfaceClass));
            }
        }
        return implementationType;
    }

    @Bean
    @Conditional(MessageParserPresentCondition.class)
    public LogMessageParser getLogMessageParser() throws IllegalAccessException, InstantiationException {
        return findSingleImplementationType(LogMessageParser.class).newInstance();
    }
}
