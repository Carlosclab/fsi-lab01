import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pasajero from './pasajero';
import PasajeroDetail from './pasajero-detail';
import PasajeroUpdate from './pasajero-update';
import PasajeroDeleteDialog from './pasajero-delete-dialog';

const PasajeroRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pasajero />} />
    <Route path="new" element={<PasajeroUpdate />} />
    <Route path=":id">
      <Route index element={<PasajeroDetail />} />
      <Route path="edit" element={<PasajeroUpdate />} />
      <Route path="delete" element={<PasajeroDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PasajeroRoutes;
