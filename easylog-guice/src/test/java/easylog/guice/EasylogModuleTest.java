package easylog.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class EasylogModuleTest {

    @Test
    public void testLoggingInterception() throws Exception {
        Injector injector = Guice.createInjector(new EasylogModule());

        TestObject testObject = injector.getInstance(TestObject.class);

        testObject.testIt();
    }
}