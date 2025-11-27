# OnlyFlans Bakery version
---

```markdown
# OnlyFlans Back-end

API back-end para OnlyFlans (servicio de administración de productos y ventas de la panadería). Proyecto en Java con Spring Boot, persiste datos en SQL y está preparado para ejecutarse con Docker.

## Tecnologías
- Java 17+
- Spring Boot
- Maven (`pom.xml`)
- Base de datos SQL (MySQL configurables)
- Docker / Docker Compose

## Requisitos
- JDK 17+
- Maven 3.6+
- Docker y Docker Compose (si se usa dockerizado)
- Archivo de configuración de entorno: `./.env`

## Estructura básica
- Código fuente: `src/main/java`
- Tests: `src/test/java`
- Configuración de Maven: `pom.xml`
- Dockerfile: `Dockerfile`
- Orquestación: `docker-compose.yml` (opcional)

## Variables de entorno (ejemplo `.env`)
```env
URL_DATABASE=jdbc:mysql://<URL:PUERTO>/<NOMBRE_BASE_DATOS>?useSSL=false&serverTimezone=UTC
DRIVER_DATABASE=com.mysql.cj.jdbc.Driver
USER_DATABASE=<USUARIO_BASE_DATOS>
PASSWORD_DATABASE=<CONTRASEÑA_BASE_DATOS>
DIALECT_DATABASE=org.hibernate.dialect.MySQL8Dialect

AWS_S3_BUCKET_NAME=<NOMBRE_BUCKET_S3
AWS_REGION=<REGION_AWS>

JWT_SECRET=<CLAVE_SECRETA_JWT>
JWT_ACCESS_EXPIRATION_MS=<TIEMPO_EXPIRACION_ACCESS_MS>
JWT_REFRESH_EXPIRATION_MS=<TIEMPO_EXPIRACION_REFRESH_MS>
```

Ajustar las variables según la base de datos y entorno que uses.

## Ejecución local (sin Docker)
1. Configurar `.env` o `application.yml` con credenciales de base de datos.
2. Ejecutar tests:
```bash
mvn test
```
3. Empaquetar y ejecutar:
```bash
mvn clean package -DskipTests
java -jar target/*.jar
```
La API quedará disponible en `http://localhost:8080` (puerto configurable).

## Ejecución con Docker
Construir imagen:
```bash
docker build -t onlyflans-backend:latest .
```
Ejecutar contenedor (ejemplo):
```bash
docker run --rm -p 8080:8080 --env-file .env onlyflans-backend:latest
```

## Ejecución con Docker Compose (ejemplo)

```bash
docker-compose up --build
```

## Build y CI
- Comandos principales:
    - `mvn clean package`
    - `mvn test`
- Integrar pipeline que ejecute tests y construya la imagen Docker para despliegue.

## Endpoints
Documentación de endpoints disponible una vez el proyecto esté ejecutándose en Swagger/OpenAPI `http://localhost:8080/swagger-ui.html`

## Contribución
- Abrir issues para bugs o mejoras.
- Crear ramas feature a partir de `main` con prefijo `feature/`.
- Enviar PR con descripción y pasos para reproducir.

```

