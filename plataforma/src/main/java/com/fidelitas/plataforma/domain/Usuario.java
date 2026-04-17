package com.fidelitas.plataforma.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nombre", length = 150)
    private String nombre;
    
    @Column(name = "email", length = 200, unique = true)
    private String email;
    
    private String password;
    
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
        //Relacion de muchos a uno. Varios usuarios pueden tener el mismo rol
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}

