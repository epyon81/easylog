package easylog.spring;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "easylog.spring")
@Import(EasylogConfiguration.class)
public class AppConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TestLoggerFactory getLoggerFactory() {
        return new TestLoggerFactory();
    }
}
