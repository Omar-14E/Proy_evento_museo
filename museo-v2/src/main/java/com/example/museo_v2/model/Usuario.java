package com.example.museo_v2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "clave")
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(of = "nombreUsuario")
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    /**
     * Nombres completos del usuario.
     */
    private String nombres;

    /**
     * Nombre de usuario utilizado para iniciar sesión.
     */
    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    /**
     * contraseña del usuario.
     */
    private String clave;
    private String rol;
}