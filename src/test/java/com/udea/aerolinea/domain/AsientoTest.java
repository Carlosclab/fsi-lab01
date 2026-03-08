package com.udea.aerolinea.domain;

import static com.udea.aerolinea.domain.AsientoTestSamples.*;
import static com.udea.aerolinea.domain.ReservaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.aerolinea.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AsientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Asiento.class);
        Asiento asiento1 = getAsientoSample1();
        Asiento asiento2 = new Asiento();
        assertThat(asiento1).isNotEqualTo(asiento2);

        asiento2.setId(asiento1.getId());
        assertThat(asiento1).isEqualTo(asiento2);

        asiento2 = getAsientoSample2();
        assertThat(asiento1).isNotEqualTo(asiento2);
    }

    @Test
    void reservaTest() {
        Asiento asiento = getAsientoRandomSampleGenerator();
        Reserva reservaBack = getReservaRandomSampleGenerator();

        asiento.addReserva(reservaBack);
        assertThat(asiento.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getAsiento()).isEqualTo(asiento);

        asiento.removeReserva(reservaBack);
        assertThat(asiento.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getAsiento()).isNull();

        asiento.reservas(new HashSet<>(Set.of(reservaBack)));
        assertThat(asiento.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getAsiento()).isEqualTo(asiento);

        asiento.setReservas(new HashSet<>());
        assertThat(asiento.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getAsiento()).isNull();
    }
}
