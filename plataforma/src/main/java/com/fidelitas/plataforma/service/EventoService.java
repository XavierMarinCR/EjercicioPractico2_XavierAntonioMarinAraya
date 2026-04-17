package com.fidelitas.plataforma.service;

import com.fidelitas.plataforma.domain.Evento;
import com.fidelitas.plataforma.repository.EventoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    //LISTAR
    @Transactional(readOnly = true)
    public List<Evento> getEventos() {
        return eventoRepository.findAll();
    }

    //OBTENER POR ID
    @Transactional(readOnly = true)
    public Optional<Evento> getEvento(Long id) {
        return eventoRepository.findById(id);
    }

    //GUARDAR (CREAR O EDITAR)
    @Transactional
    public void save(Evento evento) {
        eventoRepository.save(evento);
    }

    //ELIMINAR
    @Transactional
    public void delete(Long id) {
        // Verifica si el evento existe antes de intentar eliminarlo
        if (!eventoRepository.existsById(id)) {
            // Lanza una excepción para indicar que el evento no fue encontrado
            throw new IllegalArgumentException(
                    "El evento con ID " + id + " no existe.");
        }
        try {
            eventoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // Excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException(
                    "No se puede eliminar el evento. Tiene datos asociados.", e);
        }
    }

    //CONSULTA DERIVADA
    @Transactional(readOnly = true)
    public List<Evento> buscarPorNombre(String nombre) {
        return eventoRepository.findByNombreContaining(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Evento> buscarActivos() {
        return eventoRepository.findByActivoTrue();
    }
    
    @Transactional(readOnly = true)
    public List<Evento> buscarPorFechas(LocalDate inicio, LocalDate fin) {
        return eventoRepository.findByFechaBetween(inicio, fin);
    }
}
