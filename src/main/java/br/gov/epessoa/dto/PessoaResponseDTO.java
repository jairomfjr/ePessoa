package br.gov.epessoa.dto;

import br.gov.epessoa.model.Pessoa.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseDTO {
    
    private Long id;
    
    // Dados Pessoais
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private EstadoCivil estadoCivil;
    
    // Endereço
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    
    // Contatos
    private String email;
    private String telefoneFixo;
    private String telefoneCelular;
    
    // Dados Financeiros
    private BigDecimal rendaMensal;
    private Integer scoreCredito;
    private String profissao;
    private String banco;
    private String numeroConta;
    private TipoConta tipoConta;
    
    // Auditoria
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

