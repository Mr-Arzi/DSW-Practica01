import './commands';

beforeEach(() => {
  cy.ensureTestData();
});

afterEach(() => {
  cy.cleanupTestData();
});
