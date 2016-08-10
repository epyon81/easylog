package easylog.guice;

import com.google.inject.AbstractModule;
import easylog.core.*;

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

        bindOptionalClass("easylog.slf4j.Slf4jLoggerFactory", LoggerFactory.class);
        bindOptionalClass("easylog.el.messageparser.ElMessageParser", LogMessageParser.class);
    }

    private <T> void bindOptionalClass(String className, Class<T> interfaceClass) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends T> optionalClass = (Class<? extends T>) Class.forName(className);

            bind(interfaceClass).to(optionalClass);
        } catch (ClassNotFoundException ignored) {
        }
    }
}
