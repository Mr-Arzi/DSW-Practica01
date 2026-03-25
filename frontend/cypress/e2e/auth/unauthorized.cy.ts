describe('Unauthorized behavior', () => {
  it('redirige a login al visitar ruta protegida sin sesion', () => {
    cy.clearAllSessionStorage();
    cy.visit('/empleados');
    cy.url().should('include', '/login');
  });
});
