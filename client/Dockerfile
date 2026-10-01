FROM node:18-alpine AS base

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

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
