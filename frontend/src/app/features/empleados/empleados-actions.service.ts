import { Injectable } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { EmpleadosApiService } from './empleados-api.service';
import { EmpleadoPayload, EmpleadoUpdatePayload, PaginatedEmpleadoResponse } from './empleados.models';

@Injectable({ providedIn: 'root' })
export class EmpleadosActionsService {
  constructor(private readonly api: EmpleadosApiService) {}

  createAndRefresh(payload: EmpleadoPayload): Observable<PaginatedEmpleadoResponse> {
    return this.api.create(payload).pipe(switchMap(() => this.api.list()));
  }

  updateAndRefresh(clave: string, payload: EmpleadoUpdatePayload): Observable<PaginatedEmpleadoResponse> {
    return this.api.update(clave, payload).pipe(switchMap(() => this.api.list()));
  }

  deleteAndRefresh(clave: string): Observable<PaginatedEmpleadoResponse> {
    return this.api.remove(clave).pipe(switchMap(() => this.api.list()));
  }
}
