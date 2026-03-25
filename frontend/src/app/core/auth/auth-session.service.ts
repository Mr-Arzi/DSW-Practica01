import { Injectable } from '@angular/core';

export type UserRole = 'ADMIN' | 'LECTOR';

export interface SessionState {
  email: string;
  basicAuthHeader: string;
  role: UserRole;
}

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private readonly storageKey = 'frontend.auth.session';

  isAuthenticated(): boolean {
    return this.getSession() !== null;
  }

  getSession(): SessionState | null {
    const raw = sessionStorage.getItem(this.storageKey);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as SessionState;
    } catch {
      this.clear();
      return null;
    }
  }

  getRole(): UserRole | null {
    return this.getSession()?.role ?? null;
  }

  start(email: string, password: string): void {
    const normalizedEmail = email.toLowerCase().trim();
    const basicAuthHeader = `Basic ${btoa(`${email}:${password}`)}`;
    const role = normalizedEmail === 'admin' || normalizedEmail === 'admin@example.com'
      ? 'ADMIN'
      : 'LECTOR';
    const state: SessionState = { email: normalizedEmail, basicAuthHeader, role };
    sessionStorage.setItem(this.storageKey, JSON.stringify(state));
  }

  clear(): void {
    sessionStorage.removeItem(this.storageKey);
  }
}
