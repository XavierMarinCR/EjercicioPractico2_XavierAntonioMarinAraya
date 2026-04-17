package com.fidelitas.plataforma.repository;

import com.fidelitas.plataforma.domain.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public List<Usuario> findAll();

    public List<Usuario> findByNombreContaining(String nombre);

    public Optional<Usuario> findByEmail(String email);
}
