package br.gov.epessoa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ePessoa API",
                version = "1.0.0",
                description = "API para gerenciamento de cadastro de pessoas com autenticação JWT e Gov.br",
                contact = @Contact(
                        name = "ePessoa Support",
                        email = "suporte@epessoa.gov.br",
                        url = "https://epessoa.gov.br"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor Local"
                ),
                @Server(
                        url = "https://api.epessoa.gov.br",
                        description = "Servidor de Produção"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Insira o token JWT no formato: Bearer {token}",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

