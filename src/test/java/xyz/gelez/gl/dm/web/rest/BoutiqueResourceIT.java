package xyz.gelez.gl.dm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.gelez.gl.dm.IntegrationTest;
import xyz.gelez.gl.dm.domain.Boutique;
import xyz.gelez.gl.dm.repository.BoutiqueRepository;

/**
 * Integration tests for the {@link BoutiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoutiqueResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/boutiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoutiqueRepository boutiqueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoutiqueMockMvc;

    private Boutique boutique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boutique createEntity(EntityManager em) {
        Boutique boutique = new Boutique().nom(DEFAULT_NOM).adresse(DEFAULT_ADRESSE);
        return boutique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boutique createUpdatedEntity(EntityManager em) {
        Boutique boutique = new Boutique().nom(UPDATED_NOM).adresse(UPDATED_ADRESSE);
        return boutique;
    }

    @BeforeEach
    public void initTest() {
        boutique = createEntity(em);
    }

    @Test
    @Transactional
    void createBoutique() throws Exception {
        int databaseSizeBeforeCreate = boutiqueRepository.findAll().size();
        // Create the Boutique
        restBoutiqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isCreated());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeCreate + 1);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBoutique.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void createBoutiqueWithExistingId() throws Exception {
        // Create the Boutique with an existing ID
        boutique.setId(1L);

        int databaseSizeBeforeCreate = boutiqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoutiqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = boutiqueRepository.findAll().size();
        // set the field null
        boutique.setNom(null);

        // Create the Boutique, which fails.

        restBoutiqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isBadRequest());

        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBoutiques() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        // Get all the boutiqueList
        restBoutiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boutique.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }

    @Test
    @Transactional
    void getBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        // Get the boutique
        restBoutiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, boutique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boutique.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingBoutique() throws Exception {
        // Get the boutique
        restBoutiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Update the boutique
        Boutique updatedBoutique = boutiqueRepository.findById(boutique.getId()).get();
        // Disconnect from session so that the updates on updatedBoutique are not directly saved in db
        em.detach(updatedBoutique);
        updatedBoutique.nom(UPDATED_NOM).adresse(UPDATED_ADRESSE);

        restBoutiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoutique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoutique))
            )
            .andExpect(status().isOk());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBoutique.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void putNonExistingBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();
        boutique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoutiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boutique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boutique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();
        boutique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoutiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boutique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();
        boutique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoutiqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoutiqueWithPatch() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Update the boutique using partial update
        Boutique partialUpdatedBoutique = new Boutique();
        partialUpdatedBoutique.setId(boutique.getId());

        restBoutiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoutique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoutique))
            )
            .andExpect(status().isOk());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBoutique.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void fullUpdateBoutiqueWithPatch() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();

        // Update the boutique using partial update
        Boutique partialUpdatedBoutique = new Boutique();
        partialUpdatedBoutique.setId(boutique.getId());

        partialUpdatedBoutique.nom(UPDATED_NOM).adresse(UPDATED_ADRESSE);

        restBoutiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoutique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoutique))
            )
            .andExpect(status().isOk());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
        Boutique testBoutique = boutiqueList.get(boutiqueList.size() - 1);
        assertThat(testBoutique.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBoutique.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void patchNonExistingBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();
        boutique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoutiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boutique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boutique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();
        boutique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoutiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boutique))
            )
            .andExpect(status().isBadRequest());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoutique() throws Exception {
        int databaseSizeBeforeUpdate = boutiqueRepository.findAll().size();
        boutique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoutiqueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(boutique)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Boutique in the database
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoutique() throws Exception {
        // Initialize the database
        boutiqueRepository.saveAndFlush(boutique);

        int databaseSizeBeforeDelete = boutiqueRepository.findAll().size();

        // Delete the boutique
        restBoutiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, boutique.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Boutique> boutiqueList = boutiqueRepository.findAll();
        assertThat(boutiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
