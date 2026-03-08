import dayjs from 'dayjs';
import { IVuelo } from 'app/shared/model/vuelo.model';
import { IAsiento } from 'app/shared/model/asiento.model';
import { IPasajero } from 'app/shared/model/pasajero.model';

export interface IReserva {
  id?: number;
  codigo?: string;
  fechaReserva?: dayjs.Dayjs;
  estado?: string;
  vuelo?: IVuelo | null;
  asiento?: IAsiento | null;
  pasajero?: IPasajero | null;
}

export const defaultValue: Readonly<IReserva> = {};
