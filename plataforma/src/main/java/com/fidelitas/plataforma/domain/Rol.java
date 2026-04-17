package com.fidelitas.plataforma.domain;


import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    // Se recomienda añadir un serialVersionUID
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    // Añadir restricción de longitud y unicidad si el campo 'rol' es el nombre del rol
    @Column(name = "nombre",length = 100, unique = true, nullable = false)
    private String nombre;
}