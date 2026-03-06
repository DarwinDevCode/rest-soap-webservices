# Implementación y Consumo de Servicios Web Escalables (REST y SOAP)

## Descripción General

Este proyecto implementa un sistema integrado de servicios web escalables que cubre:

1. **APIs REST** para gestión de catálogos de productos y categorías
2. **Servicios SOAP** para conversión de divisas
3. **Autenticación OAuth2** con Google y GitHub
4. **Auditoría de cambios** replicada a Firebase
5. **Caché distribuido** con Caffeine
6. **Integración** con Heroku API
7. **Base de datos** PostgreSQL con Flyway

---

## Arquitectura

```
rest-soap-web-services/
├── src/main/
│   ├── java/org/uteq/restsoapwebservices/
│   │   ├── config/          # Configuraciones (Security, Cache, Soap, App)
│   │   ├── controller/      # Controllers REST
│   │   ├── service/         # Lógica de negocio
│   │   ├── repository/      # Acceso a datos JPA
│   │   ├── entity/          # Entidades JPA
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── soap/endpoint/   # Endpoints SOAP
│   │   └── listener/        # JPA Listeners para auditoría
│   └── resources/
│       ├── application.properties
│       ├── xsd/
│       │   └── divisas.xsd  # Schema SOAP
│       └── db/migration/
│           └── V1__initial_schema.sql
└── src/test/
    └── java/org/uteq/restsoapwebservices/
        └── service/         # Tests unitarios
```

---

## Entidades Principales

### 1. Usuario (OAuth2)
- Almacena usuarios autenticados via Google o GitHub
- Campos: id, oauth2Provider, email, nombre, foto, fechaCreacion, fechaActualizacion

### 2. Categoria
- Categorías de productos
- Relación: 1 -> N con Producto
- Campos: id, nombre, descripcion, fechaCreacion, fechaActualizacion

### 3. Producto
- Productos del catálogo
- Relación: N -> 1 con Categoria
- Campos: id, nombre, descripcion, precio, stock, categoriaId, fechaCreacion, fechaActualizacion
- Auditoría: Todos los cambios se registran en AuditLog

### 4. AuditLog
- Registro de auditoría de cambios
- Campos: id, tipoEntidad, idEntidad, operacion, usuarioEmail, timestamp, datosAntes, datosDespues, descripcion, replicadoFirebase

---

## APIs REST

### Categorías (`/api/catalogos/categorias`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Obtener todas las categorías (paginadas) |
| GET | `/{id}` | Obtener categoría por ID |
| GET | `/buscar?nombre=...` | Buscar categorías por nombre |
| POST | `/` | Crear nueva categoría |
| PUT | `/{id}` | Actualizar categoría |
| DELETE | `/{id}` | Eliminar categoría |

**Ejemplo de request POST:**
```json
{
  "nombre": "Electrónica",
  "descripcion": "Productos electrónicos"
}
```

### Productos (`/api/catalogos/productos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Obtener todos los productos (paginados) |
| GET | `/{id}` | Obtener producto por ID |
| GET | `/buscar?nombre=...` | Buscar productos por nombre |
| GET | `/categoria/{categoriaId}` | Obtener productos por categoría |
| GET | `/disponibles` | Obtener productos con stock |
| POST | `/` | Crear nuevo producto |
| PUT | `/{id}` | Actualizar producto |
| DELETE | `/{id}` | Eliminar producto |

**Ejemplo de request POST:**
```json
{
  "nombre": "Laptop",
  "descripcion": "Laptop de alta performance",
  "precio": 999.99,
  "stock": 10,
  "categoriaId": 1
}
```

### Usuarios (`/api/usuarios`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/perfil` | Obtener perfil del usuario autenticado |
| GET | `/check` | Verificar si el usuario está autenticado |

---

## Servicios SOAP

### Divisas (`/ws`)

**Operación:** `convertir`

