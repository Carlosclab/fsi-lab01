package com.udea.aerolinea.web.rest;

import com.udea.aerolinea.domain.Pasajero;
import com.udea.aerolinea.repository.PasajeroRepository;
import com.udea.aerolinea.service.PasajeroService;
import com.udea.aerolinea.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.udea.aerolinea.domain.Pasajero}.
 */
@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroResource {

    private static final Logger LOG = LoggerFactory.getLogger(PasajeroResource.class);

    private static final String ENTITY_NAME = "pasajero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasajeroService pasajeroService;

    private final PasajeroRepository pasajeroRepository;

    public PasajeroResource(PasajeroService pasajeroService, PasajeroRepository pasajeroRepository) {
        this.pasajeroService = pasajeroService;
        this.pasajeroRepository = pasajeroRepository;
    }

    /**
     * {@code POST  /pasajeros} : Create a new pasajero.
     *
     * @param pasajero the pasajero to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pasajero, or with status {@code 400 (Bad Request)} if the pasajero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pasajero> createPasajero(@Valid @RequestBody Pasajero pasajero) throws URISyntaxException {
        LOG.debug("REST request to save Pasajero : {}", pasajero);
        if (pasajero.getId() != null) {
            throw new BadRequestAlertException("A new pasajero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pasajero = pasajeroService.save(pasajero);
        return ResponseEntity.created(new URI("/api/pasajeros/" + pasajero.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pasajero.getId().toString()))
            .body(pasajero);
    }

    /**
     * {@code PUT  /pasajeros/:id} : Updates an existing pasajero.
     *
     * @param id the id of the pasajero to save.
     * @param pasajero the pasajero to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasajero,
     * or with status {@code 400 (Bad Request)} if the pasajero is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pasajero couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pasajero> updatePasajero(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pasajero pasajero
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pasajero : {}, {}", id, pasajero);
        if (pasajero.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasajero.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasajeroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pasajero = pasajeroService.update(pasajero);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasajero.getId().toString()))
            .body(pasajero);
    }

    /**
     * {@code PATCH  /pasajeros/:id} : Partial updates given fields of an existing pasajero, field will ignore if it is null
     *
     * @param id the id of the pasajero to save.
     * @param pasajero the pasajero to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasajero,
     * or with status {@code 400 (Bad Request)} if the pasajero is not valid,
     * or with status {@code 404 (Not Found)} if the pasajero is not found,
     * or with status {@code 500 (Internal Server Error)} if the pasajero couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pasajero> partialUpdatePasajero(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pasajero pasajero
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Pasajero partially : {}, {}", id, pasajero);
        if (pasajero.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasajero.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasajeroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pasajero> result = pasajeroService.partialUpdate(pasajero);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasajero.getId().toString())
        );
    }

    /**
     * {@code GET  /pasajeros} : get all the pasajeros.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pasajeros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Pasajero>> getAllPasajeros(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Pasajeros");
        Page<Pasajero> page = pasajeroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pasajeros/:id} : get the "id" pasajero.
     *
     * @param id the id of the pasajero to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pasajero, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pasajero> getPasajero(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pasajero : {}", id);
        Optional<Pasajero> pasajero = pasajeroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pasajero);
    }

    /**
     * {@code DELETE  /pasajeros/:id} : delete the "id" pasajero.
     *
     * @param id the id of the pasajero to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasajero(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pasajero : {}", id);
        pasajeroService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
