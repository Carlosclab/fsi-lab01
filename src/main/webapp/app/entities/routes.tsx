import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pasajero from './pasajero';
import Reserva from './reserva';
import Vuelo from './vuelo';
import Asiento from './asiento';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="pasajero/*" element={<Pasajero />} />
        <Route path="reserva/*" element={<Reserva />} />
        <Route path="vuelo/*" element={<Vuelo />} />
        <Route path="asiento/*" element={<Asiento />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
