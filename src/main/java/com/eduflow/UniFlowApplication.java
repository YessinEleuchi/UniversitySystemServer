package com.eduflow;

import com.eduflow.academic.domain.Cycle;
import com.eduflow.academic.domain.enums.ProgramCycle;
import com.eduflow.academic.repo.CycleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UniFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniFlowApplication.class, args);
    }

    @Bean
    CommandLineRunner initCycles(CycleRepository repo) {
        return args -> {
            if (repo.count() == 0) {   // avoid duplicate on restart because of unique code
                Cycle c = Cycle.builder()
                        .code("CYC-001")
                        .label(ProgramCycle.LICENCE)   // use one value from your enum
                        .build();

                repo.save(c);
                System.out.println("✅ Saved Cycle with id = " + c.getId());
            }
        };
    }
}
