package com.example.museo_v2.model;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * Test para la entidad {@link Reserva}.
 * <p>
 * Esta clase utiliza {@link EqualsVerifier} para asegurar que la
 * implementación del contrato {@code equals()} y {@code hashCode()}
 * de la clase {@link Reserva} es correcta.
 */
public class ReservaTest {

    /**
     * Verifica el contrato de {@code equals()} y {@code hashCode()} para la clase
     * {@link Reserva}.
     * <p>
     * Este test confirma que la igualdad se basa <b>únicamente</b> en el
     * campo 'dniRuc', que se designa como la "llave de negocio".
     * <p>
     * <b>NOTA:</b> Este test <i>fallará</i> a menos que la clase {@link Reserva}
     * sea anotada con {@code @EqualsAndHashCode(of = "dniRuc")},
     * de forma similar a como se hizo con {@link Usuario}.
     * </p>
     */
    @Test
    void testEqualsAndHashCodeContract() {

        EqualsVerifier.forClass(Reserva.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withOnlyTheseFields("dniRuc")
                .verify();
    }
}