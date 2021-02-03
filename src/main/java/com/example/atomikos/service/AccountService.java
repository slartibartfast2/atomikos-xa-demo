package com.example.atomikos.service;

import com.example.atomikos.dual.model.mysql.MysqlAccount;
import com.example.atomikos.dual.model.postgresql.PostgresqlAccount;
import com.example.atomikos.dual.repository.mysql.AccountMysqlRepository;
import com.example.atomikos.dual.repository.postgresql.AccountPostgresqlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountMysqlRepository accountMysqlRepository;
    private final AccountPostgresqlRepository accountPostgresqlRepository;

    @Transactional
    public void createAccount(String username) {
        MysqlAccount mysqlAccount = new MysqlAccount();
        mysqlAccount.setUsername(username);
        accountMysqlRepository.save(mysqlAccount);

        PostgresqlAccount postgresqlAccount = new PostgresqlAccount();
        postgresqlAccount.setUsername(username);
        accountPostgresqlRepository.save(postgresqlAccount);
    }
}
