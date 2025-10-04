package br.gov.epessoa.security;

import br.gov.epessoa.model.Usuario;
import br.gov.epessoa.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            
            String sub = oauth2User.getAttribute("sub");
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            
            log.info("Autenticação Gov.br bem-sucedida para: {}", email);
            
            // Criar ou atualizar usuário
            Usuario usuario = usuarioService.createOrUpdateUsuarioFromGovbr(sub, email, name);
            
            // Gerar tokens JWT
            String token = tokenProvider.generateToken(usuario.getUsername());
            String refreshToken = tokenProvider.generateRefreshToken(usuario.getUsername());
            
            // Redirecionar com o token na URL (em produção, usar cookies seguros ou outro método)
            String targetUrl = UriComponentsBuilder.fromUriString("/api/auth/oauth2/success")
                    .queryParam("token", token)
                    .queryParam("refreshToken", refreshToken)
                    .build().toUriString();
            
            log.info("Redirecionando para: {}", targetUrl);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}

