import dayjs from 'dayjs';

export interface IVuelo {
  id?: number;
  numeroVuelo?: string;
  origen?: string;
  destino?: string;
  fechaSalida?: dayjs.Dayjs;
  fechaLlegada?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IVuelo> = {};
