package xyz.gelez.gl.dm.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.gelez.gl.dm.domain.Boutique;

/**
 * Spring Data JPA repository for the Boutique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoutiqueRepository extends JpaRepository<Boutique, Long> {}
