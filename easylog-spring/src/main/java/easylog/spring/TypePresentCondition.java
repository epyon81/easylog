package easylog.spring;

import easylog.core.EasylogException;
import org.reflections.Reflections;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.text.MessageFormat;
import java.util.Set;

class TypePresentCondition<T> implements Condition {

    private Reflections reflections;
    private Class<T> typeClass;

    public TypePresentCondition(Class<T> typeClass) {
        reflections = new Reflections("easylog");
        this.typeClass = typeClass;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return findSingleImplementationType() != null;
    }

    private Class<? extends T> findSingleImplementationType() {
        Set<Class<? extends T>> foundTypes = reflections.getSubTypesOf(typeClass);

        Class<? extends T> implementationType = null;

        for (Class<? extends T> type : foundTypes) {
            if (implementationType == null) {
                implementationType = type;
            } else {
                throw new EasylogException(
                        MessageFormat.format("Multiple implementations of type ''{0}'' found.", typeClass));
            }
        }
        return implementationType;
    }
}
