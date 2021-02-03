package com.example.atomikos.dual.repository.postgresql;

import com.example.atomikos.dual.model.postgresql.PostgresqlAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountPostgresqlRepository extends JpaRepository<PostgresqlAccount, Long> {

}