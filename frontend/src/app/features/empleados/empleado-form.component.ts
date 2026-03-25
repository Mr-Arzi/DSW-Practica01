import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Empleado, EmpleadoPayload } from './empleados.models';
import { Departamento } from '../departamentos/departamentos.models';

@Component({
  selector: 'app-empleado-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="save()" class="inline-form">
      <input formControlName="nombre" placeholder="Nombre" />
      <input formControlName="direccion" placeholder="Direccion" />
      <input formControlName="telefono" placeholder="Telefono" />
      <input formControlName="email" placeholder="Correo" />
      <select formControlName="departamentoId">
        <option *ngFor="let departamento of departamentos" [ngValue]="departamento.id">
          {{ departamento.clave }} - {{ departamento.nombre }}
        </option>
      </select>
      <input formControlName="password" type="password" placeholder="Contrasena" />
      <button type="submit">{{ submitLabel }}</button>
      <button type="button" *ngIf="mode === 'edit'" (click)="cancel()">Cancelar</button>
    </form>
  `,
  styles: [
    '.inline-form { display: grid; gap: .6rem; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); }',
    'input, select { background: rgba(20,21,25,.75); border: 1px solid rgba(255,255,255,.14); border-radius: 10px; color: var(--cr-text); outline: none; padding: .56rem .62rem; }',
    'input:focus, select:focus { border-color: rgba(244,117,33,.75); box-shadow: 0 0 0 3px rgba(244,117,33,.2); }',
    'button { background: linear-gradient(120deg,var(--cr-orange),#e45d09); border: 0; border-radius: 999px; color: #fff; cursor: pointer; font-weight: 700; padding: .56rem .62rem; }'
  ]
})
export class EmpleadoFormComponent {
  private readonly fb = inject(FormBuilder);

  @Input() submitLabel = 'Guardar';
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() departamentos: Departamento[] = [];
  @Input() set empleado(value: Empleado | null) {
    if (!value) {
      this.form.reset({
        nombre: '',
        direccion: '',
        telefono: '',
        email: '',
        departamentoId: 1,
        password: ''
      });
      return;
    }

    this.form.patchValue({
      nombre: value.nombre,
      direccion: value.direccion,
      telefono: value.telefono,
      email: value.email,
      departamentoId: value.departamentoId
    });
  }

  @Output() saveEmpleado = new EventEmitter<EmpleadoPayload>();
  @Output() cancelEdit = new EventEmitter<void>();

  readonly form = this.fb.group({
    nombre: ['', Validators.required],
    direccion: ['', Validators.required],
    telefono: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    departamentoId: [1, [Validators.required]],
    password: ['']
  });

  constructor() {}

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saveEmpleado.emit({
      nombre: this.form.controls.nombre.value ?? '',
      direccion: this.form.controls.direccion.value ?? '',
      telefono: this.form.controls.telefono.value ?? '',
      email: this.form.controls.email.value ?? '',
      departamentoId: Number(this.form.controls.departamentoId.value ?? 1),
      password: this.form.controls.password.value ?? ''
    });

    if (this.mode === 'create') {
      this.form.reset({
        nombre: '',
        direccion: '',
        telefono: '',
        email: '',
        departamentoId: 1,
        password: ''
      });
    }
  }

  cancel(): void {
    this.cancelEdit.emit();
  }
}
