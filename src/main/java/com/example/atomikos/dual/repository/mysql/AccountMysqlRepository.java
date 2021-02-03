package com.example.atomikos.dual.repository.mysql;

import com.example.atomikos.dual.model.mysql.MysqlAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountMysqlRepository extends JpaRepository<MysqlAccount, Long> {

}