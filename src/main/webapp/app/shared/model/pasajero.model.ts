import dayjs from 'dayjs';

export interface IPasajero {
  id?: number;
  nombre?: string;
  apellido?: string;
  email?: string;
  telefono?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPasajero> = {};
