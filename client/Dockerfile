FROM node:18 AS builder

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Etapa de producción: servir con Nginx
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html

# Copia config opcional de Nginx si quieres (no es necesario para Vite)
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]