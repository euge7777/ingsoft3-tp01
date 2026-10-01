FROM node:24-alpine AS base

WORKDIR /app
COPY package.json package-lock.json ./

RUN npm ci

COPY . .

FROM base AS test
RUN npm run test:coverage

FROM scratch AS test-report
COPY --from=test /app/coverage /coverage

FROM base AS build
RUN npm run build

FROM nginx:alpine AS runtime

COPY --from=build /app/dist /usr/share/nginx/html
COPY default.conf.template /etc/nginx/templates/default.conf.template

ENV BACKEND_URL=http://backend:8080 \
    DNS_RESOLVER=127.0.0.11

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
