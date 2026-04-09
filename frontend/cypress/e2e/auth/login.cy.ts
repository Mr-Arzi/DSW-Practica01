describe('Auth login', () => {
  it('permite login exitoso para admin', () => {
    cy.fixture('test-data.json').then((data) => {
      cy.login(data.admin.email, data.admin.password);
      cy.url().should('include', '/empleados');
      cy.contains('Edicion habilitada').should('be.visible');
    });
  });

  it('permite login exitoso para lector en modo solo lectura', () => {
    cy.fixture('test-data.json').then((data) => {
      cy.login(data.lector.email, data.lector.password);
      cy.url().should('include', '/empleados');
      cy.contains('Solo lectura').should('be.visible');
    });
  });

  it('muestra error para credenciales invalidas', () => {
    cy.visit('/login');
    cy.get('#email').type('invalido@example.com');
    cy.get('#password').type('incorrecto');
    cy.contains('button', 'Entrar').click();
    cy.contains(/No fue posible iniciar sesion|Credenciales invalidas/).should('be.visible');
  });
});
