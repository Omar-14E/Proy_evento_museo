package com.example.museo_v2.model;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test para la entidad {@link Usuario}.
 * <p>
 * Esta clase no prueba la lógica de negocio, sino que se enfoca en verificar
 * la correcta implementación del contrato {@code equals()} y {@code hashCode()}
 * utilizando la biblioteca EqualsVerifier.
 *
 * @Author Juan
 */
public class UsuarioTest {

    /**
     * Verifica el contrato de {@code equals()} y {@code hashCode()} para la clase {@link Usuario}.
     * <p>
     * Este test asegura que la implementación de {@code equals} y {@code hashCode}
     * (proporcionada por Lombok con {@code @EqualsAndHashCode(of = "nombreUsuario")})
     * cumple con todas las reglas del contrato de Java (reflexividad, simetría,
     * transitividad, consistencia y manejo de nulos).
     *
     * <p>La verificación se configura de la siguiente manera:</p>
     * <ul>
     * <li>
     * <b>{@code .suppress(Warning.NONFINAL_FIELDS)}:</b> Se suprime esta advertencia
     * porque las entidades JPA ({@code @Entity}) no pueden ser {@code final},
     * ni sus campos tampoco, para que el proveedor de persistencia (como Hibernate)
     * pueda crear proxies y gestionar el estado del objeto.
     * </li>
     * <li>
     * <b>{@code .withOnlyTheseFields("nombreUsuario")}:</b> Se especifica
     * explícitamente que <b>solo</b> el campo 'nombreUsuario' debe ser usado
     * para determinar la igualdad. Esto es crucial porque hemos definido
     * 'nombreUsuario' como nuestra "llave de negocio" (business key),
     * ignorando intencionalmente el {@code id} (llave primaria generada)
     * y otros campos mutables.
     * </li>
     * </ul>
     */
    @Test
    void testEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(Usuario.class)
            .suppress(Warning.NONFINAL_FIELDS) 
            .withOnlyTheseFields("nombreUsuario") 
            .verify();
    }
}