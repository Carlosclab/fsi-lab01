package com.udea.aerolinea.web.rest;

import static com.udea.aerolinea.domain.ReservaAsserts.*;
import static com.udea.aerolinea.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.aerolinea.IntegrationTest;
import com.udea.aerolinea.domain.Reserva;
import com.udea.aerolinea.repository.ReservaRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ReservaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservaResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_RESERVA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_RESERVA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reservas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservaMockMvc;

    private Reserva reserva;

    private Reserva insertedReserva;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reserva createEntity() {
        return new Reserva().codigo(DEFAULT_CODIGO).fechaReserva(DEFAULT_FECHA_RESERVA).estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reserva createUpdatedEntity() {
        return new Reserva().codigo(UPDATED_CODIGO).fechaReserva(UPDATED_FECHA_RESERVA).estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        reserva = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReserva != null) {
            reservaRepository.delete(insertedReserva);
            insertedReserva = null;
        }
    }

    @Test
    @Transactional
    void createReserva() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reserva
        var returnedReserva = om.readValue(
            restReservaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Reserva.class
        );

        // Validate the Reserva in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReservaUpdatableFieldsEquals(returnedReserva, getPersistedReserva(returnedReserva));

        insertedReserva = returnedReserva;
    }

    @Test
    @Transactional
    void createReservaWithExistingId() throws Exception {
        // Create the Reserva with an existing ID
        reserva.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reserva.setCodigo(null);

        // Create the Reserva, which fails.

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaReservaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reserva.setFechaReserva(null);

        // Create the Reserva, which fails.

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reserva.setEstado(null);

        // Create the Reserva, which fails.

        restReservaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservas() throws Exception {
        // Initialize the database
        insertedReserva = reservaRepository.saveAndFlush(reserva);

        // Get all the reservaList
        restReservaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reserva.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].fechaReserva").value(hasItem(DEFAULT_FECHA_RESERVA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getReserva() throws Exception {
        // Initialize the database
        insertedReserva = reservaRepository.saveAndFlush(reserva);

        // Get the reserva
        restReservaMockMvc
            .perform(get(ENTITY_API_URL_ID, reserva.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reserva.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.fechaReserva").value(DEFAULT_FECHA_RESERVA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingReserva() throws Exception {
        // Get the reserva
        restReservaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReserva() throws Exception {
        // Initialize the database
        insertedReserva = reservaRepository.saveAndFlush(reserva);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reserva
        Reserva updatedReserva = reservaRepository.findById(reserva.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReserva are not directly saved in db
        em.detach(updatedReserva);
        updatedReserva.codigo(UPDATED_CODIGO).fechaReserva(UPDATED_FECHA_RESERVA).estado(UPDATED_ESTADO);

        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReserva.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReservaToMatchAllProperties(updatedReserva);
    }

    @Test
    @Transactional
    void putNonExistingReserva() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reserva.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(put(ENTITY_API_URL_ID, reserva.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReserva() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reserva.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReserva() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reserva.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        insertedReserva = reservaRepository.saveAndFlush(reserva);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva.codigo(UPDATED_CODIGO);

        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReserva, reserva), getPersistedReserva(reserva));
    }

    @Test
    @Transactional
    void fullUpdateReservaWithPatch() throws Exception {
        // Initialize the database
        insertedReserva = reservaRepository.saveAndFlush(reserva);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reserva using partial update
        Reserva partialUpdatedReserva = new Reserva();
        partialUpdatedReserva.setId(reserva.getId());

        partialUpdatedReserva.codigo(UPDATED_CODIGO).fechaReserva(UPDATED_FECHA_RESERVA).estado(UPDATED_ESTADO);

        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReserva.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReserva))
            )
            .andExpect(status().isOk());

        // Validate the Reserva in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservaUpdatableFieldsEquals(partialUpdatedReserva, getPersistedReserva(partialUpdatedReserva));
    }

    @Test
    @Transactional
    void patchNonExistingReserva() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reserva.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reserva.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReserva() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reserva.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reserva))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReserva() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reserva.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reserva)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reserva in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReserva() throws Exception {
        // Initialize the database
        insertedReserva = reservaRepository.saveAndFlush(reserva);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reserva
        restReservaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reserva.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reservaRepository.count();
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

    protected Reserva getPersistedReserva(Reserva reserva) {
        return reservaRepository.findById(reserva.getId()).orElseThrow();
    }

    protected void assertPersistedReservaToMatchAllProperties(Reserva expectedReserva) {
        assertReservaAllPropertiesEquals(expectedReserva, getPersistedReserva(expectedReserva));
    }

    protected void assertPersistedReservaToMatchUpdatableProperties(Reserva expectedReserva) {
        assertReservaAllUpdatablePropertiesEquals(expectedReserva, getPersistedReserva(expectedReserva));
    }
}
