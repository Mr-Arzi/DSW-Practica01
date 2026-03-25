import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { AppShellComponent } from './shared/layout/app-shell.component';
import { LoginPageComponent } from './features/login/login-page.component';
import { EmpleadosPageComponent } from './features/empleados/empleados-page.component';
import { DepartamentosPageComponent } from './features/departamentos/departamentos-page.component';

export const routes: Routes = [
	{ path: 'login', component: LoginPageComponent },
	{
		path: '',
		component: AppShellComponent,
		canActivate: [authGuard],
		children: [
			{ path: 'empleados', component: EmpleadosPageComponent },
			{ path: 'departamentos', component: DepartamentosPageComponent },
			{ path: '', pathMatch: 'full', redirectTo: 'empleados' }
		]
	},
	{ path: '**', redirectTo: '' }
];
