# Actividad Evaluable: Control de Versiones (0487 - Entornos de Desarrollo)

Aplicación cliente-servidor desarrollada en **Java** y **HTML** que realiza exclusivamente operaciones de **suma**, **resta**, **multiplicación** y **potencia** con números enteros de hasta 5 dígitos (`[-99999 ... 99999]`).

---

## Ejecución de la aplicación

1. Compilar el servidor Java:
   ```bash
   javac Server.java
   ```
2. Ejecutar el servidor:
   ```bash
   java Server
   ```
3. Abrir en el navegador:
   `http://localhost:8080`

---

## Comandos utilizados en orden cronológico

```bash
# 1. Clonar el repositorio creado previamente en GitHub con README.md
git clone [https://github.com/prodrigu-ux/calculadora-git-java.git](https://github.com/prodrigu-ux/calculadora-git-java.git)
cd calculadora-git-java

# 2. Crear y cambiar a la rama backend
git checkout -b backend

# 3. Crear Server.java, añadirlo, confirmarlo y subirlo exclusivamente en backend
git add Server.java
git commit -m "Implementar logica backend en Java con validacion de enteros de 5 digitos"
git push -u origin backend

# 4. Volver a main y crear la rama frontend
git checkout main
git checkout -b frontend

# 5. Crear index.html, añadirlo, confirmarlo y subirlo exclusivamente en frontend
git add index.html
git commit -m "Crear interfaz frontend en HTML para operaciones permitidas"
git push -u origin frontend

# 6. Volver a main e integrar ambas ramas mediante merge
git checkout main
git merge backend
git merge frontend --no-edit

# 7. Actualizar comentarios en el codigo fuente dentro de main
git add Server.java index.html
git commit -m "Actualizar comentarios en el codigo fuente"

# 8. Documentar comandos y proceso en README.md dentro de main y subir cambios
git add README.md
git commit -m "Actualizar comentarios y documentacion de comandos en README.md"
git push origin main
```

---

## Explicación detallada de los comandos y publicación en GitHub

### Cómo crear y publicar un repositorio en GitHub

1. **Creación en GitHub (recomendado con `README.md` inicial):**
   * Acceder a GitHub y pulsar en **New repository**.
   * Asignar un nombre al repositorio (`calculadora-git-java`), configurarlo como **Public** (público) para que pueda ser evaluado mediante enlace directo y marcar la opción **Add a README file**.
   * Al crearlo así, GitHub genera automáticamente la rama principal `main` con un primer commit.
2. **Alternativa desde cero (si se inicia en local sin `README.md`):**
   * `git init`: inicializa un repositorio Git vacío en la carpeta local.
   * `git branch -M main`: renombra la rama inicial a `main`.
   * `git remote add origin https://github.com/prodrigu-ux/calculadora-git-java.git`: vincula el repositorio local con el repositorio remoto en GitHub.
   * `git push -u origin main`: sube los commits locales de `main` al repositorio remoto y deja vinculada la rama para futuros `git push`.

### Función de cada comando empleado

* `git clone <url>`: descarga una copia completa del repositorio remoto (incluyendo su historial y ramas) a una carpeta local y configura automáticamente el remoto `origin`.
* `git checkout -b <rama>`: crea una nueva rama a partir del commit actual y cambia el directorio de trabajo a dicha rama en un solo paso (`backend` para Java y `frontend` para HTML).
* `git checkout <rama>`: cambia de una rama a otra (por ejemplo, para regresar a `main` antes de crear `frontend` o antes de realizar los `merge`).
* `git add <archivo>`: añade los cambios del archivo indicado al área de preparación (*staging area*), garantizando que en `backend` solo se incluyan archivos `.java` y en `frontend` solo archivos `.html`.
* `git commit -m "<mensaje>"`: registra una nueva instantánea en el historial de la rama activa con un mensaje descriptivo.
* `git push -u origin <rama>`: publica la rama local y sus commits en el repositorio remoto `origin`, estableciendo el seguimiento (*upstream*) entre la rama local y la remota.
* `git merge <rama>`: fusiona los commits de la rama indicada (`backend` o `frontend`) dentro de la rama en la que nos encontramos (`main`), generando los commits de fusión correspondientes.
* `git push origin main`: sube a GitHub tanto los merges realizados en `main` como los commits posteriores de actualización de comentarios y documentación en `README.md`.