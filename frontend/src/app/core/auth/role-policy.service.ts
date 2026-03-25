import { Injectable } from '@angular/core';
import { AuthSessionService } from './auth-session.service';

@Injectable({ providedIn: 'root' })
export class RolePolicyService {
  constructor(private readonly session: AuthSessionService) {}

  canWrite(): boolean {
    return this.session.getRole() === 'ADMIN';
  }

  canRead(): boolean {
    return this.session.isAuthenticated();
  }
}
