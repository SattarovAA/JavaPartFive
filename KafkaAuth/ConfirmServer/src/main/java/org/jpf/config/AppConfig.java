package org.jpf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Default application configuration.
 */
@ComponentScan("org.jpf")
@Configuration
@PropertySource("classpath:config/application.yml")
public class AppConfig {
    /**
     * Bean {@link PasswordEncoder} for security configure.
     *
     * @return default {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
