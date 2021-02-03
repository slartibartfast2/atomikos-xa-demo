package com.example.atomikos.config;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
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
@EnableJpaRepositories(basePackages = "com.example.atomikos.dual.repository.mysql", entityManagerFactoryRef = "mysqlEntityManagerFactory", transactionManagerRef = "transactionManager")
public class MysqlDataSourceConfig {

    @Value("${spring.datasource.mysql.driver-class-name}")
    String driverClass;
    @Value("${spring.datasource.mysql.url}")
    String url;
    @Value("${spring.datasource.mysql.username}")
    String userName;
    @Value("${spring.datasource.mysql.password}")
    String passWord;


    @Bean(name = "msDataSource")
    @Primary
    public DataSource msDataSource() {
        System.out.println("msDataSource init");
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();

        mysqlXADataSource.setUrl(url);
        mysqlXADataSource.setPassword(passWord);
        mysqlXADataSource.setUser(userName);
        //pgxaDataSource.setPinGlobalTxToPhysicalConnection(true);
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXADataSource);
        xaDataSource.setUniqueResourceName("mysqlDataSource");
        xaDataSource.setMinPoolSize(10);
        xaDataSource.setPoolSize(10);
        xaDataSource.setMaxPoolSize(30);
        xaDataSource.setBorrowConnectionTimeout(60);
        xaDataSource.setReapTimeout(20);
        xaDataSource.setMaxIdleTime(60);
        xaDataSource.setMaintenanceInterval(60);
        return xaDataSource;
    }

    @Bean(name = "jpaMysqlAdapter")
    public JpaVendorAdapter jpaMysqlAdapter() {
        System.out.println("jpaMysqlAdapter init");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        adapter.setGenerateDdl(false);
        return adapter;
    }

    @Bean(name = "mysqlEntityManagerFactory")
    @DependsOn({"atomikosJtaPlatform"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        System.out.println("mysqlEntityManagerFactory init");
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJpaVendorAdapter(jpaMysqlAdapter());

        entityManager.setPackagesToScan("com.example.atomikos.dual.model");
        entityManager.setJtaDataSource(msDataSource());
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.ddl-auto", "validate");
        properties.put("hibernate.use-new-id-generator-mappings", "false");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory");
        properties.put("hibernate.transaction.jta.platform", "com.example.atomikos.config.AtomikosJtaPlatform");

        entityManager.setJpaProperties(properties);
        return entityManager;
    }

}
