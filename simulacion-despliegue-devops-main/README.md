# simulacion-despliegue-devops

Calculadora de tres capas usada como simulación de despliegue DevOps:

| Capa     | Tecnología                | Contenedor            | Puerto host |
|----------|---------------------------|-----------------------|-------------|
| Frontend | React 19 + Vite, servido por nginx | `calculator-frontend` | `3000` |
| Backend  | Spring Boot 3.2 (Java 21) | `calculator-backend`  | `8080` |
| Base de datos | MySQL 8.0            | `calculator-db`       | `127.0.0.1:3306` |

## Arranque

```bash
docker compose up -d --build
```

La aplicación queda en <http://localhost:3000>.

`--wait` es útil en CI para bloquear hasta que los tres *healthchecks* estén en verde:

```bash
docker compose up -d --build --wait
```

Para parar el entorno conservando los datos:

```bash
docker compose down
```

Para parar el entorno **y borrar la base de datos** (necesario si cambias `db/init.sql`,
ya que MySQL solo ejecuta los scripts de `docker-entrypoint-initdb.d` cuando el
volumen está vacío):

```bash
docker compose down -v
```

## Arquitectura de red

El navegador nunca llama al puerto 8080 directamente: nginx publica el frontend y
hace de *proxy* inverso de `/api/` hacia `http://backend:8080/`. Esto mantiene todo
en el mismo origen (sin CORS) y evita hornear la IP del backend en el bundle.

```
navegador ──▶ :3000 nginx ──┬──▶ /            estáticos de React
                            └──▶ /api/*  ──▶  backend:8080/*  ──▶  db:3306
```

## Endpoints

| Método | Ruta                | Cuerpo                | Respuesta |
|--------|---------------------|-----------------------|-----------|
| `POST` | `/sumar`            | `{"a": 5, "b": 3}`    | `OperacionResponseDto` |
| `POST` | `/restar`           | `{"a": 5, "b": 3}`    | `OperacionResponseDto` |
| `POST` | `/multiplicar`      | `{"a": 5, "b": 3}`    | `OperacionResponseDto` |
| `POST` | `/dividir`          | `{"a": 5, "b": 3}`    | `OperacionResponseDto`, o `400` si `b = 0` |
| `GET`  | `/historial`        | —                     | Las 5 últimas operaciones |
| `GET`  | `/actuator/health`  | —                     | Estado, usado por el `HEALTHCHECK` |

Desde el host se pueden probar a través del proxy:

```bash
curl -X POST http://localhost:3000/api/sumar -H "Content-Type: application/json" -d '{"a":5,"b":3}'
```

## Configuración

El backend lee estas variables de entorno (los valores por defecto apuntan al
`docker-compose.yml`):

| Variable | Por defecto | Descripción |
|----------|-------------|-------------|
| `DB_HOST` / `DB_PORT` | `db` / `3306` | Ubicación de MySQL |
| `DB_NAME` / `DB_USER` / `DB_PASSWORD` | `calculator` | Credenciales |
| `CORS_ALLOWED_ORIGINS` | `*` | Solo relevante si se llama al `:8080` sin pasar por nginx |

El frontend resuelve la URL del API en **tiempo de compilación** (Vite congela
`import.meta.env`), por eso se pasa como `build-arg`:

```bash
docker compose build --build-arg VITE_API_BASE_URL=/api frontend
```

## Esquema de base de datos

`db/init.sql` es la fuente de verdad del esquema. Hibernate arranca con
`ddl-auto=validate`, de modo que cualquier desincronización entre la tabla y la
entidad `com.example.entity.Operacion` aborta el arranque del backend en lugar de
provocar un error 500 en cada operación.

## Desarrollo sin Docker

```bash
# backend
cd calculator && mvn spring-boot:run

# frontend (usa .env.local -> http://localhost:8080)
cd frontend/ing5-t1 && npm ci && npm run dev
```
