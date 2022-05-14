package org.example.hibernate_search_poc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig {

    @Bean
    WebMvcConfigurer isoDatesByDefault() {
        return new WebMvcConfigurer() {
            public void addFormatters(FormatterRegistry registry) {
                DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
                registrar.setUseIsoFormat(true);
                registrar.registerFormatters(registry);
            }
        };
    }
}
