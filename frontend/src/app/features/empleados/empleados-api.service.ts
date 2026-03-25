import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiBaseService } from '../../core/api/api-base.service';
import {
  Empleado,
  EmpleadoPayload,
  EmpleadoUpdatePayload,
  PaginatedEmpleadoResponse
} from './empleados.models';

@Injectable({ providedIn: 'root' })
export class EmpleadosApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly apiBase: ApiBaseService
  ) {}

  list(page = 1, limit = 100): Observable<PaginatedEmpleadoResponse> {
    return this.http.get<PaginatedEmpleadoResponse>(
      this.apiBase.endpoint(`/empleados?page=${page}&limit=${limit}`)
    );
  }

  create(payload: EmpleadoPayload): Observable<Empleado> {
    return this.http.post<Empleado>(this.apiBase.endpoint('/empleados'), payload);
  }

  update(clave: string, payload: EmpleadoUpdatePayload): Observable<Empleado> {
    return this.http.put<Empleado>(this.apiBase.endpoint(`/empleados/${clave}`), payload);
  }

  remove(clave: string): Observable<void> {
    return this.http.delete<void>(this.apiBase.endpoint(`/empleados/${clave}`));
  }
}
