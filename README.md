# 🏋️ GoldGYMAPI

¡Bienvenido al repositorio de **GoldGYMAPI**! Esta es la API backend, construida con **Spring Boot**, diseñada para gestionar eficientemente todas las operaciones de un gimnasio.

## 🌟 Características Principales

* **Gestión de Miembros:** CRUD (Crear, Leer, Actualizar, Eliminar) de la información de los miembros (usando el paquete `entities` y `repository`).
* **Gestión de Planes y Pagos:** Administración de diferentes planes de membresía.
* **Seguridad:** Implementación de autenticación y autorización con **JWT** (JSON Web Tokens) usando el paquete `jwt`.
* **Arquitectura MVC:** Separación clara de lógica en `controllers`, `services` y `repository`.

## 🚀 Empezando

Sigue estos pasos para configurar y ejecutar la API en tu entorno local.

### Prerrequisitos

Necesitarás tener instalado:

* **Java Development Kit (JDK):** Versión 17 o superior (recomendado).
* **Apache Maven:** Versión 3.6 o superior.
* Una base de datos (por ejemplo, **PostgreSQL** o **MySQL**).

### Instalación y Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Hyground/GoldGYMAPI.git](https://github.com/Hyground/GoldGYMAPI.git)
    cd GoldGYMAPI
    ```

2.  **Configuración del Entorno:**
    Abre el archivo `src/main/resources/application.properties` y configura los parámetros de tu base de datos y la llave secreta para JWT.

    ```properties
    # Ejemplo de application.properties
    server.port=8080
    spring.datasource.url=jdbc:postgresql://localhost:5432/goldgymdb
    spring.datasource.username=usuario
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    
    # Propiedad para JWT (ajusta a tu necesidad)
    jwt.secret=una_clave_secreta_fuerte_para_jwt
    ```

3.  **Compilar el Proyecto (Usando Maven):**
    Construye el proyecto y resuelve todas las dependencias:
    ```bash
    mvn clean install
    ```

4.  **Ejecutar la Aplicación Spring Boot:**
    Puedes ejecutar el archivo JAR generado o usar el plugin de Spring Boot directamente:
    ```bash
    mvn spring-boot:run
    ```
    La API estará corriendo en `http://localhost:8080`.

## 📚 Documentación de la API

La API expone sus endpoints principales a través de los `controllers`.

* **Ruta Base:** `/api/v1` (o la ruta configurada en tus controladores).

| Método | Ruta | Descripción | Requiere JWT |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Autentica un usuario y devuelve un token JWT. | No |
| `POST` | `/api/v1/miembros` | Registra un nuevo miembro en el gimnasio. | Sí |
| `GET` | `/api/v1/miembros/:id` | Obtiene la información detallada de un miembro. | Sí |
| `PUT` | `/api/v1/planes/:id` | Actualiza un plan de membresía existente. | Sí |

*(**Nota:** Si utilizas Swagger o OpenAPI, añade el enlace aquí: La documentación de Swagger se encuentra en `http://localhost:8080/swagger-ui.html`)*

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java
* **Framework:** Spring Boot
* **Gestor de Dependencias:** Apache Maven (`pom.xml`)
* **Persistencia:** Spring Data JPA / Hibernate
* **Seguridad:** Spring Security + JWT
* **Estructura:** DTOs, Services, Repositories, Controllers.

## 🤝 Contribuyendo

Las contribuciones son bienvenidas. Para contribuir:

1.  Haz un *fork* del proyecto.
2.  Crea tu rama de *feature* (`git checkout -b feature/nueva-funcionalidad`).
3.  Comitea tus cambios (`git commit -m 'feat: añadir nueva funcionalidad X'`).
4.  Sube la rama (`git push origin feature/nueva-funcionalidad`).
5.  Abre un *Pull Request* claro y conciso.
