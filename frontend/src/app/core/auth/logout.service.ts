import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthSessionService } from './auth-session.service';

@Injectable({ providedIn: 'root' })
export class LogoutService {
  constructor(
    private readonly session: AuthSessionService,
    private readonly router: Router
  ) {}

  async logout(): Promise<void> {
    this.session.clear();
    await this.router.navigate(['/login']);
  }
}
