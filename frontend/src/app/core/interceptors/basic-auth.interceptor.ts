import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthSessionService } from '../auth/auth-session.service';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const session = inject(AuthSessionService).getSession();

  if (!session || req.url.includes('/auth/login')) {
    return next(req);
  }

  const authReq = req.clone({
    setHeaders: {
      Authorization: session.basicAuthHeader
    }
  });

  return next(authReq);
};
