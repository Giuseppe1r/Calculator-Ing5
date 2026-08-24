FROM mysql:8.0

# Las credenciales reales las inyecta docker-compose. No se fijan aqui para no
# hornear secretos en la imagen.
COPY init.sql /docker-entrypoint-initdb.d/01-init.sql

EXPOSE 3306
