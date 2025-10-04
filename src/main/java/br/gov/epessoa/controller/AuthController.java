package br.gov.epessoa.controller;

import br.gov.epessoa.dto.AuthResponseDTO;
import br.gov.epessoa.dto.LoginRequestDTO;
import br.gov.epessoa.dto.RefreshTokenRequestDTO;
import br.gov.epessoa.dto.RegisterRequestDTO;
import br.gov.epessoa.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro de usuários")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna tokens JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Requisição de login para usuário: {}", request.getUsername());
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "Registro de novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existe")
    })
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Requisição de registro para usuário: {}", request.getUsername());
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Renovar token", description = "Renova o token JWT usando o refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido")
    })
    public ResponseEntity<AuthResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        log.info("Requisição de renovação de token");
        AuthResponseDTO response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/oauth2/success")
    @Operation(summary = "Callback OAuth2", description = "Endpoint de callback após autenticação via Gov.br")
    public ResponseEntity<AuthResponseDTO> oauth2Success(
            @RequestParam String token,
            @RequestParam String refreshToken) {
        log.info("Callback OAuth2 recebido");
        
        // Em produção, você pode querer redirecionar para o frontend com os tokens
        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();
        
        return ResponseEntity.ok(response);
    }
}

