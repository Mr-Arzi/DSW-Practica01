import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { EmpleadoFormComponent } from './empleado-form.component';
import { Empleado, EmpleadoPayload, EmpleadoUpdatePayload } from './empleados.models';
import { EmpleadosApiService } from './empleados-api.service';
import { EmpleadosActionsService } from './empleados-actions.service';
import { RolePolicyService } from '../../core/auth/role-policy.service';
import { Departamento } from '../departamentos/departamentos.models';
import { DepartamentosApiService } from '../departamentos/departamentos-api.service';

@Component({
  selector: 'app-empleados-page',
  standalone: true,
  imports: [CommonModule, EmpleadoFormComponent],
  templateUrl: './empleados-page.component.html',
  styleUrl: './empleados-page.component.scss'
})
export class EmpleadosPageComponent implements OnInit {
  empleados: Empleado[] = [];
  departamentos: Departamento[] = [];
  selectedEmpleado: Empleado | null = null;
  errorMessage = '';

  constructor(
    private readonly api: EmpleadosApiService,
    private readonly actions: EmpleadosActionsService,
    private readonly rolePolicy: RolePolicyService,
    private readonly departamentosApi: DepartamentosApiService
  ) {}

  ngOnInit(): void {
    this.load();
    this.loadDepartamentos();
  }

  get canWrite(): boolean {
    return this.rolePolicy.canWrite();
  }

  load(): void {
    this.api.list().subscribe({
      next: (result) => {
        this.errorMessage = '';
        this.empleados = this.sortByNewest(result.data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  loadDepartamentos(): void {
    this.departamentosApi.list().subscribe({
      next: (result) => {
        this.departamentos = result;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  create(payload: EmpleadoPayload): void {
    if (!this.canWrite) {
      return;
    }

    this.api.create(payload).subscribe({
      next: (created) => {
        this.errorMessage = '';
        this.empleados = this.sortByNewest([created, ...this.empleados]);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  remove(clave: string): void {
    if (!this.canWrite) {
      return;
    }

    this.actions.deleteAndRefresh(clave).subscribe({
      next: (result) => {
        this.errorMessage = '';
        this.empleados = this.sortByNewest(result.data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  startEdit(empleado: Empleado): void {
    if (!this.canWrite) {
      return;
    }
    this.selectedEmpleado = { ...empleado };
    this.errorMessage = '';
  }

  cancelEdit(): void {
    this.selectedEmpleado = null;
  }

  save(payload: EmpleadoPayload): void {
    if (!this.selectedEmpleado) {
      this.create(payload);
      return;
    }

    const updatePayload: EmpleadoUpdatePayload = {
      nombre: payload.nombre,
      direccion: payload.direccion,
      telefono: payload.telefono,
      departamentoId: payload.departamentoId
    };

    this.actions.updateAndRefresh(this.selectedEmpleado.clave, updatePayload).subscribe({
      next: (result) => {
        this.errorMessage = '';
        this.selectedEmpleado = null;
        this.empleados = this.sortByNewest(result.data);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.normalizeError(error);
      }
    });
  }

  private normalizeError(error: HttpErrorResponse): string {
    if (error.status >= 500 || error.status === 0) {
      return 'Backend no disponible. Puedes reintentar.';
    }

    if (error.status === 409 && typeof error.error?.message === 'string') {
      return error.error.message;
    }

    if (error.status === 403) {
      return 'No tienes permisos para esta accion.';
    }

    return 'No fue posible completar la operacion.';
  }

  private sortByNewest(data: Empleado[]): Empleado[] {
    return [...data].sort((a, b) => this.extractClaveNumber(b.clave) - this.extractClaveNumber(a.clave));
  }

  private extractClaveNumber(clave: string): number {
    const parts = clave.split('-');
    const value = Number(parts[1]);
    return Number.isFinite(value) ? value : 0;
  }
}
