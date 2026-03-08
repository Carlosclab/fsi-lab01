# Informe de Laboratorio - Aplicacion Web Responsiva con JHipster

**Materia:** Fundamentos de Sistemas de Informacion
**Universidad:** Universidad de Antioquia
**Profesor:** Ph.D Diego Jose Luis Botia Valderrama
**Estudiante:** Carlos Forero
**Fecha:** 8 de Marzo de 2026
**Repositorio:** https://github.com/Carlosclab/fsi-lab01.git

---

## 1. Objetivo General

Comprender el desarrollo de una aplicacion web responsiva con el framework JHipster y como puede generar aplicaciones completas y rapidas generando productividad al desarrollador.

## 2. Objetivos Especificos

- Configurar el entorno de desarrollo con JHipster
- Realizar una aplicacion web completa mostrando la integracion de herramientas para generar aplicaciones modernas
- Disenar el modelo de dominio de la aplicacion por medio del lenguaje JDL
- Comprender el funcionamiento de los principales componentes de JHipster
- Realizar un proceso de despliegue a un ambiente de produccion

## 3. Herramientas Utilizadas

| Herramienta    | Version                     |
| -------------- | --------------------------- |
| Java (OpenJDK) | 17                          |
| Maven          | 3.9.13                      |
| Node.js        | 25.2.1                      |
| npm            | 11.4.1                      |
| JHipster       | 8.11.0                      |
| Spring Boot    | 3.4.5                       |
| React          | (incluido en JHipster)      |
| MySQL          | 9.2.0 (remoto en Hostinger) |
| Docker Desktop | 4.52.0                      |
| Git            | 2.49.0                      |

## 4. Procedimiento

### 4.1 Instalacion del Ambiente (FASE 0)

Se verifico la instalacion de todas las herramientas requeridas: Java 17, Maven, Node.js, npm, JHipster, Docker Desktop y Git. Se confirmo la conectividad con la base de datos MySQL remota en Hostinger.

**Pantallazo requerido:** Terminal mostrando las versiones de las herramientas instaladas.

### 4.2 Creacion del Proyecto JHipster (FASE 1)

Se genero el proyecto con la siguiente configuracion:

- Tipo de aplicacion: Monolith
- Framework frontend: React
- Build tool: Maven
- Autenticacion: JWT
- Base de datos: MySQL (dev y prod)
- Idiomas: Espanol (nativo) + Ingles
- Cache: Ehcache
- Nombre base: aerolineaVirtual
- Paquete: com.udea.aerolinea

**Pantallazo requerido:** Contenido del archivo .yo-rc.json con la configuracion del proyecto.

### 4.3 Configuracion de la Base de Datos (FASE 2)

Se editaron los archivos `application-dev.yml` y `application-prod.yml` para apuntar a la instancia MySQL remota en Hostinger (31.97.208.109:3306). Se configuro el dialecto MySQL en `application.yml`.

**Pantallazo requerido:** Seccion datasource del archivo application-dev.yml.

### 4.4 Modelo de Dominio con JDL (FASE 3)

Se creo el archivo `aerolinea.jdl` con las 4 entidades del sistema de aerolinea virtual:

```
entity Pasajero {
    nombre String required
    apellido String required
    email String required unique
    telefono String
    fechaNacimiento LocalDate
}

entity Reserva {
    codigo String required unique
    fechaReserva Instant required
    estado String required
}

entity Vuelo {
    numeroVuelo String required unique
    origen String required
    destino String required
    fechaSalida Instant required
    fechaLlegada Instant required
}

entity Asiento {
    numero String required
    clase String required
    disponible Boolean required
}

relationship OneToMany {
    Pasajero to Reserva
}

relationship ManyToOne {
    Reserva to Vuelo
    Reserva to Asiento
}

paginate Pasajero, Reserva, Vuelo, Asiento with pagination
service Pasajero, Reserva, Vuelo, Asiento with serviceClass
```

Se ejecuto `jhipster import-jdl aerolinea.jdl --force` para generar entidades, repositorios, servicios, controladores REST, interfaces React y migraciones Liquibase.

**Pantallazo requerido:** Diagrama de entidades en JDL Studio o salida del import-jdl.

### 4.5 Build y Prueba Local (FASE 4)

