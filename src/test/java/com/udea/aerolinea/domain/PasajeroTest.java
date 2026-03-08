package com.udea.aerolinea.domain;

import static com.udea.aerolinea.domain.PasajeroTestSamples.*;
import static com.udea.aerolinea.domain.ReservaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.aerolinea.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PasajeroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pasajero.class);
        Pasajero pasajero1 = getPasajeroSample1();
        Pasajero pasajero2 = new Pasajero();
        assertThat(pasajero1).isNotEqualTo(pasajero2);

        pasajero2.setId(pasajero1.getId());
        assertThat(pasajero1).isEqualTo(pasajero2);

        pasajero2 = getPasajeroSample2();
        assertThat(pasajero1).isNotEqualTo(pasajero2);
    }

    @Test
    void reservaTest() {
        Pasajero pasajero = getPasajeroRandomSampleGenerator();
        Reserva reservaBack = getReservaRandomSampleGenerator();

        pasajero.addReserva(reservaBack);
        assertThat(pasajero.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getPasajero()).isEqualTo(pasajero);

        pasajero.removeReserva(reservaBack);
        assertThat(pasajero.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getPasajero()).isNull();

        pasajero.reservas(new HashSet<>(Set.of(reservaBack)));
        assertThat(pasajero.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getPasajero()).isEqualTo(pasajero);

        pasajero.setReservas(new HashSet<>());
        assertThat(pasajero.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getPasajero()).isNull();
    }
}
