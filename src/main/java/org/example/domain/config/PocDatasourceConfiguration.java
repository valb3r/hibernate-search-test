package org.example.domain.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class PocDatasourceConfiguration {

    @Bean
    @ConfigurationProperties("datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("datasource.canonical")
    public DataSourceProperties canonicalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource masterDataSource() {
        return masterDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @Primary
    public DataSource canonicalDataSource() {
        return canonicalDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

}