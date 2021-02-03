package com.example.atomikos.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, //if you are using Hibernate
        DataSourceTransactionManagerAutoConfiguration.class
})
public class MainConfig {

    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager")
    public TransactionManager atomikosTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn({"userTransaction", "atomikosTransactionManager"})
    public PlatformTransactionManager transactionManager() throws Throwable {
        UserTransaction userTransaction = userTransaction();
        TransactionManager atomikosTransactionManager = atomikosTransactionManager();
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, atomikosTransactionManager);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

    @Bean(name = "atomikosJtaPlatform")
    public AtomikosJtaPlatform atomikosJtaPlatform() {
        AtomikosJtaPlatform atomikosJtaPlatform = new AtomikosJtaPlatform();
        try {
            atomikosJtaPlatform.setTm(atomikosTransactionManager());
            atomikosJtaPlatform.setUt(userTransaction());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return atomikosJtaPlatform;
    }
}
