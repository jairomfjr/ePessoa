package br.gov.epessoa.config;

import br.gov.epessoa.model.Usuario;
import br.gov.epessoa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    @Profile("!test")
    public CommandLineRunner loadData() {
        return ignoredArgs -> {
            // Criar usuário admin padrão se não existir
            if (!usuarioRepository.existsByUsername("admin")) {
                Usuario admin = Usuario.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .name("Administrador")
                        .email("admin@epessoa.gov.br")
                        .role(Usuario.Role.ADMIN)
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
                
                usuarioRepository.save(admin);
                log.info("Usuário admin criado com sucesso");
                log.info("Username: admin");
                log.info("Password: admin123");
            }
            
            // Criar usuário demo padrão se não existir
            if (!usuarioRepository.existsByUsername("demo")) {
                Usuario demo = Usuario.builder()
                        .username("demo")
                        .password(passwordEncoder.encode("demo123"))
                        .name("Usuário Demo")
                        .email("demo@epessoa.gov.br")
                        .role(Usuario.Role.USER)
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
                
                usuarioRepository.save(demo);
                log.info("Usuário demo criado com sucesso");
                log.info("Username: demo");
                log.info("Password: demo123");
            }
            
            log.info("=".repeat(80));
            log.info("ePessoa Application iniciada com sucesso!");
            log.info("Acesse a documentação da API em: http://localhost:8080/swagger-ui.html");
            log.info("=".repeat(80));
        };
    }
}

