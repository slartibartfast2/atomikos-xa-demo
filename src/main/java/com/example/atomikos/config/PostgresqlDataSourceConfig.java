package com.example.atomikos.config;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "com.example.atomikos.dual.repository.postgresql", entityManagerFactoryRef = "mysqlEntityManagerFactory", transactionManagerRef = "transactionManager")
public class PostgresqlDataSourceConfig {

    @Value("${spring.datasource.postgresql.driver-class-name}")
    String driverClass;
    @Value("${spring.datasource.postgresql.url}")
    String url;
    @Value("${spring.datasource.postgresql.username}")
    String userName;
    @Value("${spring.datasource.postgresql.password}")
    String passWord;


    @Bean(name = "psDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource psDataSource() {
        System.out.println("psDataSource init");
        PGXADataSource pgxaDataSource = new PGXADataSource();

        pgxaDataSource.setUrl(url);
        pgxaDataSource.setPassword(passWord);
        pgxaDataSource.setUser(userName);
        //pgxaDataSource.setPinGlobalTxToPhysicalConnection(true);
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgxaDataSource);
        xaDataSource.setUniqueResourceName("postgresqlDataSource");
        xaDataSource.setMinPoolSize(10);
        xaDataSource.setPoolSize(10);
        xaDataSource.setMaxPoolSize(30);
        xaDataSource.setBorrowConnectionTimeout(60);
        xaDataSource.setReapTimeout(20);
        xaDataSource.setMaxIdleTime(60);
        xaDataSource.setMaintenanceInterval(60);
        return xaDataSource;
    }

    @Bean(name = "jpaPostgresqlAdapter")
    public JpaVendorAdapter jpaPostgresqlAdapter() {
        System.out.println("jpaPostgresqlAdapter init");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        adapter.setGenerateDdl(false);
        return adapter;
    }

    @Primary
    @Bean(name = "postgresqlEntityManagerFactory")
    @DependsOn({"atomikosJtaPlatform"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        System.out.println("postgresqlEntityManagerFactory init");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJpaVendorAdapter(jpaPostgresqlAdapter());

        entityManager.setPackagesToScan("com.example.atomikos.dual.model");
        entityManager.setJtaDataSource(psDataSource());
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory");
        properties.put("hibernate.transaction.jta.platform", "com.example.atomikos.config.AtomikosJtaPlatform");

        entityManager.setJpaProperties(properties);
        return entityManager;
    }
}