package com.udea.aerolinea.domain;

import static com.udea.aerolinea.domain.ReservaTestSamples.*;
import static com.udea.aerolinea.domain.VueloTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.udea.aerolinea.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VueloTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vuelo.class);
        Vuelo vuelo1 = getVueloSample1();
        Vuelo vuelo2 = new Vuelo();
        assertThat(vuelo1).isNotEqualTo(vuelo2);

        vuelo2.setId(vuelo1.getId());
        assertThat(vuelo1).isEqualTo(vuelo2);

        vuelo2 = getVueloSample2();
        assertThat(vuelo1).isNotEqualTo(vuelo2);
    }

    @Test
    void reservaTest() {
        Vuelo vuelo = getVueloRandomSampleGenerator();
        Reserva reservaBack = getReservaRandomSampleGenerator();

        vuelo.addReserva(reservaBack);
        assertThat(vuelo.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getVuelo()).isEqualTo(vuelo);

        vuelo.removeReserva(reservaBack);
        assertThat(vuelo.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getVuelo()).isNull();

        vuelo.reservas(new HashSet<>(Set.of(reservaBack)));
        assertThat(vuelo.getReservas()).containsOnly(reservaBack);
        assertThat(reservaBack.getVuelo()).isEqualTo(vuelo);

        vuelo.setReservas(new HashSet<>());
        assertThat(vuelo.getReservas()).doesNotContain(reservaBack);
        assertThat(reservaBack.getVuelo()).isNull();
    }
}
