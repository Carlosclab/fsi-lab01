export interface IAsiento {
  id?: number;
  numero?: string;
  clase?: string;
  disponible?: boolean;
}

export const defaultValue: Readonly<IAsiento> = {
  disponible: false,
};