Se ejecuto `mvn -Pdev -DskipTests` para compilar el proyecto. La aplicacion inicio exitosamente en http://localhost:8080 con el perfil de desarrollo.

**Pantallazo requerido:** Pagina principal de la aplicacion en el navegador mostrando la pantalla de login.

### 4.6 Versionamiento con Git (FASE 5)

Se inicializo el repositorio Git, se realizo el commit inicial y se subio al repositorio remoto en GitHub: https://github.com/Carlosclab/fsi-lab01.git

**Pantallazo requerido:** Salida del comando git push mostrando la subida exitosa.

### 4.7 Ejecucion de Pruebas (FASE 6)

#### Pruebas Backend

Se ejecuto `mvn clean verify` obteniendo:

- **180 tests ejecutados**
- **0 fallos**
- **0 errores**

Se incluyen pruebas unitarias (JUnit 5), pruebas de integracion (Spring Test + Testcontainers), y pruebas de arquitectura (ArchUnit).

**Nota tecnica:** Se resolvio un problema de compatibilidad entre Testcontainers 1.20.6 y Docker Desktop 4.52.0 (API version minima 1.44 vs docker-java 3.4.1 default 1.43). Se agrego `-Dapi.version=1.44` al argLine de Maven.

#### Pruebas Frontend

Se ejecuto `npm test` obteniendo:

- **202 tests ejecutados**
- **91.97% de cobertura**
- **0 fallos**

**Pantallazo requerido:** Salida de la terminal mostrando los resultados de pruebas backend y frontend.

### 4.8 Build de Produccion (FASE 7)

Se ejecuto `mvn -Pprod package -DskipTests` generando el archivo JAR ejecutable:
`target/aerolinea-virtual-0.0.1-SNAPSHOT.jar`

**Pantallazo requerido:** Salida BUILD SUCCESS del build de produccion.

### 4.9 Despliegue con Docker (FASE 8)

1. Se edito `src/main/docker/app.yml` para que el contenedor apunte directamente a la BD de Hostinger (sin contenedor MySQL local)
2. Se construyo la imagen Docker con Jib: `mvn package -Pprod verify jib:dockerBuild -DskipTests`
3. Se levanto el contenedor: `docker compose -f src/main/docker/app.yml up -d`
4. Se verifico el estado: contenedor healthy, health endpoint respondiendo `{"status":"UP"}`

**Pantallazo requerido:**

- Salida de `docker container ls` mostrando el contenedor corriendo
- App accesible en http://localhost:8080 desde el contenedor Docker

### 4.10 Internacionalizacion (i18n)

Se configuraron las traducciones al ingles para las 4 entidades del dominio en `src/main/webapp/i18n/en/`. Al cambiar el idioma a English en el frontend, todos los labels de entidades se muestran correctamente traducidos (Passenger, Reservation, Flight, Seat).

**Pantallazo requerido:** App en idioma ingles mostrando las entidades traducidas.

## 5. Analisis de Cumplimiento

| Requisito del PDF                                 | Estado      | Observacion                                 |
| ------------------------------------------------- | ----------- | ------------------------------------------- |
| Aplicacion monolith con JHipster                  | Cumplido    | JHipster 8.11.0                             |
| Framework React                                   | Cumplido    | React con TypeScript                        |
| Build tool Maven                                  | Cumplido    | Maven 3.9.13                                |
| Autenticacion JWT                                 | Cumplido    | Spring Security + JWT                       |
| Base de datos MySQL                               | Cumplido    | MySQL remoto en Hostinger                   |
| Entidades JDL (Pasajero, Reserva, Vuelo, Asiento) | Cumplido    | 4 entidades con relaciones                  |
| Relaciones del modelo                             | Cumplido    | OneToMany, ManyToOne                        |
| Paginacion                                        | Cumplido    | Pagination para todas las entidades         |
| Service classes                                   | Cumplido    | serviceClass para todas                     |
| Build desarrollo                                  | Cumplido    | mvn -Pdev                                   |
| Build produccion                                  | Cumplido    | mvn -Pprod package                          |
| Pruebas backend                                   | Cumplido    | 180 tests, 0 fallos                         |
| Pruebas frontend                                  | Cumplido    | 202 tests, 91.97% cobertura                 |
| Despliegue Docker                                 | Cumplido    | Imagen Jib + Docker Compose                 |
| Repositorio Git/GitHub                            | Cumplido    | https://github.com/Carlosclab/fsi-lab01.git |
| Internacionalizacion                              | Cumplido    | Espanol + Ingles                            |
| Pruebas Gatling                                   | No incluido | Opcional segun el PDF                       |
| TLS/HTTP2                                         | No incluido | Opcional segun el PDF                       |

