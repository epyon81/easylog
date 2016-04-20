package easylog.el.messageparser;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import easylog.core.InvocationContext;
import easylog.core.LogMessageParser;
import easylog.core.Scope;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Easylog message parser using the java unified expression language.
 *
 * @since 1.0
 */
public class ElMessageParser implements LogMessageParser {
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{.*?\\}");

    private ExpressionFactory expressionFactory;

    /**
     * Constructs the object.
     */
    public ElMessageParser() {
        expressionFactory = new ExpressionFactoryImpl();
    }

    @Override
    public String parse(Scope scope, String message) {
        SimpleContext context = new SimpleContext();
        context.setVariable("arguments", expressionFactory.createValueExpression(scope.getInvocationContext().getArguments(), Object[].class));
        context.setVariable("methodName", expressionFactory.createValueExpression(scope.getInvocationContext().getMethod().getName(), String.class));
        context.setVariable("exception", expressionFactory.createValueExpression(scope.getException(), Exception.class));
        context.setVariable("result", expressionFactory.createValueExpression(scope.getResult(), Object.class));
        context.setVariable("ic", expressionFactory.createValueExpression(scope.getInvocationContext(), InvocationContext.class));

        try {
            context.setFunction("args", "asString", ElMessageParser.class.getMethod("arrayToString", Object[].class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Matcher formatMatcher = EXPRESSION_PATTERN.matcher(message);

        List<Function<String, String>> replaceActions = new ArrayList<>();

        while (formatMatcher.find()) {
            String expression = formatMatcher.group();

            ValueExpression valueExpression = expressionFactory.createValueExpression(context, expression, Object.class);
            Object expressionValue = valueExpression.getValue(context);

            int start = formatMatcher.start();
            int end = formatMatcher.end();

            replaceActions.add(s -> new StringBuilder(s).replace(start, end, expressionValue == null ? "null" : expressionValue.toString()).toString());
        }

        for (int i = replaceActions.size() - 1; i >= 0; i--) {
            message = replaceActions.get(i).apply(message);
        }

        return message;
    }

    // this is just a utility method that is added to the EL context
    @SuppressWarnings({"WeakerAccess"})
    public static String arrayToString(Object[] array) {
        String result = Arrays.toString(array);

        return result.substring(1, result.length() - 1);
    }
}
