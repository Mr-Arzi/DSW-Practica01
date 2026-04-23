/// <reference types="cypress" />

declare global {
  namespace Cypress {
    interface Chainable {
      login(email: string, password: string): Chainable<void>;
      ensureTestData(): Chainable<void>;
      cleanupTestData(): Chainable<void>;
    }
  }
}

Cypress.Commands.add('login', (email: string, password: string) => {
  cy.visit('/login');
  cy.get('#email').clear().type(email);
  cy.get('#password').clear().type(password);
  cy.contains('button', 'Entrar').click();
});

Cypress.Commands.add('ensureTestData', () => {
  cy.fixture('test-data.json').then((data) => {
    cy.wrap(data).as('testData');

    const adminAuth = `Basic ${btoa('admin:admin123')}`;

    // Ensure login users exist in empleado auth store so /auth/login can validate them.
    const seedUsers = [
      {
        nombre: 'Admin Cypress',
        direccion: 'Calle Admin',
        telefono: '5551001',
        departamentoId: 1,
        email: data.admin.email,
        password: data.admin.password
      },
      {
        nombre: 'Lector Cypress',
        direccion: 'Calle Lector',
        telefono: '5551002',
        departamentoId: 1,
        email: data.lector.email,
        password: data.lector.password
      }
    ];

    seedUsers.forEach((payload) => {
      cy.request({
        method: 'POST',
        url: '/api/v1/empleados',
        headers: {
          Authorization: adminAuth,
          'Content-Type': 'application/json'
        },
        body: payload,
        failOnStatusCode: false
      }).then((response) => {
        expect([201, 409]).to.include(response.status);
      });
    });
  });
});

Cypress.Commands.add('cleanupTestData', () => {
  // Cleanup se realiza via API/UI segun flujo; este hook mantiene la suite idempotente.
  cy.wrap(null);
});

export {};
