package com.example.museo_v2.model;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test para la entidad {@link Sala}.
 * <p>
 * Esta clase utiliza {@link EqualsVerifier} para asegurar que la
 * implementación del contrato {@code equals()} y {@code hashCode()}
 * de la clase {@link Sala} es correcta, robusta y cumple con
 * todas las reglas de Java.
 *
 * @Author Juan
 */
public class SalaTest {

    /**
     * Verifica el contrato de {@code equals()} y {@code hashCode()} para la clase {@link Sala}.
     * <p>
     * Este test confirma que la igualdad se basa <b>únicamente</b> en el
     * campo 'nombre', que hemos designado como la "llave de negocio".
     *
     * <p>Configuraciones aplicadas:</p>
     * <ul>
     * <li>
     * <b>{@code .suppress(Warning.NONFINAL_FIELDS)}:</b> Se suprime esta advertencia
     * ya que las entidades JPA (@Entity) no pueden ser 'final' para
     * permitir la instrumentación (proxies) de Hibernate.
     * </li>
     * <li>
     * <b>{@code .withOnlyTheseFields("nombre")}:</b> Se le indica explícitamente
     * a EqualsVerifier que la intención es que <b>solo</b> el campo 'nombre'
     * participe en la lógica de igualdad, ignorando el 'id' y otros campos.
     * </li>
     * </ul>
     */
    @Test
    void testEqualsAndHashCodeContract() {
        
        EqualsVerifier.forClass(Sala.class)
            .suppress(Warning.NONFINAL_FIELDS) 
            .withOnlyTheseFields("nombre") 
            .verify();
    }
}