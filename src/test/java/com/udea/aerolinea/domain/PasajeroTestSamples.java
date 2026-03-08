package com.udea.aerolinea.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PasajeroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pasajero getPasajeroSample1() {
        return new Pasajero().id(1L).nombre("nombre1").apellido("apellido1").email("email1").telefono("telefono1");
    }

    public static Pasajero getPasajeroSample2() {
        return new Pasajero().id(2L).nombre("nombre2").apellido("apellido2").email("email2").telefono("telefono2");
    }

    public static Pasajero getPasajeroRandomSampleGenerator() {
        return new Pasajero()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
