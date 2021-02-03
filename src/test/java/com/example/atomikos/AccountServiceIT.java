package com.example.atomikos;

import com.example.atomikos.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceIT {

    @Autowired
    private AccountService accountService;

    private static String today() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    @Test
    public void should_createAccount_with_two_phase_commit() {
        accountService.createAccount("username_" + today());
    }
}