## 6. Guia para Toma de Pantallazos

### Automatizacion posible (macOS)

En macOS se puede usar `screencapture` desde terminal para capturas automatizadas:

```bash
mkdir -p screenshots

# Captura de pantalla completa
screencapture screenshots/01_pantalla_completa.png

# Captura de ventana especifica (interactivo, click en la ventana)
screencapture -w screenshots/02_ventana.png

# Captura con temporizador (5 segundos para posicionar)
screencapture -T 5 screenshots/03_temporizador.png
```

Sin embargo, las capturas de la app web requieren que la app este corriendo y el navegador abierto, lo cual no se puede automatizar completamente desde CLI sin herramientas adicionales como Puppeteer o Playwright.

### Paso a Paso Manual para Pantallazos

**Preparacion:** Asegurarse de que la app este corriendo (`mvn -Pdev` o Docker).

#### Pantallazo 1: Versiones de herramientas

```bash
java -version && mvn -version && node -v && npm -v && jhipster --version && docker --version && git --version
```

Capturar la salida con Cmd+Shift+4 (seleccionar area) o Cmd+Shift+3 (pantalla completa).

#### Pantallazo 2: Pagina principal / Login

1. Abrir http://localhost:8080 en el navegador
2. Capturar con Cmd+Shift+4

#### Pantallazo 3: Dashboard admin

1. Iniciar sesion como admin/admin
2. Capturar la pagina principal despues del login

#### Pantallazo 4: Vistas de entidades (CRUD)

1. Ir a Entities > Passenger (o Pasajero en espanol)
2. Crear un registro de prueba
3. Capturar la lista y el formulario de creacion

#### Pantallazo 5: Administration > Health

1. Ir a Administration > Health
2. Capturar mostrando el estado UP de los componentes

#### Pantallazo 6: API Docs (Swagger)

1. Ir a Administration > API
2. Capturar la pagina de Swagger/OpenAPI

#### Pantallazo 7: Resultados de pruebas

```bash
# Backend
mvn clean verify 2>&1 | tail -20
# Frontend
npm test -- --watchAll=false 2>&1 | tail -30
```

Capturar la salida de la terminal.

#### Pantallazo 8: Build produccion

```bash
mvn -Pprod package -DskipTests 2>&1 | tail -10
```

Capturar mostrando BUILD SUCCESS.

#### Pantallazo 9: Docker container

```bash
docker container ls
```

Capturar mostrando el contenedor healthy.

#### Pantallazo 10: App en Docker

1. Con el contenedor corriendo, abrir http://localhost:8080
2. Capturar la app funcionando desde Docker

#### Pantallazo 11: Internacionalizacion

1. Cambiar idioma a English en el menu del frontend
2. Ir a Entities > mostrar entidades con labels en ingles
3. Capturar

#### Pantallazo 12: GitHub

1. Abrir https://github.com/Carlosclab/fsi-lab01.git en el navegador
2. Capturar mostrando los commits

## 7. Conclusiones

- JHipster permite generar aplicaciones web completas de forma rapida, integrando backend (Spring Boot) y frontend (React) con configuracion minima.
- El uso de JDL facilita la definicion del modelo de dominio y genera automaticamente todas las capas (entidad, repositorio, servicio, controlador, UI).
- Las pruebas automatizadas (JUnit 5, Jest, Testcontainers) garantizan la calidad del codigo generado.
- El despliegue con Docker mediante Jib simplifica la creacion de imagenes sin necesidad de Dockerfile manual.
- La internacionalizacion integrada permite soportar multiples idiomas sin modificar el codigo fuente.
- Se resolvio exitosamente un problema de compatibilidad entre Testcontainers y Docker Desktop (API version mismatch), documentando la solucion para referencia futura.
