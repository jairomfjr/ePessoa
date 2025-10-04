package br.gov.epessoa.controller;

import br.gov.epessoa.dto.PessoaRequestDTO;
import br.gov.epessoa.dto.PessoaResponseDTO;
import br.gov.epessoa.model.Pessoa;
import br.gov.epessoa.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PessoaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PessoaControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private PessoaService pessoaService;
    
    private PessoaRequestDTO pessoaRequestDTO;
    private PessoaResponseDTO pessoaResponseDTO;
    
    @BeforeEach
    void setUp() {
        pessoaRequestDTO = PessoaRequestDTO.builder()
                .nomeCompleto("Maria Santos")
                .cpf("98765432100")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .sexo(Pessoa.Sexo.FEMININO)
                .estadoCivil(Pessoa.EstadoCivil.CASADO)
                .cep("87654321")
                .rua("Avenida Paulista")
                .numero("1000")
                .bairro("Bela Vista")
                .cidade("São Paulo")
                .estado("SP")
                .email("maria@email.com")
                .telefoneCelular("11912345678")
                .rendaMensal(new BigDecimal("7500.00"))
                .scoreCredito(820)
                .build();
        
        pessoaResponseDTO = PessoaResponseDTO.builder()
                .id(1L)
                .nomeCompleto("Maria Santos")
                .cpf("98765432100")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .sexo(Pessoa.Sexo.FEMININO)
                .estadoCivil(Pessoa.EstadoCivil.CASADO)
                .cep("87654321")
                .rua("Avenida Paulista")
                .numero("1000")
                .bairro("Bela Vista")
                .cidade("São Paulo")
                .estado("SP")
                .email("maria@email.com")
                .telefoneCelular("11912345678")
                .rendaMensal(new BigDecimal("7500.00"))
                .scoreCredito(820)
                .ativo(true)
                .build();
    }
    
    @Test
    @WithMockUser
    @DisplayName("POST /api/pessoas - Deve criar pessoa com sucesso")
    void shouldCreatePessoaSuccessfully() throws Exception {
        when(pessoaService.createPessoa(any(PessoaRequestDTO.class))).thenReturn(pessoaResponseDTO);
        
        mockMvc.perform(post("/api/pessoas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Santos"))
                .andExpect(jsonPath("$.cpf").value("98765432100"));
    }
    
    @Test
    @WithMockUser
    @DisplayName("GET /api/pessoas - Deve listar todas as pessoas")
    void shouldGetAllPessoas() throws Exception {
        List<PessoaResponseDTO> pessoas = Arrays.asList(pessoaResponseDTO);
        when(pessoaService.getAllPessoas()).thenReturn(pessoas);
        
        mockMvc.perform(get("/api/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nomeCompleto").value("Maria Santos"));
    }
    
    @Test
    @WithMockUser
    @DisplayName("GET /api/pessoas/{id} - Deve buscar pessoa por ID")
    void shouldGetPessoaById() throws Exception {
        when(pessoaService.getPessoaById(anyLong())).thenReturn(pessoaResponseDTO);
        
        mockMvc.perform(get("/api/pessoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeCompleto").value("Maria Santos"));
    }
    
    @Test
    @WithMockUser
    @DisplayName("PUT /api/pessoas/{id} - Deve atualizar pessoa")
    void shouldUpdatePessoa() throws Exception {
        when(pessoaService.updatePessoa(anyLong(), any(PessoaRequestDTO.class)))
                .thenReturn(pessoaResponseDTO);
        
        mockMvc.perform(put("/api/pessoas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
    
    @Test
    @WithMockUser
    @DisplayName("DELETE /api/pessoas/{id} - Deve deletar pessoa")
    void shouldDeletePessoa() throws Exception {
        mockMvc.perform(delete("/api/pessoas/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}

