package org.example.hibernate_search_poc.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
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
    @ConfigurationProperties("datasource.master.liquibase")
    public SpringLiquibase masterLiquibase(
            @Qualifier("masterDataSource") DataSource masterDataSource
    ) {
        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(masterDataSource);
        return liquibase;
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

    @Bean
    @ConfigurationProperties("datasource.canonical.liquibase")
    public SpringLiquibase canonicalLiquibase(
            @Qualifier("canonicalDataSource") DataSource canonicalDataSource
    ) {
        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(canonicalDataSource);
        return liquibase;
    }
}