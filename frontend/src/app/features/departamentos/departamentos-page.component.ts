import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RolePolicyService } from '../../core/auth/role-policy.service';
import { DepartamentoFormComponent } from './departamento-form.component';
import { DepartamentoPayload } from './departamentos.models';
import { DepartamentosApiService } from './departamentos-api.service';
import { DepartamentosActionsService } from './departamentos-actions.service';

@Component({
  selector: 'app-departamentos-page',
  standalone: true,
  imports: [CommonModule, DepartamentoFormComponent],
  templateUrl: './departamentos-page.component.html',
  styleUrl: './departamentos-page.component.scss'
})
export class DepartamentosPageComponent implements OnInit {
  departamentos = [] as Array<{ id: number; clave: string; nombre: string }>;
  errorMessage = '';

  constructor(
    private readonly api: DepartamentosApiService,
    private readonly actions: DepartamentosActionsService,
    private readonly rolePolicy: RolePolicyService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  get canWrite(): boolean {
    return this.rolePolicy.canWrite();
  }

  load(): void {
    this.api.list().subscribe({
      next: (result) => {
        this.errorMessage = '';
        this.departamentos = result;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  create(payload: DepartamentoPayload): void {
    if (!this.canWrite) {
      return;
    }

    this.actions.createAndRefresh(payload).subscribe({
      next: (result) => {
        this.errorMessage = '';
        this.departamentos = result;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  remove(id: number): void {
    if (!this.canWrite) {
      return;
    }

    this.actions.deleteAndRefresh(id).subscribe({
      next: (result) => {
        this.errorMessage = '';
        this.departamentos = result;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  private normalizeError(error: HttpErrorResponse): string {
    if (error.status === 409) {
      return 'No se puede eliminar por conflicto de integridad.';
    }

    if (error.status >= 500 || error.status === 0) {
      return 'Backend no disponible. Puedes reintentar.';
    }

    return 'No fue posible completar la operacion.';
  }
}
