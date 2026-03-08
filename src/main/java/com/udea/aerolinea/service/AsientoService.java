package com.udea.aerolinea.service;

import com.udea.aerolinea.domain.Asiento;
import com.udea.aerolinea.repository.AsientoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.udea.aerolinea.domain.Asiento}.
 */
@Service
@Transactional
public class AsientoService {

    private static final Logger LOG = LoggerFactory.getLogger(AsientoService.class);

    private final AsientoRepository asientoRepository;

    public AsientoService(AsientoRepository asientoRepository) {
        this.asientoRepository = asientoRepository;
    }

    /**
     * Save a asiento.
     *
     * @param asiento the entity to save.
     * @return the persisted entity.
     */
    public Asiento save(Asiento asiento) {
        LOG.debug("Request to save Asiento : {}", asiento);
        return asientoRepository.save(asiento);
    }

    /**
     * Update a asiento.
     *
     * @param asiento the entity to save.
     * @return the persisted entity.
     */
    public Asiento update(Asiento asiento) {
        LOG.debug("Request to update Asiento : {}", asiento);
        return asientoRepository.save(asiento);
    }

    /**
     * Partially update a asiento.
     *
     * @param asiento the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Asiento> partialUpdate(Asiento asiento) {
        LOG.debug("Request to partially update Asiento : {}", asiento);

        return asientoRepository
            .findById(asiento.getId())
            .map(existingAsiento -> {
                if (asiento.getNumero() != null) {
                    existingAsiento.setNumero(asiento.getNumero());
                }
                if (asiento.getClase() != null) {
                    existingAsiento.setClase(asiento.getClase());
                }
                if (asiento.getDisponible() != null) {
                    existingAsiento.setDisponible(asiento.getDisponible());
                }

                return existingAsiento;
            })
            .map(asientoRepository::save);
    }

    /**
     * Get all the asientos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Asiento> findAll(Pageable pageable) {
        LOG.debug("Request to get all Asientos");
        return asientoRepository.findAll(pageable);
    }

    /**
     * Get one asiento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Asiento> findOne(Long id) {
        LOG.debug("Request to get Asiento : {}", id);
        return asientoRepository.findById(id);
    }

    /**
     * Delete the asiento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Asiento : {}", id);
        asientoRepository.deleteById(id);
    }
}
