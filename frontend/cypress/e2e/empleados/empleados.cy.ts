describe('Empleados flows', () => {
  it('admin puede crear, editar y eliminar empleado', () => {
    cy.fixture('test-data.json').then((data) => {
      const suffix = Date.now();
      const createdName = `Empleado Cypress ${suffix}`;
      const updatedName = `Empleado Cypress Editado ${suffix}`;
      const email = `empleado.cypress.${suffix}@example.com`;

      cy.login(data.admin.email, data.admin.password);
      cy.contains('h2', 'Empleados').should('be.visible');

      cy.get('input[formcontrolname="nombre"]').clear().type(createdName);
      cy.get('input[formcontrolname="direccion"]').type('Calle Cypress');
      cy.get('input[formcontrolname="telefono"]').type('5551234');
      cy.get('input[formcontrolname="email"]').type(email);
      cy.get('select[formcontrolname="departamentoId"]').select(0);
      cy.contains('button', 'Crear empleado').click();

      cy.contains('td', createdName).should('be.visible');

      cy.contains('tr', createdName).within(() => {
        cy.contains('button', 'Editar').click();
      });

      cy.get('input[formcontrolname="nombre"]').clear().type(updatedName);
      cy.contains('button', 'Guardar cambios').click();
      cy.contains('td', updatedName).should('be.visible');

      cy.contains('tr', updatedName).within(() => {
        cy.contains('button', 'Eliminar').click();
      });
      cy.contains('td', updatedName).should('not.exist');
    });
  });
});
