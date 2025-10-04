package br.gov.epessoa.dto;

import br.gov.epessoa.model.Pessoa.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequestDTO {
    
    // Dados Pessoais
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nomeCompleto;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
    
    @NotNull(message = "Sexo é obrigatório")
    private Sexo sexo;
    
    @NotNull(message = "Estado civil é obrigatório")
    private EstadoCivil estadoCivil;
    
    // Endereço
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos numéricos")
    private String cep;
    
    @NotBlank(message = "Rua é obrigatória")
    private String rua;
    
    @NotBlank(message = "Número é obrigatório")
    private String numero;
    
    private String complemento;
    
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;
    
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    
    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser a sigla com 2 letras maiúsculas")
    private String estado;
    
    // Contatos
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    private String email;
    
    @Pattern(regexp = "\\d{10}", message = "Telefone fixo deve conter 10 dígitos")
    private String telefoneFixo;
    
    @NotBlank(message = "Telefone celular é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "Telefone celular deve conter 11 dígitos")
    private String telefoneCelular;
    
    // Dados Financeiros
    @DecimalMin(value = "0.0", inclusive = true, message = "Renda mensal não pode ser negativa")
    private BigDecimal rendaMensal;
    
    @Min(value = 0, message = "Score de crédito deve ser no mínimo 0")
    @Max(value = 1000, message = "Score de crédito deve ser no máximo 1000")
    private Integer scoreCredito;
    
    private String profissao;
    private String banco;
    private String numeroConta;
    private TipoConta tipoConta;
}

