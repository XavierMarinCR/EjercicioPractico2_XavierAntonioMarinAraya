package com.fidelitas.plataforma.service;

import com.fidelitas.plataforma.domain.Rol;
import com.fidelitas.plataforma.repository.RolRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        var lista = rolRepository.findAll();
        return lista;
    }

    @Transactional(readOnly = true)
    public Rol getRol(Long id) {
        return rolRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Rol con ID " + id+ " no encontrado."));
    }

    @Transactional
    public void save(Rol rol) {

        if (rol.getNombre() == null || rol.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }

        Optional<Rol> existente = rolRepository.findByNombre(rol.getNombre());

        if (existente.isPresent() && (rol.getId() == null || !existente.get().getId().equals(rol.getId()))) {
            throw new IllegalArgumentException("El rol ya existe");
        }

        rolRepository.save(rol);
    }

    @Transactional
    public void delete(Long id) {
        // Verifica si el rol existe antes de intentar eliminarlo
        if (!rolRepository.existsById(id)) {
            // Lanza una excepción para indicar que el rol no fue encontrado
            throw new IllegalArgumentException("El Rol con ID " + id + " no existe.");
        }
        try {
            rolRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el rol. Tiene datos asociados.", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
