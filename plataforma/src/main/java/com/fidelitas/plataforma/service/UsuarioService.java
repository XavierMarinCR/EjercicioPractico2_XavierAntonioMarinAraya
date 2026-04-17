package com.fidelitas.plataforma.service;

import com.fidelitas.plataforma.domain.Usuario;
import com.fidelitas.plataforma.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MailService mailService;
    
    public UsuarioService(UsuarioRepository usuarioRepository, MailService mailService) {
        this.usuarioRepository = usuarioRepository;
        this.mailService = mailService;

    }

    //LISTAR
    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    //OBTENER POR ID
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    //GUARDAR (CREAR O EDITAR)
    @Transactional
    public void save(Usuario usuario) {
        // Verificar si el correo ya existe, excluyendo el usuario actual        
        final Long idUser = usuario.getId();
        Optional<Usuario> usuarioDuplicado = usuarioRepository.findByEmail(usuario.getEmail());       
        if (usuarioDuplicado.isPresent()) {
            Usuario encontrado = usuarioDuplicado.get();

            // Verifica si estamos en modo CREACIÓN (idUser == null) O si el ID encontrado NO es el mismo que estamos actualizando
            if (idUser == null || !encontrado.getId().equals(idUser)) {
                throw new DataIntegrityViolationException("El email ya está en uso por otro usuario.");
            }
        }
        
        boolean esNuevo = (usuario.getId() == null);
        
        usuarioRepository.save(usuario);
        
        if (esNuevo) {
            mailService.enviarCorreoBienvenida(usuario.getEmail(), usuario.getNombre());
        }
    }

    //ELIMINAR
    @Transactional
    public void delete(Long idUsuario) {
        // Verifica si el usuario existe antes de intentar eliminarlo
        if (!usuarioRepository.existsById(idUsuario)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario + " no existe.");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            // Excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException(
                    "No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }

    //CONSULTA DERIVADA
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContaining(nombre);
    }
}
