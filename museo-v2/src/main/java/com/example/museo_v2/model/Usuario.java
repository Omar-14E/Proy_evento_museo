package com.example.museo_v2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "usuarios")
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
     * Clave o contraseña del usuario.
     */
    private String clave;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Obtiene el identificador único del usuario.
     * @return el id del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * @param id el id del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene los nombres del usuario.
     * @return los nombres del usuario
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Establece los nombres del usuario.
     * @param nombres los nombres del usuario
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return el nombre de usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre de usuario.
     * @param nombreUsuario el nombre de usuario
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene la clave del usuario.
     * @return la clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * Establece la clave del usuario.
     * @param clave la clave del usuario
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
}
