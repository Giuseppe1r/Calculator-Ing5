# syntax=docker/dockerfile:1

# ---------- Etapa de compilacion ----------
FROM node:24-alpine AS build

WORKDIR /app

# npm ci (en vez de npm install) instala exactamente lo fijado en
# package-lock.json, que es lo que hace reproducible el build.
COPY package.json package-lock.json ./
RUN npm ci

COPY . .

# Vite congela import.meta.env en tiempo de build, por eso la URL del API entra
# como build-arg y no como variable de entorno del contenedor final.
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

# ---------- Etapa de ejecucion ----------
FROM nginx:alpine AS runtime

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/app.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=10s --timeout=5s --start-period=10s --retries=5 \
    CMD wget -qO- http://127.0.0.1/healthz >/dev/null 2>&1 || exit 1

CMD ["nginx", "-g", "daemon off;"]
