package easylog.spring;

import easylog.core.Log;
import easylog.core.LogLevel;
import easylog.core.LogPosition;
import org.springframework.stereotype.Component;

@Component
public class TestClass {

    @Log(detailed = true)
    @Log(value = "Exit ${methodName}...", position = LogPosition.AFTER, level = LogLevel.WARN)
    public int doIt(int a, int b) {
        return a + b;
    }
}
