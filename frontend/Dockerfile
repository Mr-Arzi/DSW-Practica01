FROM node:20-alpine
WORKDIR /app

# Avoid downloading Cypress binary in frontend runtime image builds.
ENV CYPRESS_INSTALL_BINARY=0

COPY package*.json ./
RUN npm ci

COPY . .

EXPOSE 4200
CMD ["npm", "run", "start:docker"]
