import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiBaseService } from '../../core/api/api-base.service';
import { Departamento, DepartamentoPayload } from './departamentos.models';

@Injectable({ providedIn: 'root' })
export class DepartamentosApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly apiBase: ApiBaseService
  ) {}

  list(): Observable<Departamento[]> {
    return this.http.get<Departamento[]>(this.apiBase.endpoint('/departamentos'));
  }

  create(payload: DepartamentoPayload): Observable<Departamento> {
    return this.http.post<Departamento>(this.apiBase.endpoint('/departamentos'), payload);
  }

  update(id: number, payload: DepartamentoPayload): Observable<Departamento> {
    return this.http.put<Departamento>(this.apiBase.endpoint(`/departamentos/${id}`), payload);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(this.apiBase.endpoint(`/departamentos/${id}`));
  }
}
