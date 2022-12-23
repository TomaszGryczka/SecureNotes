FROM node:18-alpine as builder

WORKDIR /app

COPY package.json ./
COPY package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build --omit-dev

FROM nginx:1.22.1-alpine

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/dist/frontend /usr/share/nginx/html
