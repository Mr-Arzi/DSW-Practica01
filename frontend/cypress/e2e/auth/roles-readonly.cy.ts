describe('Role permissions', () => {
  it('lector solo puede visualizar empleados y departamentos', () => {
    cy.fixture('test-data.json').then((data) => {
      cy.login(data.lector.email, data.lector.password);

      cy.contains('h2', 'Empleados').should('be.visible');
      cy.contains('Solo lectura').should('be.visible');

      cy.get('app-empleado-form').should('not.exist');
      cy.contains('button', 'Editar').should('not.exist');
      cy.contains('button', 'Eliminar').should('not.exist');

      cy.contains('a', 'Departamentos').click();
      cy.url().should('include', '/departamentos');
      cy.contains('h2', 'Departamentos').should('be.visible');
      cy.contains('Solo lectura').should('be.visible');

      cy.get('app-departamento-form').should('not.exist');
      cy.contains('button', 'Crear departamento').should('not.exist');
      cy.contains('button', 'Eliminar').should('not.exist');
    });
  });
});
