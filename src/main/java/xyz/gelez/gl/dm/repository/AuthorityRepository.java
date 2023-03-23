package xyz.gelez.gl.dm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.gelez.gl.dm.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
