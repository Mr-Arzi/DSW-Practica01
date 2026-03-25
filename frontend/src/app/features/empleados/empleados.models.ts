export interface Empleado {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  email: string;
  activo: boolean;
  departamentoId: number;
  departamentoNombre: string;
}

export interface EmpleadoPayload {
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoId: number;
  email: string;
  password?: string;
}

export interface EmpleadoUpdatePayload {
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoId: number;
}

export interface PaginatedEmpleadoResponse {
  page: number;
  limit: number;
  total: number;
  data: Empleado[];
}
