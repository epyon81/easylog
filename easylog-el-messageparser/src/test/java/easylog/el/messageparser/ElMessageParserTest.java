package easylog.el.messageparser;

import easylog.core.InvocationContext;
import easylog.core.Scope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElMessageParserTest {

    @Mock
    private InvocationContext invocationContext;

    @Test
    public void parse() throws Exception {
        Exception ex = new RuntimeException("TEST");
        Integer result = 123;

        Scope scope = new Scope(invocationContext, ex, result);

        when(invocationContext.getMethod()).thenReturn(ElMessageParserTest.class.getMethod("parse"));
        when(invocationContext.getArguments()).thenReturn(new String[]{"A", "B"});

        ElMessageParser parser = new ElMessageParser();

        String parsedMessage = parser.parse(scope, "public ${result.getClass().getName()} ${methodName}(${args:asString(arguments)}) throws ${exception.getClass().getName()}");

        assertThat(parsedMessage).isEqualTo("public java.lang.Integer parse(A, B) throws java.lang.RuntimeException");
    }

    @Test
    public void arrayToString() throws Exception {
        Integer[] values = new Integer[]{1, 2, 3};

        String valuesString = ElMessageParser.arrayToString(values);

        assertThat(valuesString).isEqualTo("1, 2, 3");
    }

}