import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthSessionService } from '../../core/auth/auth-session.service';
import { LoginApiService } from './login-api.service';
import { LoginRequest, LoginResponse } from './login.models';

@Injectable({ providedIn: 'root' })
export class LoginFacade {
  constructor(
    private readonly loginApi: LoginApiService,
    private readonly session: AuthSessionService,
    private readonly router: Router
  ) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.loginApi.login(payload).pipe(
      tap(() => {
        this.session.start(payload.email, payload.password);
        void this.router.navigate(['/empleados']);
      })
    );
  }
}
