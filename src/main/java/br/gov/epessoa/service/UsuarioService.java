package br.gov.epessoa.service;

import br.gov.epessoa.dto.RegisterRequestDTO;
import br.gov.epessoa.exception.BusinessException;
import br.gov.epessoa.model.Usuario;
import br.gov.epessoa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public Usuario createUsuario(RegisterRequestDTO request) {
        log.info("Criando novo usuário: {}", request.getUsername());
        
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username já está em uso");
        }
        
        if (request.getEmail() != null && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já está em uso");
        }
        
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .role(Usuario.Role.USER)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso: {}", savedUsuario.getUsername());
        
        return savedUsuario;
    }
    
    @Transactional
    public Usuario createOrUpdateUsuarioFromGovbr(String govbrSub, String email, String name) {
        log.info("Criando ou atualizando usuário via Gov.br: {}", govbrSub);
        
        return usuarioRepository.findByGovbrSub(govbrSub)
                .map(usuario -> {
                    usuario.setEmail(email);
                    usuario.setName(name);
                    log.info("Usuário Gov.br atualizado: {}", usuario.getUsername());
                    return usuarioRepository.save(usuario);
                })
                .orElseGet(() -> {
                    Usuario novoUsuario = Usuario.builder()
                            .username(email)
                            .password(passwordEncoder.encode(govbrSub))
                            .name(name)
                            .email(email)
                            .govbrSub(govbrSub)
                            .role(Usuario.Role.USER)
                            .enabled(true)
                            .accountNonExpired(true)
                            .accountNonLocked(true)
                            .credentialsNonExpired(true)
                            .build();
                    
                    Usuario savedUsuario = usuarioRepository.save(novoUsuario);
                    log.info("Novo usuário Gov.br criado: {}", savedUsuario.getUsername());
                    return savedUsuario;
                });
    }
    
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }
}

