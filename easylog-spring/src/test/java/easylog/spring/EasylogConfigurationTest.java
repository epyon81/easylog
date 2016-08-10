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

        TestLoggerFactory loggerFactory = ctx.getBean(TestLoggerFactory.class);

        String expectedLog = "[INFO] Enter easylog.spring.TestClass.doIt(5, 3).\n" +
                "[WARN] Exit doIt...\n";

        assertThat(loggerFactory.getLogText()).isEqualTo(expectedLog);
    }
}
