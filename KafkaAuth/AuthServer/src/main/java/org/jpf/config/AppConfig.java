package org.jpf.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Default application configuration.
 */
@ComponentScan("org.jpf")
@Configuration
@PropertySource("classpath:config/application.yml")
public class AppConfig {
}
