package br.gov.epessoa.controller;

import br.gov.epessoa.dto.PessoaRequestDTO;
import br.gov.epessoa.dto.PessoaResponseDTO;
import br.gov.epessoa.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pessoas", description = "Endpoints de gerenciamento de pessoas")
@SecurityRequirement(name = "Bearer Authentication")
public class PessoaController {
    
    private final PessoaService pessoaService;
    
    @PostMapping
    @Operation(summary = "Criar nova pessoa", description = "Cadastra uma nova pessoa no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF/e-mail já cadastrados"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<PessoaResponseDTO> createPessoa(@Valid @RequestBody PessoaRequestDTO request) {
        log.info("Requisição para criar nova pessoa com CPF: {}", request.getCpf());
        PessoaResponseDTO response = pessoaService.createPessoa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Listar todas as pessoas", description = "Retorna lista de todas as pessoas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<PessoaResponseDTO>> getAllPessoas() {
        log.info("Requisição para listar todas as pessoas");
        List<PessoaResponseDTO> pessoas = pessoaService.getAllPessoas();
        return ResponseEntity.ok(pessoas);
    }
    
    @GetMapping("/ativas")
    @Operation(summary = "Listar pessoas ativas", description = "Retorna lista de pessoas ativas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<PessoaResponseDTO>> getPessoasAtivas() {
        log.info("Requisição para listar pessoas ativas");
        List<PessoaResponseDTO> pessoas = pessoaService.getPessoasAtivas();
        return ResponseEntity.ok(pessoas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID", description = "Retorna uma pessoa específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<PessoaResponseDTO> getPessoaById(@PathVariable Long id) {
        log.info("Requisição para buscar pessoa por ID: {}", id);
        PessoaResponseDTO pessoa = pessoaService.getPessoaById(id);
        return ResponseEntity.ok(pessoa);
    }
    
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar pessoa por CPF", description = "Retorna uma pessoa específica pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<PessoaResponseDTO> getPessoaByCpf(@PathVariable String cpf) {
        log.info("Requisição para buscar pessoa por CPF: {}", cpf);
        PessoaResponseDTO pessoa = pessoaService.getPessoaByCpf(cpf);
        return ResponseEntity.ok(pessoa);
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar pessoas por nome", description = "Retorna lista de pessoas que contenham o nome especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<List<PessoaResponseDTO>> getPessoasByNome(@RequestParam String nome) {
        log.info("Requisição para buscar pessoas por nome: {}", nome);
        List<PessoaResponseDTO> pessoas = pessoaService.getPessoasByNome(nome);
        return ResponseEntity.ok(pessoas);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa", description = "Atualiza os dados de uma pessoa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<PessoaResponseDTO> updatePessoa(
            @PathVariable Long id,
            @Valid @RequestBody PessoaRequestDTO request) {
        log.info("Requisição para atualizar pessoa - ID: {}", id);
        PessoaResponseDTO response = pessoaService.updatePessoa(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pessoa (soft delete)", description = "Marca uma pessoa como inativa (não remove do banco)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pessoa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
        log.info("Requisição para deletar pessoa - ID: {}", id);
        pessoaService.deletePessoa(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar pessoa permanentemente", description = "Remove permanentemente uma pessoa do banco (apenas ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pessoa deletada permanentemente"),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão (apenas ADMIN)")
    })
    public ResponseEntity<Void> hardDeletePessoa(@PathVariable Long id) {
        log.info("Requisição para deletar permanentemente pessoa - ID: {}", id);
        pessoaService.hardDeletePessoa(id);
        return ResponseEntity.noContent().build();
    }
}

