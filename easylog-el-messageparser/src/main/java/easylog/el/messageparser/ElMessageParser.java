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
        SimpleContext context = createExpressionContext(scope);

        List<Function<String, String>> replaceFunctions = parseMessageIntoTextReplaceFunctions(message, context);

        return replacePlaceholdersInMessage(message, replaceFunctions);
    }

    private String replacePlaceholdersInMessage(String message, List<Function<String, String>> replaceFunctions) {
        for (int i = replaceFunctions.size() - 1; i >= 0; i--) {
            message = replaceFunctions.get(i).apply(message);
        }

        return message;
    }

    private List<Function<String, String>> parseMessageIntoTextReplaceFunctions(String message, SimpleContext context) {
        Matcher expressionMatcher = EXPRESSION_PATTERN.matcher(message);

        List<Function<String, String>> replaceFunctions = new ArrayList<>();

        while (expressionMatcher.find()) {
            replaceFunctions.add(parseExpressionIntoReplaceFunction(context, expressionMatcher));
        }
        return replaceFunctions;
    }

    private Function<String, String> parseExpressionIntoReplaceFunction(SimpleContext context, Matcher expressionMatcher) {
        String expression = expressionMatcher.group();

        Object expressionValue = evaluateExpression(context, expression);

        int start = expressionMatcher.start();
        int end = expressionMatcher.end();

        return s -> new StringBuilder(s).replace(start, end, nullSafeToString(expressionValue)).toString();
    }

    private static String nullSafeToString(Object value) {
        return value == null ? "null" : value.toString();
    }

    private Object evaluateExpression(SimpleContext context, String expression) {
        ValueExpression valueExpression = expressionFactory.createValueExpression(context, expression, Object.class);
        return valueExpression.getValue(context);
    }

    private SimpleContext createExpressionContext(Scope scope) {
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
        return context;
    }

    // this is just a utility method that is added to the EL context
    @SuppressWarnings({"WeakerAccess"})
    public static String arrayToString(Object[] array) {
        String result = Arrays.toString(array);

        return result.substring(1, result.length() - 1);
    }
}
