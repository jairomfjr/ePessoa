package br.gov.epessoa.service;

import br.gov.epessoa.dto.PessoaRequestDTO;
import br.gov.epessoa.dto.PessoaResponseDTO;
import br.gov.epessoa.exception.BusinessException;
import br.gov.epessoa.exception.ResourceNotFoundException;
import br.gov.epessoa.model.Pessoa;
import br.gov.epessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PessoaService {
    
    private final PessoaRepository pessoaRepository;
    
    @Transactional
    public PessoaResponseDTO createPessoa(PessoaRequestDTO request) {
        log.info("Criando nova pessoa com CPF: {}", request.getCpf());
        
        if (pessoaRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
        
        if (pessoaRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        
        Pessoa pessoa = convertToEntity(request);
        Pessoa savedPessoa = pessoaRepository.save(pessoa);
        
        log.info("Pessoa criada com sucesso - ID: {}", savedPessoa.getId());
        return convertToDTO(savedPessoa);
    }
    
    @Transactional(readOnly = true)
    public PessoaResponseDTO getPessoaById(Long id) {
        log.debug("Buscando pessoa por ID: {}", id);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + id));
        
        return convertToDTO(pessoa);
    }
    
    @Transactional(readOnly = true)
    public PessoaResponseDTO getPessoaByCpf(String cpf) {
        log.debug("Buscando pessoa por CPF: {}", cpf);
        
        Pessoa pessoa = pessoaRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com CPF: " + cpf));
        
        return convertToDTO(pessoa);
    }
    
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> getAllPessoas() {
        log.debug("Listando todas as pessoas");
        
        return pessoaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> getPessoasByNome(String nome) {
        log.debug("Buscando pessoas por nome: {}", nome);
        
        return pessoaRepository.findByNomeCompletoContainingIgnoreCase(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> getPessoasAtivas() {
        log.debug("Listando pessoas ativas");
        
        return pessoaRepository.findByAtivo(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PessoaResponseDTO updatePessoa(Long id, PessoaRequestDTO request) {
        log.info("Atualizando pessoa - ID: {}", id);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + id));
        
        // Validar se CPF ou e-mail já existem em outra pessoa
        if (!pessoa.getCpf().equals(request.getCpf()) && pessoaRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado para outra pessoa");
        }
        
        if (!pessoa.getEmail().equals(request.getEmail()) && pessoaRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado para outra pessoa");
        }
        
        updateEntityFromDTO(pessoa, request);
        Pessoa updatedPessoa = pessoaRepository.save(pessoa);
        
        log.info("Pessoa atualizada com sucesso - ID: {}", updatedPessoa.getId());
        return convertToDTO(updatedPessoa);
    }
    
    @Transactional
    public void deletePessoa(Long id) {
        log.info("Deletando pessoa - ID: {}", id);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com ID: " + id));
        
        // Soft delete
        pessoa.setAtivo(false);
        pessoaRepository.save(pessoa);
        
        log.info("Pessoa deletada (soft delete) com sucesso - ID: {}", id);
    }
    
    @Transactional
    public void hardDeletePessoa(Long id) {
        log.info("Deletando permanentemente pessoa - ID: {}", id);
        
        if (!pessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pessoa não encontrada com ID: " + id);
        }
        
        pessoaRepository.deleteById(id);
        log.info("Pessoa deletada permanentemente - ID: {}", id);
    }
    
    private Pessoa convertToEntity(PessoaRequestDTO dto) {
        return Pessoa.builder()
                .nomeCompleto(dto.getNomeCompleto())
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .sexo(dto.getSexo())
                .estadoCivil(dto.getEstadoCivil())
                .cep(dto.getCep())
                .rua(dto.getRua())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .email(dto.getEmail())
                .telefoneFixo(dto.getTelefoneFixo())
                .telefoneCelular(dto.getTelefoneCelular())
                .rendaMensal(dto.getRendaMensal())
                .scoreCredito(dto.getScoreCredito())
                .profissao(dto.getProfissao())
                .banco(dto.getBanco())
                .numeroConta(dto.getNumeroConta())
                .tipoConta(dto.getTipoConta())
                .ativo(true)
                .build();
    }
    
    private PessoaResponseDTO convertToDTO(Pessoa pessoa) {
        return PessoaResponseDTO.builder()
                .id(pessoa.getId())
                .nomeCompleto(pessoa.getNomeCompleto())
                .cpf(pessoa.getCpf())
                .dataNascimento(pessoa.getDataNascimento())
                .sexo(pessoa.getSexo())
                .estadoCivil(pessoa.getEstadoCivil())
                .cep(pessoa.getCep())
                .rua(pessoa.getRua())
                .numero(pessoa.getNumero())
                .complemento(pessoa.getComplemento())
                .bairro(pessoa.getBairro())
                .cidade(pessoa.getCidade())
                .estado(pessoa.getEstado())
                .email(pessoa.getEmail())
                .telefoneFixo(pessoa.getTelefoneFixo())
                .telefoneCelular(pessoa.getTelefoneCelular())
                .rendaMensal(pessoa.getRendaMensal())
                .scoreCredito(pessoa.getScoreCredito())
                .profissao(pessoa.getProfissao())
                .banco(pessoa.getBanco())
                .numeroConta(pessoa.getNumeroConta())
                .tipoConta(pessoa.getTipoConta())
                .ativo(pessoa.getAtivo())
                .createdAt(pessoa.getCreatedAt())
                .updatedAt(pessoa.getUpdatedAt())
                .build();
    }
    
    private void updateEntityFromDTO(Pessoa pessoa, PessoaRequestDTO dto) {
        pessoa.setNomeCompleto(dto.getNomeCompleto());
        pessoa.setCpf(dto.getCpf());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setSexo(dto.getSexo());
        pessoa.setEstadoCivil(dto.getEstadoCivil());
        pessoa.setCep(dto.getCep());
        pessoa.setRua(dto.getRua());
        pessoa.setNumero(dto.getNumero());
        pessoa.setComplemento(dto.getComplemento());
        pessoa.setBairro(dto.getBairro());
        pessoa.setCidade(dto.getCidade());
        pessoa.setEstado(dto.getEstado());
        pessoa.setEmail(dto.getEmail());
        pessoa.setTelefoneFixo(dto.getTelefoneFixo());
        pessoa.setTelefoneCelular(dto.getTelefoneCelular());
        pessoa.setRendaMensal(dto.getRendaMensal());
        pessoa.setScoreCredito(dto.getScoreCredito());
        pessoa.setProfissao(dto.getProfissao());
        pessoa.setBanco(dto.getBanco());
        pessoa.setNumeroConta(dto.getNumeroConta());
        pessoa.setTipoConta(dto.getTipoConta());
    }
}

