package com.fidelitas.plataforma.repository;

import com.fidelitas.plataforma.domain.Evento;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    public List<Evento> findAll();

    public List<Evento> findByActivoTrue();

    public List<Evento> findByFechaBetween(LocalDate inicio, LocalDate fin);

    public List<Evento> findByNombreContaining(String nombre);
}
