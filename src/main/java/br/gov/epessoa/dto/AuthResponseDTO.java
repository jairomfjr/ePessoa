package br.gov.epessoa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    
    private String token;
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";
    private Long expiresIn;
    private String username;
    private String name;
    private String role;
}

