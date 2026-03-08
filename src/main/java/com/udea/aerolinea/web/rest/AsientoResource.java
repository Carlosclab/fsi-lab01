package com.udea.aerolinea.web.rest;

import com.udea.aerolinea.domain.Asiento;
import com.udea.aerolinea.repository.AsientoRepository;
import com.udea.aerolinea.service.AsientoService;
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
 * REST controller for managing {@link com.udea.aerolinea.domain.Asiento}.
 */
@RestController
@RequestMapping("/api/asientos")
public class AsientoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsientoResource.class);

    private static final String ENTITY_NAME = "asiento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AsientoService asientoService;

    private final AsientoRepository asientoRepository;

    public AsientoResource(AsientoService asientoService, AsientoRepository asientoRepository) {
        this.asientoService = asientoService;
        this.asientoRepository = asientoRepository;
    }

    /**
     * {@code POST  /asientos} : Create a new asiento.
     *
     * @param asiento the asiento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asiento, or with status {@code 400 (Bad Request)} if the asiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Asiento> createAsiento(@Valid @RequestBody Asiento asiento) throws URISyntaxException {
        LOG.debug("REST request to save Asiento : {}", asiento);
        if (asiento.getId() != null) {
            throw new BadRequestAlertException("A new asiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        asiento = asientoService.save(asiento);
        return ResponseEntity.created(new URI("/api/asientos/" + asiento.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, asiento.getId().toString()))
            .body(asiento);
    }

    /**
     * {@code PUT  /asientos/:id} : Updates an existing asiento.
     *
     * @param id the id of the asiento to save.
     * @param asiento the asiento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asiento,
     * or with status {@code 400 (Bad Request)} if the asiento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asiento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Asiento> updateAsiento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Asiento asiento
    ) throws URISyntaxException {
        LOG.debug("REST request to update Asiento : {}, {}", id, asiento);
        if (asiento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asiento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        asiento = asientoService.update(asiento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asiento.getId().toString()))
            .body(asiento);
    }

    /**
     * {@code PATCH  /asientos/:id} : Partial updates given fields of an existing asiento, field will ignore if it is null
     *
     * @param id the id of the asiento to save.
     * @param asiento the asiento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asiento,
     * or with status {@code 400 (Bad Request)} if the asiento is not valid,
     * or with status {@code 404 (Not Found)} if the asiento is not found,
     * or with status {@code 500 (Internal Server Error)} if the asiento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Asiento> partialUpdateAsiento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Asiento asiento
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Asiento partially : {}, {}", id, asiento);
        if (asiento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asiento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Asiento> result = asientoService.partialUpdate(asiento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asiento.getId().toString())
        );
    }

    /**
     * {@code GET  /asientos} : get all the asientos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asientos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Asiento>> getAllAsientos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Asientos");
        Page<Asiento> page = asientoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asientos/:id} : get the "id" asiento.
     *
     * @param id the id of the asiento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asiento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Asiento> getAsiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Asiento : {}", id);
        Optional<Asiento> asiento = asientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asiento);
    }

    /**
     * {@code DELETE  /asientos/:id} : delete the "id" asiento.
     *
     * @param id the id of the asiento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsiento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Asiento : {}", id);
        asientoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
