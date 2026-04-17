package com.fidelitas.plataforma.service;

import com.fidelitas.plataforma.domain.Usuario;
import com.fidelitas.plataforma.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Se cargan los roles de usuario
        var roles = List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre())
        );

        //Se retorna el usuario con la información en él
        return new User( usuario.getEmail(), usuario.getPassword(), roles);
    }
}