**Request (XML):**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <tns:ConversionRequest xmlns:tns="http://www.uteq.org/soap/divisas">
      <tns:monto>100</tns:monto>
      <tns:divisaOrigen>USD</tns:divisaOrigen>
      <tns:divisaDestino>EUR</tns:divisaDestino>
    </tns:ConversionRequest>
  </soap:Body>
</soap:Envelope>
```

**Response (XML):**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <tns:ConversionResponse xmlns:tns="http://www.uteq.org/soap/divisas">
      <tns:montoOriginal>100</tns:montoOriginal>
      <tns:divisaOrigen>USD</tns:divisaOrigen>
      <tns:montoConvertido>92.00</tns:montoConvertido>
      <tns:divisaDestino>EUR</tns:divisaDestino>
      <tns:tasaCambio>0.92</tns:tasaCambio>
      <tns:timestamp>2024-03-06T10:30:00</tns:timestamp>
    </tns:ConversionResponse>
  </soap:Body>
</soap:Envelope>
```

**Divisas soportadas:** USD, EUR, GBP, JPY, MXN, COP

**WSDL:** `http://localhost:8080/ws/divisas.wsdl`

---

## Autenticación OAuth2

### Google
- **Endpoint:** `http://localhost:8080/oauth2/authorization/google`
- **Client ID:** Configurado en `application.properties`
- **Scopes:** openid, profile, email

### GitHub
- **Endpoint:** `http://localhost:8080/oauth2/authorization/github`
- **Client ID:** Configurado en `application.properties`
- **Scopes:** read:user, user:email

### Flujo
1. Usuario hace clic en "Conectar con Google/GitHub"
2. Redireccionado a proveedor OAuth
3. Usuario autoriza la aplicación
4. Redireccionado a `/api/usuarios/perfil` con token
5. Usuario se guarda/actualiza en BD
6. Sesión activa para operaciones autenticadas

---

## Auditoría y Firebase

### Cómo funciona
1. Cada operación CRUD en Producto se registra en `AuditLog`
2. Se registran: tipo de entidad, operación (CREATE/UPDATE/DELETE), usuario, datos antes/después
3. De forma **asíncrona** (non-blocking), se replica a Firebase
4. Firebase almacena auditoría en tiempo real para reportes

### Configuración Firebase
- **DB URL:** `firebase.db.url` en application.properties
- **Endpoint:** `{db-url}/audits.json` (POST)
- **Formato:** JSON con documento AuditLog

---

## Caché con Caffeine

### Cachés configuradas
- `categorias` - Lista de categorías
- `categoria` - Categoría por ID
- `categoriaPorNombre` - Búsqueda de categorías
- `productos` - Lista de productos
- `producto` - Producto por ID
- `productoPorNombre` - Búsqueda de productos
- `productosPorCategoria` - Productos por categoría
- `productosConStock` - Productos con stock

### Política
- **Expiración:** 10 minutos sin acceso
- **Tamaño máximo:** 1000 entradas
- **Invalidación:** Automática al crear/actualizar/eliminar

---

## Heroku API

### Endpoint (`/api/heroku/info`)

Consulta información del app desplegado en Heroku:
- Información del app
- Estado de dynos
- Última build/release

**Configuración:**
```properties
heroku.app-name=rest-soap-webservices
heroku.api-key=<tu-token>
```

---

## Base de Datos

