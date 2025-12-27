package com.eduflow;

import com.eduflow.academic.domain.Cycle;
import com.eduflow.academic.domain.enums.ProgramCycle;
import com.eduflow.academic.repo.CycleRepository;
import com.eduflow.people.domain.Role;
import com.eduflow.people.domain.UserAccount;
import com.eduflow.people.repo.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class UniFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniFlowApplication.class, args);
    }
}