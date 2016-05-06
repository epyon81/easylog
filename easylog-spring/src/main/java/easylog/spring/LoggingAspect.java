package easylog.spring;

import easylog.core.InvocationContext;
import easylog.core.Log;
import easylog.core.LoggingInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * AspectJ aspect for intercepting {@link Log} annotated methods and calling the easy log {@link LoggingInterceptor}.
 */
@Aspect
public class LoggingAspect {
    private LoggingInterceptor loggingInterceptor;

    /**
     * Constructs the object.
     *
     * @param loggingInterceptor the logging interceptor
     */
    public LoggingAspect(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    /**
     * Around advice method.
     *
     * @param proceedingJoinPoint the AspectJ join point
     * @return the invocation result
     * @throws Throwable exception thrown by the invocation
     */
    @Around("@annotation(easylog.core.Log) || @annotation(easylog.core.Logs)")
    public Object loggingAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return loggingInterceptor.intercept(new AspectInvocationContext(proceedingJoinPoint));
    }

    private static class AspectInvocationContext implements InvocationContext {

        private ProceedingJoinPoint proceedingJoinPoint;

        public AspectInvocationContext(ProceedingJoinPoint proceedingJoinPoint) {
            this.proceedingJoinPoint = proceedingJoinPoint;
        }

        @Override
        public Class<?> getTargetClass() {
            return proceedingJoinPoint.getSignature().getDeclaringType();
        }

        @Override
        public Method getMethod() {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

            return signature.getMethod();
        }

        @Override
        public Object[] getArguments() {
            return proceedingJoinPoint.getArgs();
        }

        @Override
        public Object proceed() throws Throwable {
            return proceedingJoinPoint.proceed();
        }
    }
}
