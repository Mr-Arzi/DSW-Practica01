describe('Departamentos flows', () => {
  it('visualiza listado y permite crear', () => {
    cy.fixture('test-data.json').then((data) => {
      cy.login(data.admin.email, data.admin.password);
      cy.contains('a', 'Departamentos').click();
      cy.contains('h2', 'Departamentos').should('be.visible');
      cy.get('input[formcontrolname="clave"]').type('CYP');
      cy.get('input[formcontrolname="nombre"]').type('Departamento Cypress');
      cy.contains('button', 'Crear departamento').click();
    });
  });
});
