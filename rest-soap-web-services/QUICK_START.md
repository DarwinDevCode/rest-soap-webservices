# Guía de Inicio Rápido - Servicios Web Escalables

## 1. Requisitos Previos

- **Java 21+** instalado
- **PostgreSQL 12+** instalado y corriendo
- **Maven** (incluido con mvnw.cmd)
- **Postman** o **Insomnia** (opcional, para pruebas de API)

---

## 2. Configuración de la Base de Datos

### Crear base de datos en PostgreSQL

```sql
CREATE DATABASE webservices_db;

-- Conectar a la base de datos
\c webservices_db;

-- Flyway ejecutará las migraciones automáticamente al iniciar la aplicación
-- V1__initial_schema.sql - Crea todas las tablas
-- V2__insert_test_data.sql - Inserta datos de prueba
```

**Configuración en `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/webservices_db
spring.datasource.username=admin1
spring.datasource.password=admin1
```

---

## 3. Configurar Credenciales OAuth2

Editar `src/main/resources/application.properties`:

### Google OAuth2
1. Ir a [Google Cloud Console](https://console.cloud.google.com)
2. Crear un proyecto
3. Habilitar "Google+ API"
4. Crear credenciales (OAuth 2.0 Client ID)
5. Copiar Client ID y Client Secret a application.properties:

```properties
spring.security.oauth2.client.registration.google.client-id=<tu-client-id>
spring.security.oauth2.client.registration.google.client-secret=<tu-client-secret>
```

### GitHub OAuth2
1. Ir a [GitHub Settings > Developer settings > OAuth Apps](https://github.com/settings/developers)
2. Crear una nueva OAuth App
3. Copiar Client ID y Client Secret:

```properties
spring.security.oauth2.client.registration.github.client-id=<tu-client-id>
spring.security.oauth2.client.registration.github.client-secret=<tu-client-secret>
```

---

## 4. Compilar el Proyecto

```bash
cd C:\app-web\rest-soap-web-services

# Compilar
.\mvnw.cmd clean compile

# O compilar con tests
.\mvnw.cmd clean test
```

---

## 5. Ejecutar la Aplicación

```bash
.\mvnw.cmd spring-boot:run
```

**Salida esperada:**
```
  _____ __________  _   _____  __________________
 / ___// __________/ / / / _ \/ ___/_  __/ ____/
 \__ \ / /  / ___/ / / / // / /    / / / / __/ 
___/ // /__/ /  / /_/ / /_/ / /    / / / /___ 
___/ \______/   \____/\____/_/    /_/  \____/

Started RestSoapWebServicesApplication in X.XXX seconds
```

---

## 6. Verificar que la Aplicación Está Corriendo

```bash
curl http://localhost:8080/api/catalogos/categorias?page=0&size=10
```

**Respuesta esperada:**
```json
{
  "content": [...],
  "pageable": {...},
  "totalElements": 5,
  "totalPages": 1
}
```

---

## 7. Pruebas Rápidas

### 7.1 APIs REST - Obtener Categorías
```bash
curl -X GET "http://localhost:8080/api/catalogos/categorias?page=0&size=10" \
  -H "Content-Type: application/json"
```

### 7.2 APIs REST - Crear Categoría
```bash
curl -X POST "http://localhost:8080/api/catalogos/categorias" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Nueva Categoría","descripcion":"Descripción"}'
```

### 7.3 SOAP - Conversión de Divisas
Usar Postman o similar:
- **URL:** `http://localhost:8080/ws`
- **Método:** POST
- **Content-Type:** text/xml
- **Body:**
```xml
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

### 7.4 OAuth2 Login
```
http://localhost:8080/oauth2/authorization/google
http://localhost:8080/oauth2/authorization/github
```

### 7.5 Obtener Perfil de Usuario Autenticado
```bash
curl -X GET "http://localhost:8080/api/usuarios/perfil"
```

---

## 8. Ejecutar Tests

### Tests Unitarios
```bash
.\mvnw.cmd test
```

### Tests Específicos
```bash
# Solo tests de Categoría
.\mvnw.cmd test -Dtest=CategoriaServiceTest

# Solo tests de Carga
.\mvnw.cmd test -Dtest=ConcurrencyLoadTest
```

---

## 9. Ver WSDL del Servicio SOAP

```
http://localhost:8080/ws/divisas.wsdl
```

---

## 10. Importar Colección en Postman

1. Descargar `Postman-Collection.json`
2. En Postman: **Import** > Seleccionar archivo
3. Usar las requests pregrabadas para probar todos los endpoints

---

## 11. Troubleshooting

### Error: "Falló la conexión a PostgreSQL"
- Verificar que PostgreSQL está corriendo
- Verificar credenciales en `application.properties`
- Crear la base de datos: `CREATE DATABASE webservices_db;`

### Error: "javax.servlet.ServletException: No bean named 'messageDispatcherServlet'"
- Ignorar, es un warning normal de Spring WS

### Error de compilación con JAXB
- Ejecutar: `.\mvnw.cmd clean compile` nuevamente
- El primer build genera clases JAXB

### OAuth2 no funciona
- Verificar Client ID y Secret en application.properties
- Verificar que la URL de redirección esté configurada en el proveedor OAuth

---

## 12. Estructura de Carpetas Generadas

```
target/
├── classes/
│   └── org/uteq/restsoapwebservices/  (Clases compiladas)
├── generated-sources/
│   └── jaxb/  (Clases generadas por JAXB desde XSD)
└── generated-test-sources/  (Clases de test generadas)
```

---

## 13. URLs Principales

| Descripción | URL |
|---|---|
| **Home** | http://localhost:8080 |
| **Categorías REST** | http://localhost:8080/api/catalogos/categorias |
| **Productos REST** | http://localhost:8080/api/catalogos/productos |
| **Usuarios** | http://localhost:8080/api/usuarios/perfil |
| **SOAP WSDL** | http://localhost:8080/ws/divisas.wsdl |
| **Heroku Info** | http://localhost:8080/api/heroku/info |
| **Google OAuth2** | http://localhost:8080/oauth2/authorization/google |
| **GitHub OAuth2** | http://localhost:8080/oauth2/authorization/github |

---

## 14. Próximos Pasos

1. ✅ Compilar y ejecutar la aplicación
2. ✅ Probar APIs REST con Postman
3. ✅ Probar SOAP con Postman/Insomnia
4. ✅ Configurar OAuth2 con Google/GitHub
5. ✅ Ejecutar pruebas unitarias
6. ✅ Revisar datos de prueba en BD
7. ✅ Documentar en informe técnico

---

**¿Necesitas ayuda?** Revisa el archivo `README.md` para documentación completa.

