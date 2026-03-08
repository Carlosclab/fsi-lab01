import pasajero from 'app/entities/pasajero/pasajero.reducer';
import reserva from 'app/entities/reserva/reserva.reducer';
import vuelo from 'app/entities/vuelo/vuelo.reducer';
import asiento from 'app/entities/asiento/asiento.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  pasajero,
  reserva,
  vuelo,
  asiento,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
