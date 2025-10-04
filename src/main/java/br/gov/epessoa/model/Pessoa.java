package br.gov.epessoa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pessoas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ==================== DADOS PESSOAIS ====================
    
    @Column(name = "nome_completo", nullable = false, length = 200)
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nomeCompleto;
    
    @Column(nullable = false, unique = true, length = 11)
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;
    
    @Column(name = "data_nascimento", nullable = false)
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Sexo é obrigatório")
    private Sexo sexo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", nullable = false, length = 20)
    @NotNull(message = "Estado civil é obrigatório")
    private EstadoCivil estadoCivil;
    
    // ==================== ENDEREÇO ====================
    
    @Column(nullable = false, length = 8)
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos numéricos")
    private String cep;
    
    @Column(nullable = false, length = 200)
    @NotBlank(message = "Rua é obrigatória")
    private String rua;
    
    @Column(nullable = false, length = 10)
    @NotBlank(message = "Número é obrigatório")
    private String numero;
    
    @Column(length = 100)
    private String complemento;
    
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;
    
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    
    @Column(nullable = false, length = 2)
    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser a sigla com 2 letras maiúsculas")
    private String estado;
    
    // ==================== CONTATOS ====================
    
    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail deve ser válido")
    private String email;
    
    @Column(name = "telefone_fixo", length = 10)
    @Pattern(regexp = "\\d{10}", message = "Telefone fixo deve conter 10 dígitos")
    private String telefoneFixo;
    
    @Column(name = "telefone_celular", nullable = false, length = 11)
    @NotBlank(message = "Telefone celular é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "Telefone celular deve conter 11 dígitos")
    private String telefoneCelular;
    
    // ==================== DADOS FINANCEIROS ====================
    
    @Column(name = "renda_mensal", precision = 15, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true, message = "Renda mensal não pode ser negativa")
    private BigDecimal rendaMensal;
    
    @Column(name = "score_credito")
    @Min(value = 0, message = "Score de crédito deve ser no mínimo 0")
    @Max(value = 1000, message = "Score de crédito deve ser no máximo 1000")
    private Integer scoreCredito;
    
    @Column(length = 100)
    private String profissao;
    
    @Column(length = 100)
    private String banco;
    
    @Column(name = "numero_conta", length = 20)
    private String numeroConta;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", length = 20)
    private TipoConta tipoConta;
    
    // ==================== AUDITORIA ====================
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // ==================== ENUMS ====================
    
    public enum Sexo {
        MASCULINO,
        FEMININO,
        OUTRO,
        NAO_INFORMADO
    }
    
    public enum EstadoCivil {
        SOLTEIRO,
        CASADO,
        DIVORCIADO,
        VIUVO,
        UNIAO_ESTAVEL
    }
    
    public enum TipoConta {
        CORRENTE,
        POUPANCA,
        SALARIO,
        CONJUNTA
    }
}

