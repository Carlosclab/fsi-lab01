# 🤖 Instrucciones para Claude Code — Lab JHipster FSI 2026
## Aerolínea Virtual · Universidad de Antioquia
> Base de datos: MySQL remoto en Hostinger (sin contenedor MySQL local)

---

## FASE 0 — Leer configuración y verificar entorno

```bash
source lab_config.env
echo "Proyecto : $PROJECT_NAME"
echo "Directorio: $PROJECT_DIR"
echo "DB remota : $DB_USER@$DB_HOST:$DB_PORT/$DB_NAME"
echo "GitHub    : $GITHUB_REPO_URL"
```

Verificar herramientas:
```bash
java -version        # debe ser 17.x
mvn -version
node -v              # debe ser 20.x
npm -v
jhipster --version   # debe ser 8.x
docker --version
docker ps
git --version
```

Verificar conectividad con la base de datos remota:
```bash
# Si mysql-client está disponible:
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" \
  -e "SELECT 'Conexión OK' AS status;" "$DB_NAME"
```

Si la conexión falla, detener aquí y notificar al usuario que debe habilitar
Remote MySQL en hPanel de Hostinger para su IP pública (`curl ifconfig.me`).

---

## FASE 1 — Crear proyecto JHipster

```bash
mkdir -p "$PROJECT_DIR"
cd "$PROJECT_DIR"
```

Crear `.yo-rc.json` con la configuración del lab:
```bash
cat > .yo-rc.json << EOF
{
  "generator-jhipster": {
    "applicationType": "monolith",
    "authenticationType": "jwt",
    "baseName": "$PROJECT_NAME",
    "buildTool": "maven",
    "cacheProvider": "ehcache",
    "clientFramework": "react",
    "clientTestFrameworks": [],
    "databaseType": "sql",
    "devDatabaseType": "mysql",
    "enableHibernateCache": true,
    "enableTranslation": true,
    "languages": ["es", "en"],
    "nativeLanguage": "es",
    "packageName": "$JAVA_PACKAGE",
    "prodDatabaseType": "mysql",
    "reactive": false,
    "searchEngine": false,
    "websocket": false,
    "withAdminUi": true
  }
}
EOF

jhipster --skip-git --force
```

---

## FASE 2 — Configurar conexión a Hostinger

### application-dev.yml
Ruta: `src/main/resources/config/application-dev.yml`

Localizar la sección `datasource` y reemplazar con:
```yaml
datasource:
  type: com.zaxxer.hikari.HikariDataSource
  url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true
  username: ${DB_USER}
  password: ${DB_PASSWORD}
```

Sustituir las variables con los valores reales leídos de `lab_config.env`:
```bash
sed -i '' \
  "s|url:.*jdbc:mysql://.*|url: jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME?useUnicode=true\&characterEncoding=utf8\&useSSL=false\&allowPublicKeyRetrieval=true\&useLegacyDatetimeCode=false\&serverTimezone=UTC\&createDatabaseIfNotExist=true|" \
  src/main/resources/config/application-dev.yml

sed -i '' "s|username:.*|username: $DB_USER|" \
  src/main/resources/config/application-dev.yml

sed -i '' "s|password:.*|password: $DB_PASSWORD|" \
  src/main/resources/config/application-dev.yml
```

### application-prod.yml
Aplicar los mismos cambios de datasource en el archivo de producción:
```bash
cp src/main/resources/config/application-dev.yml /tmp/dev_datasource_backup.yml

# Replicar datasource en prod:
sed -i '' \
  "s|url:.*jdbc:mysql://.*|url: jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME?useUnicode=true\&characterEncoding=utf8\&useSSL=false\&allowPublicKeyRetrieval=true\&useLegacyDatetimeCode=false\&serverTimezone=UTC\&createDatabaseIfNotExist=true|" \
  src/main/resources/config/application-prod.yml

sed -i '' "s|username:.*|username: $DB_USER|" \
  src/main/resources/config/application-prod.yml

sed -i '' "s|password:.*|password: $DB_PASSWORD|" \
  src/main/resources/config/application-prod.yml
```

### application.yml — Dialecto MySQL
Agregar bajo `jpa.properties`:
```bash
# Verificar si ya existe el dialecto, si no, insertarlo:
grep -q "hibernate.dialect" src/main/resources/config/application.yml || \
  sed -i '' '/hibernate.query.in_clause_parameter_padding/a\
        hibernate.dialect: org.hibernate.dialect.MySQLDialect' \
  src/main/resources/config/application.yml
```

---

## FASE 3 — Importar modelo de dominio JDL

Crear `aerolinea.jdl` en la raíz:
```bash
cat > "$PROJECT_DIR/aerolinea.jdl" << 'JDLEOF'
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
JDLEOF
```

Importar (ejecutar 2 veces, responder `a` cuando pregunte por `master.xml`):
```bash
cd "$PROJECT_DIR"
jhipster import-jdl aerolinea.jdl --force
jhipster import-jdl aerolinea.jdl --force
```

---

## FASE 4 — Build y prueba local (dev contra Hostinger)

```bash
cd "$PROJECT_DIR"
./mvnw -Pdev -DskipTests
```

Si falla:
```bash
npm install --save-dev ajv@^7
npm run webapp:build:dev
./mvnw -DskipTests
```

En terminal separada iniciar el frontend Webpack:
```bash
npm start
```

Verificar: `http://localhost:8080` (backend) y `http://localhost:9000` (webpack).
Login: `admin/admin` y `user/user`.

