package xyz.gelez.gl.dm.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.gelez.gl.dm.domain.Boutique;
import xyz.gelez.gl.dm.repository.BoutiqueRepository;
import xyz.gelez.gl.dm.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.gelez.gl.dm.domain.Boutique}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BoutiqueResource {

    private final Logger log = LoggerFactory.getLogger(BoutiqueResource.class);

    private static final String ENTITY_NAME = "boutique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoutiqueRepository boutiqueRepository;

    public BoutiqueResource(BoutiqueRepository boutiqueRepository) {
        this.boutiqueRepository = boutiqueRepository;
    }

    /**
     * {@code POST  /boutiques} : Create a new boutique.
     *
     * @param boutique the boutique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boutique, or with status {@code 400 (Bad Request)} if the boutique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boutiques")
    public ResponseEntity<Boutique> createBoutique(@Valid @RequestBody Boutique boutique) throws URISyntaxException {
        log.debug("REST request to save Boutique : {}", boutique);
        if (boutique.getId() != null) {
            throw new BadRequestAlertException("A new boutique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Boutique result = boutiqueRepository.save(boutique);
        return ResponseEntity
            .created(new URI("/api/boutiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /boutiques/:id} : Updates an existing boutique.
     *
     * @param id the id of the boutique to save.
     * @param boutique the boutique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boutique,
     * or with status {@code 400 (Bad Request)} if the boutique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boutique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boutiques/{id}")
    public ResponseEntity<Boutique> updateBoutique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Boutique boutique
    ) throws URISyntaxException {
        log.debug("REST request to update Boutique : {}, {}", id, boutique);
        if (boutique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boutique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boutiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Boutique result = boutiqueRepository.save(boutique);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boutique.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /boutiques/:id} : Partial updates given fields of an existing boutique, field will ignore if it is null
     *
     * @param id the id of the boutique to save.
     * @param boutique the boutique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boutique,
     * or with status {@code 400 (Bad Request)} if the boutique is not valid,
     * or with status {@code 404 (Not Found)} if the boutique is not found,
     * or with status {@code 500 (Internal Server Error)} if the boutique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boutiques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Boutique> partialUpdateBoutique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Boutique boutique
    ) throws URISyntaxException {
        log.debug("REST request to partial update Boutique partially : {}, {}", id, boutique);
        if (boutique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boutique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boutiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Boutique> result = boutiqueRepository
            .findById(boutique.getId())
            .map(existingBoutique -> {
                if (boutique.getNom() != null) {
                    existingBoutique.setNom(boutique.getNom());
                }
                if (boutique.getAdresse() != null) {
                    existingBoutique.setAdresse(boutique.getAdresse());
                }

                return existingBoutique;
            })
            .map(boutiqueRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boutique.getId().toString())
        );
    }

    /**
     * {@code GET  /boutiques} : get all the boutiques.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boutiques in body.
     */
    @GetMapping("/boutiques")
    public List<Boutique> getAllBoutiques() {
        log.debug("REST request to get all Boutiques");
        return boutiqueRepository.findAll();
    }

    /**
     * {@code GET  /boutiques/:id} : get the "id" boutique.
     *
     * @param id the id of the boutique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boutique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boutiques/{id}")
    public ResponseEntity<Boutique> getBoutique(@PathVariable Long id) {
        log.debug("REST request to get Boutique : {}", id);
        Optional<Boutique> boutique = boutiqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(boutique);
    }

    /**
     * {@code DELETE  /boutiques/:id} : delete the "id" boutique.
     *
     * @param id the id of the boutique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boutiques/{id}")
    public ResponseEntity<Void> deleteBoutique(@PathVariable Long id) {
        log.debug("REST request to delete Boutique : {}", id);
        boutiqueRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
