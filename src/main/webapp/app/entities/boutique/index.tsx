import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Boutique from './boutique';
import BoutiqueDetail from './boutique-detail';
import BoutiqueUpdate from './boutique-update';
import BoutiqueDeleteDialog from './boutique-delete-dialog';

const BoutiqueRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Boutique />} />
    <Route path="new" element={<BoutiqueUpdate />} />
    <Route path=":id">
      <Route index element={<BoutiqueDetail />} />
      <Route path="edit" element={<BoutiqueUpdate />} />
      <Route path="delete" element={<BoutiqueDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BoutiqueRoutes;
