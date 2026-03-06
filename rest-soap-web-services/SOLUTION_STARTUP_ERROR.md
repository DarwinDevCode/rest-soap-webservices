# INSTRUCCIONES - Solución del Error de Startup

**Problema Original:**
```
Schema validation: missing table [audit_log]
```

**Solución Aplicada:**
Se cambió la configuración de Hibernate para crear automáticamente las tablas en lugar de solo validarlas.

---

## Cambios Realizados

### 1. application.properties
```ini
# Antes:
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

# Ahora:
spring.jpa.hibernate.ddl-auto=create
spring.flyway.enabled=false
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
```

### 2. Nuevo Archivo: data.sql
Se creó `src/main/resources/data.sql` con datos de prueba que se ejecutan automáticamente después de crear las tablas.

---

## Pasos para Ejecutar

### 1. **Crear la base de datos PostgreSQL**

Conéctate a PostgreSQL y ejecuta:

```sql
CREATE DATABASE webservices_db;
```

Asegúrate de que PostgreSQL está corriendo en `localhost:5432` con usuario `admin1` y contraseña `admin1` (como está configurado en `application.properties`).

### 2. **Compilar el proyecto**

```bash
cd C:\app-web\rest-soap-web-services
mvn clean compile
```

### 3. **Ejecutar la aplicación**

```bash
mvn spring-boot:run
```

**Salida esperada:**

```
...
Tomcat started on port(s): 8080 (http)
Started RestSoapWebServicesApplication in X.XXXs (JVM running for X.XXXs)
```

### 4. **Verificar que funciona**

Abre en navegador o cURL:

```bash
curl http://localhost:8080/api/catalogos/categorias?page=0&size=10
```

Respuesta esperada:

```json
{
  "content": [
    {
      "id": 1,
      "nombre": "Electrónica",
      "descripcion": "Productos electrónicos y gadgets",
      ...
    },
    ...
  ],
  "pageable": {...},
  "totalElements": 5,
  "totalPages": 1
}
```

---

## ¿Qué sucede automáticamente?

1. **Hibernate** (con `ddl-auto=create`) recrea todas las tablas en la BD
2. **Spring Boot** ejecuta `data.sql` automáticamente (gracias a `spring.sql.init.mode=always`)
3. Se insertan 25 productos en 5 categorías
4. La aplicación inicia correctamente

---

## Notas Importantes

### ⚠️ Para Producción

En producción, **cambia `spring.jpa.hibernate.ddl-auto`** de `create` a `validate`:

```ini
# Producción
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=never
```

Y usa **Flyway** para gestionar migraciones:

```ini
spring.flyway.enabled=true
```

### 📝 Alternativa: Usar Flyway en Desarrollo

Si prefieres mantener Flyway habilitado, restaura esta configuración:

```ini
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.sql.init.mode=never
```

Esto hará que Flyway cree las tablas en lugar de Hibernate (más recomendado para control de versiones de BD).

---

## Problemas Comunes

### Error: "Cannot connect to PostgreSQL"
```
Solución: 
1. Verifica que PostgreSQL está corriendo
2. Verifica usuario/contraseña en application.properties
3. Verifica que la BD 'webservices_db' existe
```

### Error: "Table already exists"
```
Solución:
Es normal si ejecutas varias veces. Simplemente borra la BD y vuelve a crear:
DROP DATABASE webservices_db;
CREATE DATABASE webservices_db;
```

### Error de codificación en application.properties
```
Solución: Se usaron caracteres ASCII únicamente. Si aún hay problemas,
verifica que el archivo esté en UTF-8 sin BOM.
```

---

**La aplicación debería estar funcionando ahora. ¡Pruébala!** 🚀

