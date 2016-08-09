package easylog.jee;

import easylog.core.LogMessageParser;
import easylog.core.LoggerFactory;
import easylog.core.LoggingInterceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

/**
 * JEE interceptor to handle {@link easylog.core.Log} annotated methods.
 *
 * @since 1.0
 */
@Easylog
@Interceptor
public class EasylogJeeInterceptor {

    @Inject
    private LoggerFactory loggerFactory;

    @Inject
    private LogMessageParser logMessageParser;

    /**
     * Intercepting method.
     *
     * @param ctx the invocation context
     * @return the invocation result
     * @throws Exception when a exception is thrown in the intercepted method
     */
    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        LoggingInterceptor interceptor = new LoggingInterceptor(loggerFactory);
        interceptor.setLogMessageParser(logMessageParser);

        try {
            return interceptor.intercept(new EasylogInvocationContext(ctx));
        } catch (Exception e) {
            throw e;
        } catch (Throwable throwable) {
            throw new Exception(throwable);
        }
    }

    private static class EasylogInvocationContext implements easylog.core.InvocationContext {

        private InvocationContext invocationContext;

        public EasylogInvocationContext(InvocationContext invocationContext) {
            this.invocationContext = invocationContext;
        }

        @Override
        public Class<?> getTargetClass() {
            try {
                return getActualSimpleClass(invocationContext.getTarget().getClass());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        private Class<?> getActualSimpleClass(Class<?> possibleProxyClass) throws ClassNotFoundException {
            String outerClassName = possibleProxyClass.getName();
            String innerClassName;

            if (outerClassName.contains("$Proxy$")) {
                innerClassName = outerClassName.substring(0, outerClassName.indexOf('$'));
            } else {
                innerClassName = outerClassName;
            }

            return Class.forName(innerClassName);
        }

        @Override
        public Method getMethod() {
            return invocationContext.getMethod();
        }

        @Override
        public Object[] getArguments() {
            return invocationContext.getParameters();
        }

        @Override
        public Object proceed() throws Throwable {
            return invocationContext.proceed();
        }
    }
}
