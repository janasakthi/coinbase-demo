FROM node:14 as angular-build
WORKDIR /app
COPY angular/package*.json ./
RUN npm install
COPY angular/. .
RUN npm run build --prod
EXPOSE 4200