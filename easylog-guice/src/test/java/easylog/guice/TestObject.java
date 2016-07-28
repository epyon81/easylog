package easylog.guice;

import easylog.core.Log;
import easylog.core.LogPosition;

public class TestObject {

    @Log("Hello from ${methodName}.")
    public void testIt() {
        more();
    }

    @SuppressWarnings("WeakerAccess")
    @Log(detailed = true)
    @Log(detailed = true, position = LogPosition.AFTER)
    protected int more() {
        return 0;
    }
}
