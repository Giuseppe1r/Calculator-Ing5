# syntax=docker/dockerfile:1

# ---------- Etapa de compilacion ----------
# Antes la imagen hacia COPY target/*.jar, lo que obligaba a ejecutar "mvn package"
# a mano antes de cada build y fallaba en un clon limpio (target/ esta en .gitignore).
# Con el build multi-etapa la imagen se reconstruye de forma reproducible.
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B clean package -DskipTests

# ---------- Etapa de ejecucion ----------
# JRE en vez de JDK: la imagen final no necesita compilador.
FROM eclipse-temurin:21-jre AS runtime

RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/*

# Usuario sin privilegios: no conviene ejecutar el servicio como root.
RUN useradd --system --create-home --uid 10001 spring

WORKDIR /app
COPY --from=build --chown=spring:spring /build/target/*.jar app.jar

USER spring

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=5s --start-period=90s --retries=12 \
    CMD curl -fsS http://localhost:8080/actuator/health || exit 1

# MaxRAMPercentage respeta el limite de memoria del contenedor.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
