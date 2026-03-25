describe('Navegacion entre modulos', () => {
  it('navega entre empleados y departamentos', () => {
    cy.fixture('test-data.json').then((data) => {
      cy.login(data.admin.email, data.admin.password);
      cy.contains('a', 'Departamentos').click();
      cy.url().should('include', '/departamentos');
      cy.contains('a', 'Empleados').click();
      cy.url().should('include', '/empleados');
    });
  });
});
