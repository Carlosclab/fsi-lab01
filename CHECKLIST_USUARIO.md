# ✅ Checklist del Usuario — Lab JHipster (Mac M-series)
> Completa estas tareas **antes** de pasarle el control a Claude Code.
> Una vez marcadas todas, edita `lab_config.env` con tus datos y ejecuta Claude Code.

---

## 1. Prerrequisitos de sistema

- [ ] **Java 17 (JDK)** instalado
  ```bash
  java -version   # debe mostrar 17.x

  # Si no está:
  brew install openjdk@17
  sudo ln -sfn /opt/homebrew/opt/openjdk@17/libcask/openjdk-17.jdk \
    /Library/Java/JavaVirtualMachines/openjdk-17.jdk
  # Agregar a ~/.zshrc:
  export JAVA_HOME=$(/usr/libexec/java_home -v 17)
  ```

- [ ] **Maven** instalado
  ```bash
  brew install maven && mvn -version
  ```

- [ ] **Git** instalado y configurado
  ```bash
  git config --global user.name "Tu Nombre"
  git config --global user.email "tu@email.com"
  ```

- [ ] **Node.js 20 LTS** via nvm
  ```bash
  curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
  nvm install 20 && nvm use 20
  node -v && npm -v
  ```

- [ ] **JHipster 8** instalado globalmente
  ```bash
  npm install -g generator-jhipster
  jhipster --version   # debe mostrar 8.x
  ```

---

## 2. Docker Desktop

- [ ] Descargar e instalar desde https://www.docker.com/products/docker-desktop/ → *Mac with Apple chip*
- [ ] Ejecutándose (ícono ballena en barra de menú)
  ```bash
  docker --version && docker ps
  ```

---

## 3. Base de datos — Hostinger (sin instalación local)

> ✅ **No instalas MySQL en tu Mac.** Usas tu instancia Hostinger para todo (dev y prod).

Desde el panel **hPanel → Bases de datos MySQL**, recoge:

- [ ] **Host / IP** del servidor (ej: `srv1234.hstgr.io` o una IP pública)
- [ ] **Puerto** (confirmar, usualmente `3306`)
- [ ] **Nombre de la base de datos** (créala si no existe, ej: `aerolineavirtual`)
- [ ] **Usuario** de la base de datos
- [ ] **Contraseña**

> ⚠️ **Paso crítico — conexiones remotas:**
> hPanel → Bases de datos MySQL → **Remote MySQL** → agrega tu IP pública.
> Obtén tu IP con: `curl ifconfig.me`
> Sin este paso JHipster no podrá conectarse al arrancar.

Prueba de conectividad (opcional pero recomendado):
```bash
# Instala solo el cliente, no el servidor:
brew install mysql-client
echo 'export PATH="/opt/homebrew/opt/mysql-client/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

mysql -h TU_HOST -P 3306 -u TU_USUARIO -p
# Ingresa la contraseña; si ves "mysql>" la conexión funciona.
```

---

## 4. GitHub

- [ ] Repositorio **vacío** creado (ej: `aerolinea-virtual-jhipster`)
- [ ] Autenticación configurada
  ```bash
  ssh -T git@github.com   # SSH
  # o configura un Personal Access Token para HTTPS
  ```

---

## 5. Llenar `lab_config.env`

Abre el archivo y reemplaza cada valor con tus datos reales de Hostinger y GitHub.

---

## 6. Directorio de trabajo

- [ ] Crear la carpeta del proyecto
  ```bash
  mkdir -p ~/Desktop/aerolineaVirtual
  ```

---

## ⚠️ Notas Mac M-series

- Siempre usar `./mvnw` (no `mvnw.cmd`)
- Docker Desktop → Settings → General → activar *"Use Rosetta for x86/amd64 emulation"* si hay errores de arquitectura
- Nunca `sudo npm`; si hay errores de permisos:
  ```bash
  mkdir ~/.npm-global && npm config set prefix '~/.npm-global'
  # Agregar a ~/.zshrc: export PATH=~/.npm-global/bin:$PATH
  ```