Liquibase aplicará los changesets directamente sobre la BD de Hostinger.

---

## FASE 5 — Git commit inicial

```bash
cd "$PROJECT_DIR"
git init
git add .
git commit -m "feat: proyecto JHipster aerolínea virtual - setup inicial"
git branch -M main
git remote add origin "$GITHUB_REPO_URL"
git push -u origin main
```

---

## FASE 6 — Pruebas

Eliminar directorio timezone:
```bash
JAVA_PATH=$(echo $JAVA_PACKAGE | tr '.' '/')
rm -rf "src/test/java/$JAVA_PATH/config/timezone"
```

Backend:
```bash
./mvnw clean verify
# Esperado: BUILD SUCCESS, 0 failures
```

Frontend:
```bash
npm test -- --watchAll=false
```

---

## FASE 7 — Build de producción

```bash
./mvnw -Pprod package -DskipTests
# Genera .war en target/ — verificar Profile(s): [prod] en el log
```

---

## FASE 8 — Deploy con Docker (app container → Hostinger DB)

> No se levanta contenedor MySQL. El contenedor de la app apunta directamente
> a la instancia MySQL de Hostinger.

### 8.1 Generar docker-compose con JHipster

```bash
cd "$PROJECT_DIR"
jhipster docker-compose
# Seleccionar: Monolithic → aerolineaVirtual → no monitoring
```

### 8.2 Editar `src/main/docker/app.yml`

Reemplazar las variables de entorno de datasource para apuntar a Hostinger:
```bash
# La URL debe referenciar el host externo, no un servicio interno de compose:
sed -i '' \
  "s|SPRING_DATASOURCE_URL=.*|SPRING_DATASOURCE_URL=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME?useUnicode=true\&characterEncoding=utf8\&allowPublicKeyRetrieval=true\&useSSL=false\&useLegacyDatetimeCode=false\&serverTimezone=UTC\&createDatabaseIfNotExist=true|" \
  src/main/docker/app.yml

sed -i '' \
  "s|SPRING_LIQUIBASE_URL=.*|SPRING_LIQUIBASE_URL=jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME?useUnicode=true\&characterEncoding=utf8\&allowPublicKeyRetrieval=true\&useSSL=false\&useLegacyDatetimeCode=false\&serverTimezone=UTC\&createDatabaseIfNotExist=true|" \
  src/main/docker/app.yml

sed -i '' \
  "s|SPRING_DATASOURCE_USERNAME=.*|SPRING_DATASOURCE_USERNAME=$DB_USER|" \
  src/main/docker/app.yml

sed -i '' \
  "s|SPRING_DATASOURCE_PASSWORD=.*|SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD|" \
  src/main/docker/app.yml

# Activar perfil prod:
sed -i '' \
  "s|SPRING_PROFILES_ACTIVE=.*|SPRING_PROFILES_ACTIVE=prod,api-docs|" \
  src/main/docker/app.yml
```

Remover la dependencia al servicio mysql del compose (si existe):
```bash
# Eliminar líneas que referencien el servicio interno mysql del depends_on:
sed -i '' '/depends_on/,/^  [a-z]/{ /mysql/d }' src/main/docker/app.yml
```

### 8.3 Construir imagen con Jib

```bash
./mvnw package -Pdev verify jib:dockerBuild -DskipTests
```

### 8.4 Levantar solo el contenedor de la app

```bash
# Levantar únicamente el servicio de la app (sin mysql sidecar):
docker-compose -f src/main/docker/app.yml up -d "$PROJECT_NAME-app"

# O si el compose solo tiene el servicio app:
docker-compose -f src/main/docker/app.yml up -d
```

Verificar:
```bash
docker container ls
docker logs -f "${PROJECT_NAME}-app_1"
# Esperar línea: Application 'aerolineaVirtual' is running!
```

---

## FASE 9 — Commit final

```bash
cd "$PROJECT_DIR"
git add .
git commit -m "feat: deploy Docker + pruebas + build producción con BD Hostinger"
git push origin main
```

---

## FASE 10 — Resumen de estado

Reportar al usuario:
- App local (dev): `http://localhost:8080`
- App en Docker (prod): `http://localhost:8080`
- GitHub: `$GITHUB_REPO_URL`
- BD utilizada: `$DB_USER@$DB_HOST:$DB_PORT/$DB_NAME`
- Estado contenedores: salida de `docker container ls`
- Resultado pruebas backend y frontend: pass/fail

---

## 🚨 Errores comunes

| Error | Causa | Fix |
|-------|-------|-----|
| `Communications link failure` al arrancar | Hostinger no permite conexión remota desde tu IP | hPanel → Remote MySQL → agregar IP (`curl ifconfig.me`) |
| `Access denied for user` | Credenciales incorrectas en `lab_config.env` | Verificar usuario/password en hPanel |
| `SSL connection error` | Hostinger requiere SSL | Agregar `useSSL=true&requireSSL=false` a la JDBC URL |
| `Unsupported platform` npm (ARM64) | Paquete sin soporte ARM | `npm install --ignore-scripts` |
| `Address already in use :8080` | Puerto ocupado | `lsof -ti:8080 \| xargs kill` |
| `docker: no matching manifest linux/arm64` | Imagen sin ARM | Activar Rosetta en Docker Desktop Settings |
| Liquibase `ChangeSet already ran` | Import JDL doble con BD ya migrada | Verificar estado con `./mvnw liquibase:status` |
| `Cannot find module 'react-jhipster'` | Deps incompletas | `npm install react-jhipster react-router-dom` |
