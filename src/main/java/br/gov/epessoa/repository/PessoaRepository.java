package br.gov.epessoa.repository;

import br.gov.epessoa.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    Optional<Pessoa> findByCpf(String cpf);
    
    Optional<Pessoa> findByEmail(String email);
    
    List<Pessoa> findByNomeCompletoContainingIgnoreCase(String nome);
    
    List<Pessoa> findByAtivo(Boolean ativo);
    
    Boolean existsByCpf(String cpf);
    
    Boolean existsByEmail(String email);
    
    List<Pessoa> findByUsuarioId(Long usuarioId);
}

