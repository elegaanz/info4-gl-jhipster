package xyz.gelez.gl.dm.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.gelez.gl.dm.domain.Commande;

public interface CommandeRepositoryWithBagRelationships {
    Optional<Commande> fetchBagRelationships(Optional<Commande> commande);

    List<Commande> fetchBagRelationships(List<Commande> commandes);

    Page<Commande> fetchBagRelationships(Page<Commande> commandes);
}
