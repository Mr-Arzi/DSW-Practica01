import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiBaseService } from '../../core/api/api-base.service';
import { LoginRequest, LoginResponse } from './login.models';

@Injectable({ providedIn: 'root' })
export class LoginApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly apiBase: ApiBaseService
  ) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.apiBase.endpoint('/auth/login'), payload);
  }
}
