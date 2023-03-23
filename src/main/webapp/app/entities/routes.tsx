import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Client from './client';
import Commande from './commande';
import Boutique from './boutique';
import Produit from './produit';
import Livreur from './livreur';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="client/*" element={<Client />} />
        <Route path="commande/*" element={<Commande />} />
        <Route path="boutique/*" element={<Boutique />} />
        <Route path="produit/*" element={<Produit />} />
        <Route path="livreur/*" element={<Livreur />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
