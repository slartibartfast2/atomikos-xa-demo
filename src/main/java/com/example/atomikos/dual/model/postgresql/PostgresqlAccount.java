package com.example.atomikos.dual.model.postgresql;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Accounts")
public class PostgresqlAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;
}
