package org.example.hibernate_search_poc.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.example.domain.repository.canonical",
        entityManagerFactoryRef = "canonicalEntityManagerFactory",
        transactionManagerRef = "canonicalTransactionManager"
)
public class CanonicalJpaConfiguration {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean canonicalEntityManagerFactory(
            @Qualifier("canonicalDataSource") DataSource dataSource,
            @Qualifier("canonicalJpaProperties") JpaProperties jpaProperties,
            EntityManagerFactoryBuilder builder
    ) {
        return builder
                .dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .packages("org.example.domain.canonical")
                .build();
    }

    @Bean
    public PlatformTransactionManager canonicalTransactionManager(
            @Qualifier("canonicalEntityManagerFactory") LocalContainerEntityManagerFactoryBean canonicalEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(canonicalEntityManagerFactory.getObject()));
    }
}