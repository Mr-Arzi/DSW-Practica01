import { Injectable } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { Departamento, DepartamentoPayload } from './departamentos.models';
import { DepartamentosApiService } from './departamentos-api.service';

@Injectable({ providedIn: 'root' })
export class DepartamentosActionsService {
  constructor(private readonly api: DepartamentosApiService) {}

  createAndRefresh(payload: DepartamentoPayload): Observable<Departamento[]> {
    return this.api.create(payload).pipe(switchMap(() => this.api.list()));
  }

  updateAndRefresh(id: number, payload: DepartamentoPayload): Observable<Departamento[]> {
    return this.api.update(id, payload).pipe(switchMap(() => this.api.list()));
  }

  deleteAndRefresh(id: number): Observable<Departamento[]> {
    return this.api.remove(id).pipe(switchMap(() => this.api.list()));
  }
}
