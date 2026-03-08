import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vuelo from './vuelo';
import VueloDetail from './vuelo-detail';
import VueloUpdate from './vuelo-update';
import VueloDeleteDialog from './vuelo-delete-dialog';

const VueloRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Vuelo />} />
    <Route path="new" element={<VueloUpdate />} />
    <Route path=":id">
      <Route index element={<VueloDetail />} />
      <Route path="edit" element={<VueloUpdate />} />
      <Route path="delete" element={<VueloDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VueloRoutes;
