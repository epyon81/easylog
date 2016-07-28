package easylog.guice;

import com.google.inject.AbstractModule;
import easylog.core.*;
import org.reflections.Reflections;

import java.text.MessageFormat;
import java.util.Set;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
 * Easylog Guice module.
 */
public class EasylogModule extends AbstractModule {

    @Override
    protected void configure() {
        EasylogMethodInterceptor interceptor = new EasylogMethodInterceptor();

        requestInjection(interceptor);

        bindInterceptor(any(), annotatedWith(Log.class).or(annotatedWith(Logs.class)), interceptor);

        bind(LoggingInterceptor.class).toProvider(LoggingInterceptorProvider.class);

        Reflections reflections = new Reflections("easylog");

        bindInterfaceImplementation(reflections, LoggerFactory.class);
        bindInterfaceImplementation(reflections, LogMessageParser.class);
    }

    private <T> void bindInterfaceImplementation(Reflections reflections, Class<T> interfaceClass) {
        Class<? extends T> implementationType = findSingleImplementationType(reflections, interfaceClass);

        if (implementationType != null) {
            bind(interfaceClass).to(implementationType);
        }
    }

    private <T> Class<? extends T> findSingleImplementationType(Reflections reflections, Class<T> interfaceClass) {
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
}
