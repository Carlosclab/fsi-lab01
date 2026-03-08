package com.udea.aerolinea.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VueloTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vuelo getVueloSample1() {
        return new Vuelo().id(1L).numeroVuelo("numeroVuelo1").origen("origen1").destino("destino1");
    }

    public static Vuelo getVueloSample2() {
        return new Vuelo().id(2L).numeroVuelo("numeroVuelo2").origen("origen2").destino("destino2");
    }

    public static Vuelo getVueloRandomSampleGenerator() {
        return new Vuelo()
            .id(longCount.incrementAndGet())
            .numeroVuelo(UUID.randomUUID().toString())
            .origen(UUID.randomUUID().toString())
            .destino(UUID.randomUUID().toString());
    }
}
