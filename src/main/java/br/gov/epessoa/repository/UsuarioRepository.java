package br.gov.epessoa.repository;

import br.gov.epessoa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByGovbrSub(String govbrSub);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
}

