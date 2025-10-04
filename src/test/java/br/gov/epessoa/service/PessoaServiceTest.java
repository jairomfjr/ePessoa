package br.gov.epessoa.service;

import br.gov.epessoa.dto.PessoaRequestDTO;
import br.gov.epessoa.dto.PessoaResponseDTO;
import br.gov.epessoa.exception.BusinessException;
import br.gov.epessoa.exception.ResourceNotFoundException;
import br.gov.epessoa.model.Pessoa;
import br.gov.epessoa.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {
    
    @Mock
    private PessoaRepository pessoaRepository;
    
    @InjectMocks
    private PessoaService pessoaService;
    
    private PessoaRequestDTO pessoaRequestDTO;
    private Pessoa pessoa;
    
    @BeforeEach
    void setUp() {
        pessoaRequestDTO = PessoaRequestDTO.builder()
                .nomeCompleto("João da Silva")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .sexo(Pessoa.Sexo.MASCULINO)
                .estadoCivil(Pessoa.EstadoCivil.SOLTEIRO)
                .cep("12345678")
                .rua("Rua das Flores")
                .numero("123")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .email("joao@email.com")
                .telefoneCelular("11987654321")
                .rendaMensal(new BigDecimal("5000.00"))
                .scoreCredito(750)
                .build();
        
        pessoa = Pessoa.builder()
                .id(1L)
                .nomeCompleto("João da Silva")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .sexo(Pessoa.Sexo.MASCULINO)
                .estadoCivil(Pessoa.EstadoCivil.SOLTEIRO)
                .cep("12345678")
                .rua("Rua das Flores")
                .numero("123")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .email("joao@email.com")
                .telefoneCelular("11987654321")
                .rendaMensal(new BigDecimal("5000.00"))
                .scoreCredito(750)
                .ativo(true)
                .build();
    }
    
    @Test
    @DisplayName("Deve criar uma pessoa com sucesso")
    void shouldCreatePessoaSuccessfully() {
        when(pessoaRepository.existsByCpf(anyString())).thenReturn(false);
        when(pessoaRepository.existsByEmail(anyString())).thenReturn(false);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        
        PessoaResponseDTO response = pessoaService.createPessoa(pessoaRequestDTO);
        
        assertNotNull(response);
        assertEquals("João da Silva", response.getNomeCompleto());
        assertEquals("12345678901", response.getCpf());
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar pessoa com CPF já cadastrado")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        when(pessoaRepository.existsByCpf(anyString())).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> {
            pessoaService.createPessoa(pessoaRequestDTO);
        });
        
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }
    
    @Test
    @DisplayName("Deve buscar pessoa por ID com sucesso")
    void shouldGetPessoaByIdSuccessfully() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        
        PessoaResponseDTO response = pessoaService.getPessoaById(1L);
        
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("João da Silva", response.getNomeCompleto());
        verify(pessoaRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao buscar pessoa inexistente")
    void shouldThrowExceptionWhenPessoaNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.getPessoaById(1L);
        });
    }
    
    @Test
    @DisplayName("Deve buscar pessoa por CPF com sucesso")
    void shouldGetPessoaByCpfSuccessfully() {
        when(pessoaRepository.findByCpf("12345678901")).thenReturn(Optional.of(pessoa));
        
        PessoaResponseDTO response = pessoaService.getPessoaByCpf("12345678901");
        
        assertNotNull(response);
        assertEquals("12345678901", response.getCpf());
        verify(pessoaRepository, times(1)).findByCpf("12345678901");
    }
    
    @Test
    @DisplayName("Deve listar todas as pessoas com sucesso")
    void shouldGetAllPessoasSuccessfully() {
        List<Pessoa> pessoas = Arrays.asList(pessoa);
        when(pessoaRepository.findAll()).thenReturn(pessoas);
        
        List<PessoaResponseDTO> response = pessoaService.getAllPessoas();
        
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(pessoaRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Deve atualizar pessoa com sucesso")
    void shouldUpdatePessoaSuccessfully() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        
        PessoaResponseDTO response = pessoaService.updatePessoa(1L, pessoaRequestDTO);
        
        assertNotNull(response);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }
    
    @Test
    @DisplayName("Deve deletar pessoa com sucesso (soft delete)")
    void shouldDeletePessoaSuccessfully() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
        
        pessoaService.deletePessoa(1L);
        
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }
}

