package com.udea.aerolinea.repository;

import com.udea.aerolinea.domain.Vuelo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vuelo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {}
