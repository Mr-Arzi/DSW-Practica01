import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Departamento, DepartamentoPayload } from './departamentos.models';

@Component({
  selector: 'app-departamento-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="save()" class="inline-form">
      <input formControlName="clave" placeholder="Clave" />
      <input formControlName="nombre" placeholder="Nombre" />
      <button type="submit">{{ submitLabel }}</button>
    </form>
  `,
  styles: [
    '.inline-form { display: grid; gap: .6rem; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); }',
    'input { background: rgba(20,21,25,.75); border: 1px solid rgba(255,255,255,.14); border-radius: 10px; color: var(--cr-text); outline: none; padding: .56rem .62rem; }',
    'input:focus { border-color: rgba(244,117,33,.75); box-shadow: 0 0 0 3px rgba(244,117,33,.2); }',
    'button { background: linear-gradient(120deg,var(--cr-orange),#e45d09); border: 0; border-radius: 999px; color: #fff; cursor: pointer; font-weight: 700; padding: .56rem .62rem; }'
  ]
})
export class DepartamentoFormComponent {
  private readonly fb = inject(FormBuilder);

  @Input() submitLabel = 'Guardar';
  @Input() set departamento(value: Departamento | null) {
    if (!value) {
      return;
    }
    this.form.patchValue({ clave: value.clave, nombre: value.nombre });
  }

  @Output() saveDepartamento = new EventEmitter<DepartamentoPayload>();

  readonly form = this.fb.group({
    clave: ['', Validators.required],
    nombre: ['', Validators.required]
  });

  constructor() {}

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saveDepartamento.emit({
      clave: this.form.controls.clave.value ?? '',
      nombre: this.form.controls.nombre.value ?? ''
    });
  }
}
