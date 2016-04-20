package easylog.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class EasylogConfigurationTest {

    @Test
    public void testConfiguration() throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        TestClass testClass = ctx.getBean(TestClass.class);

        int result = testClass.doIt(5, 3);

        assertThat(result).isEqualTo(8);
    }
}
