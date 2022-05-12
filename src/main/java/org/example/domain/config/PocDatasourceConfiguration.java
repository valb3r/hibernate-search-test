package org.example.domain.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
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
    @ConfigurationProperties("datasource.master.jpa")
    public JpaProperties masterJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    public DataSource masterDataSource(DataSourceProperties masterDataSourceProperties) {
        return masterDataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties("datasource.canonical")
    public DataSourceProperties canonicalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("datasource.canonical.jpa")
    public JpaProperties canonicalJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @Primary
    public DataSource canonicalDataSource(DataSourceProperties canonicalDataSourceProperties) {
        return canonicalDataSourceProperties
                .initializeDataSourceBuilder()
                .build();
    }

}