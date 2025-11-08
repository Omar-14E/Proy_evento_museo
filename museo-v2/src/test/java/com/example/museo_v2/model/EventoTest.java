package com.example.museo_v2.model;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test para la entidad {@link Evento}.
 * <p>
 * Esta clase utiliza {@link EqualsVerifier} para asegurar que la
 * implementación del contrato {@code equals()} y {@code hashCode()}
 * de la clase {@link Evento} es correcta.
 */
public class EventoTest {

    /**
     * Verifica el contrato de {@code equals()} y {@code hashCode()} para la clase
     * {@link Evento}.
     * <p>
     * Este test confirma que la igualdad se basa <b>únicamente</b> en el
     * campo 'nombre', que se designa como la "llave de negocio".
     * <p>
     * <b>NOTA:</b> Este test <i>fallará</i> a menos que la clase {@link Evento}
     * sea anotada con {@code @EqualsAndHashCode(of = "nombre")},
     * de forma similar a como se hizo con {@link Sala}.
     * </p>
     */
    @Test
    void testEqualsAndHashCodeContract() {

        EqualsVerifier.forClass(Evento.class)
                .suppress(Warning.NONFINAL_FIELDS) 
                .withOnlyTheseFields("nombre")
                .verify();
    }
}