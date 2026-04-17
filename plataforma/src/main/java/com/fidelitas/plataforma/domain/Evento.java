package com.fidelitas.plataforma.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nombre", length = 150)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    private LocalDate fecha;
    
    private Integer capacidad;
    
    private Boolean activo;
    
}

