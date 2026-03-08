# Aerolinea Virtual

Aplicacion web responsiva desarrollada con JHipster 8.11.0 para el curso Fundamentos de Sistemas de Informacion, Universidad de Antioquia.

**Profesor:** Ph.D Diego Jose Luis Botia Valderrama

## Descripcion

Sistema de gestion de una aerolinea virtual que permite administrar pasajeros, reservas, vuelos y asientos. Desarrollado como aplicacion monolitica con React (frontend) y Spring Boot (backend), desplegable en contenedores Docker.

## Modelo de Dominio

| Entidad  | Campos principales                                      |
| -------- | ------------------------------------------------------- |
| Pasajero | nombre, apellido, email, telefono, fechaNacimiento      |
| Reserva  | codigo, fechaReserva, estado                            |
| Vuelo    | numeroVuelo, origen, destino, fechaSalida, fechaLlegada |
| Asiento  | numero, clase, disponible                               |

**Relaciones:**

- Pasajero -> Reserva (OneToMany)
- Reserva -> Vuelo (ManyToOne)
- Reserva -> Asiento (ManyToOne)

## Stack Tecnologico

- **Backend:** Java 17, Spring Boot 3.4.5, Spring Security (JWT), Spring Data JPA, Hibernate
- **Frontend:** React, TypeScript, Bootstrap
- **Base de Datos:** MySQL (remoto en Hostinger)
- **Build:** Maven, Webpack
- **Testing:** JUnit 5, Jest, Testcontainers
- **Despliegue:** Docker (Jib), Docker Compose
- **Versionamiento:** Git + GitHub

## Requisitos

- Java 17 (OpenJDK)
- Maven 3.9+
- Node.js 20+ y npm
- Docker Desktop
- Git

## Ejecucion en Desarrollo

```bash
# Backend (puerto 8080)
./mvnw -Pdev

# Frontend con hot-reload (puerto 9060)
npm start
```

Acceder a http://localhost:8080

Credenciales por defecto:

- Admin: admin / admin
- Usuario: user / user

## Ejecucion de Pruebas

```bash
# Pruebas backend (JUnit 5 + Testcontainers)
./mvnw clean verify

# Pruebas frontend (Jest)
npm test
```

## Build de Produccion

```bash
./mvnw -Pprod package -DskipTests
```

El JAR se genera en target/aerolinea-virtual-0.0.1-SNAPSHOT.jar

## Despliegue con Docker

```bash
# Construir imagen
./mvnw package -Pprod verify jib:dockerBuild -DskipTests

# Levantar contenedor
docker compose -f src/main/docker/app.yml up -d

# Verificar estado
docker container ls
curl http://localhost:8080/management/health
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/udea/aerolinea/
│   │   ├── domain/          # Entidades JPA
│   │   ├── repository/      # Repositorios Spring Data
│   │   ├── service/         # Logica de negocio
│   │   ├── web/rest/        # Controladores REST
│   │   └── config/          # Configuracion Spring
│   ├── resources/config/    # application.yml, Liquibase
│   ├── webapp/app/          # Frontend React
│   └── docker/              # Docker Compose, app.yml
└── test/                    # Pruebas unitarias e integracion
```

## Base de Datos

La aplicacion usa MySQL remoto en Hostinger. La configuracion de conexion esta en:

- src/main/resources/config/application-dev.yml
- src/main/resources/config/application-prod.yml

Las migraciones de esquema se gestionan con Liquibase (src/main/resources/config/liquibase/).

## Repositorio

GitHub: https://github.com/Carlosclab/fsi-lab01.git
