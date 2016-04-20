package easylog.guice;

import easylog.core.Log;

public class TestObject {

    @Log("Hello from ${methodName}.")
    public void testIt() {
        more();
    }

    @SuppressWarnings("WeakerAccess")
    @Log(detailed = true)
    protected int more() {
        return 0;
    }
}