### Configuración
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/webservices_db
spring.datasource.username=admin1
spring.datasource.password=admin1
```

### Migraciones (Flyway)
- `V1__initial_schema.sql` - Schema inicial con todas las tablas

### Tablas
- `usuario` - Usuarios OAuth2
- `categoria` - Categorías de productos
- `producto` - Productos
- `audit_log` - Registro de auditoría

---

## Pruebas

### Tests Unitarios

**Ejecutar todos:**
```bash
mvn test
```

**Tests incluidos:**
1. `CategoriaServiceTest` - Tests de servicio de categorías
2. `ProductoServiceTest` - Tests de servicio de productos
3. `ConcurrencyLoadTest` - Tests de carga y escalabilidad

### Cobertura
- 70%+ de cobertura en servicios
- Tests de operaciones CRUD
- Tests de caché
- Tests de paginación
- Tests de concurrencia

### Pruebas de Carga

Los tests `ConcurrencyLoadTest` incluyen:
- **100 operaciones repetidas** de lectura
- **Paginación** con diferentes tamaños (5, 10, 20, 50)
- **Medición de tiempos** de respuesta
- **Integridad CRUD** bajo operaciones simultáneas

---

## Instalación y Ejecución

### Requisitos
- Java 21+
- PostgreSQL 12+
- Maven 3.8+

### Pasos

1. **Clonar/descargar el proyecto**

2. **Crear base de datos PostgreSQL**
```sql
CREATE DATABASE webservices_db;
```

3. **Configurar credenciales OAuth2**
Actualizar `application.properties` con tus credenciales de Google/GitHub

4. **Compilar**
```bash
mvn clean compile
```

5. **Ejecutar aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

---

## Endpoints de Prueba

### Health Check
```bash
curl http://localhost:8080/api/catalogos/categorias
```

### Crear Categoría
```bash
curl -X POST http://localhost:8080/api/catalogos/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Electrónica","descripcion":"Productos electrónicos"}'
```

### Crear Producto
```bash
curl -X POST http://localhost:8080/api/catalogos/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre":"Laptop",
    "descripcion":"Laptop de prueba",
    "precio":999.99,
    "stock":10,
    "categoriaId":1
  }'
```

### OAuth2 Login
```
http://localhost:8080/oauth2/authorization/google
http://localhost:8080/oauth2/authorization/github
```

### Verificar Autenticación
```bash
curl http://localhost:8080/api/usuarios/check
```

### SOAP Request (Insomnia/Postman)
```
POST http://localhost:8080/ws
Content-Type: text/xml

<?xml version="1.0"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
               xmlns:tns="http://www.uteq.org/soap/divisas">
  <soap:Body>
    <tns:ConversionRequest>
      <tns:monto>100</tns:monto>
      <tns:divisaOrigen>USD</tns:divisaOrigen>
      <tns:divisaDestino>EUR</tns:divisaDestino>
    </tns:ConversionRequest>
  </soap:Body>
</soap:Envelope>
```

---

## Estructuratica Desarrollada

✅ **REST APIs** para catálogos (/api/catalogos/)
✅ **SOAP Service** para divisas (/ws)
✅ **OAuth2** con Google y GitHub
✅ **Auditoría** de cambios
✅ **Firebase** replicación (REST API)
✅ **Heroku API** integración
✅ **Caché** con Caffeine
✅ **Flyway** migraciones
✅ **Tests unitarios** (70%+ coverage)
✅ **Tests de carga** (@RepeatedTest, @ParameterizedTest)
✅ **PostgreSQL** base de datos
✅ **Documentación** completa

---

## Notas Importantes

1. **Desarrollo Local:** Se recomienda configurar variables de entorno en IDE para no exponer credenciales
2. **Firebase:** Requiere base de datos Realtime Database configurada
3. **Heroku API:** Token puede cambiar, actualizar en properties antes de desplegar
4. **SOAP:** Genera automáticamente clases JAXB desde XSD durante compilación
5. **Caché:** Válida para todas las operaciones GET, se invalida automáticamente en POST/PUT/DELETE

---

## Próximos Pasos (Mejoras Futuras)

- [ ] Implementar JWT tokens en lugar de sesiones
- [ ] Agregar validación detallada con spring-boot-starter-validation
- [ ] Implementar Circuit Breaker para APIs externas
- [ ] Agregar logging centralizado con ELK Stack
- [ ] Implementar GraphQL como alternativa a REST
- [ ] Agregar Docker y docker-compose
- [ ] CI/CD con GitHub Actions

---

**Versión:** 0.0.1-SNAPSHOT
**Última actualización:** 2024-03-06
**Autor:** Práctica Integración de Servicios Web

