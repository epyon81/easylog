package easylog.guice;

import easylog.core.InvocationContext;
import easylog.core.LoggingInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;
import java.lang.reflect.Method;

/**
 * Guice method interceptor which delegates to the easylog {@link LoggingInterceptor}.
 */
public class EasylogMethodInterceptor implements MethodInterceptor {

    @Inject
    private LoggingInterceptor loggingInterceptor;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return loggingInterceptor.intercept(new GuiceInvocationContext(invocation));
    }

    private static class GuiceInvocationContext implements InvocationContext {

        public static final String GUICE_ENHANCER_STRING = "EnhancerByGuice";

        private MethodInvocation methodInvocation;

        public GuiceInvocationContext(MethodInvocation methodInvocation) {
            this.methodInvocation = methodInvocation;
        }

        @Override
        public Class<?> getTargetClass() {
            Class<?> targetClass = methodInvocation.getThis().getClass();

            if (classIsEnhancedByGuice(targetClass)) {
                targetClass = targetClass.getSuperclass();
            }

            return targetClass;
        }

        private boolean classIsEnhancedByGuice(Class<?> targetClass) {
            return targetClass.getSimpleName().contains(GUICE_ENHANCER_STRING);
        }

        @Override
        public Method getMethod() {
            return methodInvocation.getMethod();
        }

        @Override
        public Object[] getArguments() {
            return methodInvocation.getArguments();
        }

        @Override
        public Object proceed() throws Throwable {
            return methodInvocation.proceed();
        }
    }
}
