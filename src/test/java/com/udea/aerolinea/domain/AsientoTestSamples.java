package com.udea.aerolinea.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AsientoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Asiento getAsientoSample1() {
        return new Asiento().id(1L).numero("numero1").clase("clase1");
    }

    public static Asiento getAsientoSample2() {
        return new Asiento().id(2L).numero("numero2").clase("clase2");
    }

    public static Asiento getAsientoRandomSampleGenerator() {
        return new Asiento().id(longCount.incrementAndGet()).numero(UUID.randomUUID().toString()).clase(UUID.randomUUID().toString());
    }
}
