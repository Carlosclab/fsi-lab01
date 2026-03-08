package com.udea.aerolinea.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reserva getReservaSample1() {
        return new Reserva().id(1L).codigo("codigo1").estado("estado1");
    }

    public static Reserva getReservaSample2() {
        return new Reserva().id(2L).codigo("codigo2").estado("estado2");
    }

    public static Reserva getReservaRandomSampleGenerator() {
        return new Reserva().id(longCount.incrementAndGet()).codigo(UUID.randomUUID().toString()).estado(UUID.randomUUID().toString());
    }
}
