package easylog.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "easylog.spring")
@Import(EasylogConfiguration.class)
public class AppConfig {
}
