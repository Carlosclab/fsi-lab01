import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Asiento from './asiento';
import AsientoDetail from './asiento-detail';
import AsientoUpdate from './asiento-update';
import AsientoDeleteDialog from './asiento-delete-dialog';

const AsientoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Asiento />} />
    <Route path="new" element={<AsientoUpdate />} />
    <Route path=":id">
      <Route index element={<AsientoDetail />} />
      <Route path="edit" element={<AsientoUpdate />} />
      <Route path="delete" element={<AsientoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AsientoRoutes;
