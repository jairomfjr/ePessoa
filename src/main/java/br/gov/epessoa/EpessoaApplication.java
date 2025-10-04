package br.gov.epessoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EpessoaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EpessoaApplication.class, args);
    }
}

