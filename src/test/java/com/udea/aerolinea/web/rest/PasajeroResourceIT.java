package com.udea.aerolinea.web.rest;

import static com.udea.aerolinea.domain.PasajeroAsserts.*;
import static com.udea.aerolinea.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.aerolinea.IntegrationTest;
import com.udea.aerolinea.domain.Pasajero;
import com.udea.aerolinea.repository.PasajeroRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PasajeroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PasajeroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/pasajeros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPasajeroMockMvc;

    private Pasajero pasajero;

    private Pasajero insertedPasajero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pasajero createEntity() {
        return new Pasajero()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pasajero createUpdatedEntity() {
        return new Pasajero()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
    }

    @BeforeEach
    void initTest() {
        pasajero = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPasajero != null) {
            pasajeroRepository.delete(insertedPasajero);
            insertedPasajero = null;
        }
    }

    @Test
    @Transactional
    void createPasajero() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pasajero
        var returnedPasajero = om.readValue(
            restPasajeroMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pasajero.class
        );

        // Validate the Pasajero in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPasajeroUpdatableFieldsEquals(returnedPasajero, getPersistedPasajero(returnedPasajero));

        insertedPasajero = returnedPasajero;
    }

    @Test
    @Transactional
    void createPasajeroWithExistingId() throws Exception {
        // Create the Pasajero with an existing ID
        pasajero.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPasajeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero)))
            .andExpect(status().isBadRequest());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasajero.setNombre(null);

        // Create the Pasajero, which fails.

        restPasajeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasajero.setApellido(null);

        // Create the Pasajero, which fails.

        restPasajeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasajero.setEmail(null);

        // Create the Pasajero, which fails.

        restPasajeroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPasajeros() throws Exception {
        // Initialize the database
        insertedPasajero = pasajeroRepository.saveAndFlush(pasajero);

        // Get all the pasajeroList
        restPasajeroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasajero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @Test
    @Transactional
    void getPasajero() throws Exception {
        // Initialize the database
        insertedPasajero = pasajeroRepository.saveAndFlush(pasajero);

        // Get the pasajero
        restPasajeroMockMvc
            .perform(get(ENTITY_API_URL_ID, pasajero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pasajero.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPasajero() throws Exception {
        // Get the pasajero
        restPasajeroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPasajero() throws Exception {
        // Initialize the database
        insertedPasajero = pasajeroRepository.saveAndFlush(pasajero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pasajero
        Pasajero updatedPasajero = pasajeroRepository.findById(pasajero.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPasajero are not directly saved in db
        em.detach(updatedPasajero);
        updatedPasajero
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restPasajeroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPasajero.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPasajero))
            )
            .andExpect(status().isOk());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPasajeroToMatchAllProperties(updatedPasajero);
    }

    @Test
    @Transactional
    void putNonExistingPasajero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasajero.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasajeroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pasajero.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPasajero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasajero.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasajeroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pasajero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPasajero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasajero.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasajeroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasajero)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePasajeroWithPatch() throws Exception {
        // Initialize the database
        insertedPasajero = pasajeroRepository.saveAndFlush(pasajero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pasajero using partial update
        Pasajero partialUpdatedPasajero = new Pasajero();
        partialUpdatedPasajero.setId(pasajero.getId());

        partialUpdatedPasajero.email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO);

        restPasajeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasajero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPasajero))
            )
            .andExpect(status().isOk());

        // Validate the Pasajero in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPasajeroUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPasajero, pasajero), getPersistedPasajero(pasajero));
    }

    @Test
    @Transactional
    void fullUpdatePasajeroWithPatch() throws Exception {
        // Initialize the database
        insertedPasajero = pasajeroRepository.saveAndFlush(pasajero);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pasajero using partial update
        Pasajero partialUpdatedPasajero = new Pasajero();
        partialUpdatedPasajero.setId(pasajero.getId());

        partialUpdatedPasajero
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restPasajeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasajero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPasajero))
            )
            .andExpect(status().isOk());

        // Validate the Pasajero in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPasajeroUpdatableFieldsEquals(partialUpdatedPasajero, getPersistedPasajero(partialUpdatedPasajero));
    }

    @Test
    @Transactional
    void patchNonExistingPasajero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasajero.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasajeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pasajero.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pasajero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPasajero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasajero.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasajeroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pasajero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPasajero() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasajero.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasajeroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pasajero)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pasajero in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePasajero() throws Exception {
        // Initialize the database
        insertedPasajero = pasajeroRepository.saveAndFlush(pasajero);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pasajero
        restPasajeroMockMvc
            .perform(delete(ENTITY_API_URL_ID, pasajero.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pasajeroRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Pasajero getPersistedPasajero(Pasajero pasajero) {
        return pasajeroRepository.findById(pasajero.getId()).orElseThrow();
    }

    protected void assertPersistedPasajeroToMatchAllProperties(Pasajero expectedPasajero) {
        assertPasajeroAllPropertiesEquals(expectedPasajero, getPersistedPasajero(expectedPasajero));
    }

    protected void assertPersistedPasajeroToMatchUpdatableProperties(Pasajero expectedPasajero) {
        assertPasajeroAllUpdatablePropertiesEquals(expectedPasajero, getPersistedPasajero(expectedPasajero));
    }
}
