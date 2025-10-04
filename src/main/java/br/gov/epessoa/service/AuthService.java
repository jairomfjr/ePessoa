package br.gov.epessoa.service;

import br.gov.epessoa.dto.AuthResponseDTO;
import br.gov.epessoa.dto.LoginRequestDTO;
import br.gov.epessoa.dto.RefreshTokenRequestDTO;
import br.gov.epessoa.dto.RegisterRequestDTO;
import br.gov.epessoa.model.Usuario;
import br.gov.epessoa.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtTokenProvider tokenProvider;
    
    public AuthResponseDTO login(LoginRequestDTO request) {
        log.info("Tentando autenticar usuário: {}", request.getUsername());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());
        
        Usuario usuario = usuarioService.findByUsername(request.getUsername());
        
        log.info("Usuário autenticado com sucesso: {}", request.getUsername());
        
        return AuthResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .expiresIn(tokenProvider.getExpirationTime())
                .username(usuario.getUsername())
                .name(usuario.getName())
                .role(usuario.getRole().name())
                .build();
    }
    
    public AuthResponseDTO register(RegisterRequestDTO request) {
        log.info("Registrando novo usuário: {}", request.getUsername());
        
        Usuario usuario = usuarioService.createUsuario(request);
        
        String token = tokenProvider.generateToken(usuario.getUsername());
        String refreshToken = tokenProvider.generateRefreshToken(usuario.getUsername());
        
        log.info("Novo usuário registrado com sucesso: {}", usuario.getUsername());
        
        return AuthResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .expiresIn(tokenProvider.getExpirationTime())
                .username(usuario.getUsername())
                .name(usuario.getName())
                .role(usuario.getRole().name())
                .build();
    }
    
    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        log.info("Renovando token");
        
        String refreshToken = request.getRefreshToken();
        
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            Usuario usuario = usuarioService.findByUsername(username);
            
            String newToken = tokenProvider.generateToken(username);
            String newRefreshToken = tokenProvider.generateRefreshToken(username);
            
            log.info("Token renovado com sucesso para usuário: {}", username);
            
            return AuthResponseDTO.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .type("Bearer")
                    .expiresIn(tokenProvider.getExpirationTime())
                    .username(usuario.getUsername())
                    .name(usuario.getName())
                    .role(usuario.getRole().name())
                    .build();
        }
        
        throw new RuntimeException("Token de refresh inválido");
    }
}

