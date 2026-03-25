describe('Empleados flows', () => {
  it('visualiza listado y permite crear', () => {
    cy.fixture('test-data.json').then((data) => {
      cy.login(data.admin.email, data.admin.password);
      cy.contains('h2', 'Empleados').should('be.visible');
      cy.get('input[formcontrolname="nombre"]').type('Empleado Cypress');
      cy.get('input[formcontrolname="direccion"]').type('Calle Cypress');
      cy.get('input[formcontrolname="telefono"]').type('5551234');
      cy.get('input[formcontrolname="email"]').type('empleado.cypress@example.com');
      cy.get('input[formcontrolname="departamentoId"]').clear().type('1');
      cy.contains('button', 'Crear empleado').click();
    });
  });
});
