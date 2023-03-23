import client from 'app/entities/client/client.reducer';
import commande from 'app/entities/commande/commande.reducer';
import boutique from 'app/entities/boutique/boutique.reducer';
import produit from 'app/entities/produit/produit.reducer';
import livreur from 'app/entities/livreur/livreur.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  commande,
  boutique,
  produit,
  livreur,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
