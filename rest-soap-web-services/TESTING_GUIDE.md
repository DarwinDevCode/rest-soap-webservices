# Testing Guide - Servicios Web Escalables

## Tipos de Pruebas

### 1. Unit Tests (JUnit 5)

#### Ejecutar Todos los Tests
```bash
cd C:\app-web\rest-soap-web-services
.\mvnw.cmd test
```

#### Ejecutar Tests Específicos

**Solo tests de Categoría**
```bash
.\mvnw.cmd test -Dtest=CategoriaServiceTest
```

**Solo tests de Producto**
```bash
.\mvnw.cmd test -Dtest=ProductoServiceTest
```

**Solo tests de Carga y Concurrencia**
```bash
.\mvnw.cmd test -Dtest=ConcurrencyLoadTest
```

#### Ejecutar Un Test Específico
```bash
.\mvnw.cmd test -Dtest=CategoriaServiceTest#testCrearCategoria
```

### 2. Tests de Carga - JUnit (@RepeatedTest y @ParameterizedTest)

El archivo `ConcurrencyLoadTest.java` incluye:

#### Test 1: 100 Operaciones Concurrentes
```java
@RepeatedTest(100)
void testOperacionesLecturaEnMasa()
```
- Simula 100 lecturas de catálogo
- Verifica que no hay fallos
- Mide estabilidad

#### Test 2: Paginación con Diferentes Tamaños
```java
@ParameterizedTest
@ValueSource(ints = {5, 10, 20, 50})
void testPaginacionConDiferentesTamanos(int tamano)
```
- Prueba con tamaños: 5, 10, 20, 50 registros por página
- Verifica que la paginación es correcta
- Mide rendimiento según tamaño

#### Test 3: Medición de Tiempos (50 Creaciones)
```java
void testTiempoRespuestaCreaciones()
```
- Crea 50 productos
- Mide tiempo total y promedio
- Assert: Máximo 5000ms (100ms promedio)

#### Test 4: Medición de Búsquedas
```java
void testTiempoRespuestaBusquedas()
```
- 100 productos + 50 búsquedas
- Mide beneficio de caché
- Assert: Máximo 2000ms

#### Test 5: Integridad CRUD
```java
@RepeatedTest(10)
void testIntegridadCRUDSimultaneo()
```
- Crea, lee, actualiza, lee, elimina
- Repite 10 veces
- Verifica integridad de datos

### 3. Pruebas Manuales con Postman/cURL

#### Pre-requisitos
- Aplicación corriendo en `http://localhost:8080`
- PostgreSQL con base de datos configurada

#### 3.1 Test REST API - Categorías

**GET - Listar categorías**
```bash
curl -X GET "http://localhost:8080/api/catalogos/categorias?page=0&size=10" \
  -H "Content-Type: application/json"
```

**POST - Crear categoría**
```bash
curl -X POST "http://localhost:8080/api/catalogos/categorias" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Prueba Categoría",
    "descripcion": "Categoría de prueba"
  }'
```

**PUT - Actualizar categoría**
```bash
curl -X PUT "http://localhost:8080/api/catalogos/categorias/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Categoría Actualizada",
    "descripcion": "Nueva descripción"
  }'
```

**DELETE - Eliminar categoría**
```bash
curl -X DELETE "http://localhost:8080/api/catalogos/categorias/1"
```

#### 3.2 Test REST API - Productos

**Crear producto** (requiere categoría existente)
```bash
curl -X POST "http://localhost:8080/api/catalogos/productos" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop Prueba",
    "descripcion": "Laptop para pruebas",
    "precio": 999.99,
    "stock": 10,
    "categoriaId": 1
  }'
```

**Buscar productos**
```bash
curl -X GET "http://localhost:8080/api/catalogos/productos/buscar?nombre=Laptop&page=0&size=10"
```

**Productos disponibles (con stock)**
```bash
curl -X GET "http://localhost:8080/api/catalogos/productos/disponibles?page=0&size=10"
```

#### 3.3 Test SOAP - Divisas

**Request SOAP (Postman/Insomnia)**

```xml
POST /ws HTTP/1.1
Host: localhost:8080
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

**Response esperado:**
```xml
<?xml version="1.0"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <tns:ConversionResponse xmlns:tns="http://www.uteq.org/soap/divisas">
      <tns:montoOriginal>100</tns:montoOriginal>
      <tns:divisaOrigen>USD</tns:divisaOrigen>
      <tns:montoConvertido>92.00</tns:montoConvertido>
      <tns:divisaDestino>EUR</tns:divisaDestino>
      <tns:tasaCambio>0.92</tns:tasaCambio>
      <tns:timestamp>2026-03-06T10:30:00</tns:timestamp>
    </tns:ConversionResponse>
  </soap:Body>
</soap:Envelope>
```

#### 3.4 Test OAuth2

**Google Login**
```
http://localhost:8080/oauth2/authorization/google
```

**GitHub Login**
```
http://localhost:8080/oauth2/authorization/github
```

**Obtener perfil (después de autenticar)**
```bash
curl -X GET "http://localhost:8080/api/usuarios/perfil" \
  -H "Cookie: JSESSIONID=<session-id>"
```

#### 3.5 Test Heroku API

```bash
curl -X GET "http://localhost:8080/api/heroku/info"
```

### 4. Test con JMeter

**Archivo:** `jmeter-test-plan.jmx`

**Carga del plan**
```bash
jmeter -n -t jmeter-test-plan.jmx -l results.jtl -j jmeter.log
```

**Parámetros:**
- 50 threads (usuarios simulados)
- 10 iteraciones cada uno
- Ramp-up: 10 segundos

**Resultados en:** `results.jtl` y `jmeter.log`

---

## Interpretación de Resultados

### Tests Unitarios

**Output esperado (SUCCESS):**
```
[INFO] BUILD SUCCESS
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
```

**Caso de fallo:**
```
[INFO] Tests run: 23, Failures: 1, Errors: 0, Skipped: 0
```

### Pruebas de Carga

**Métricas esperadas:**
- 50 creaciones: < 5000ms (< 100ms promedio)
- 50 búsquedas con caché: < 2000ms (< 40ms promedio)
- 100 operaciones concurrentes: 100% éxito

**Ejemplo de output:**
```
Tiempo para 50 creaciones: 2345ms
Promedio por creación: 46.9ms
```

---

## Casos de Prueba Recomendados

### Caso 1: CRUD Completo de Categoría
1. GET /api/catalogos/categorias (listar)
2. POST /api/catalogos/categorias (crear)
3. GET /api/catalogos/categorias/{id} (leer)
4. PUT /api/catalogos/categorias/{id} (actualizar)
5. DELETE /api/catalogos/categorias/{id} (eliminar)

### Caso 2: CRUD Completo de Producto
1. POST /api/catalogos/categorias (crear categoría)
2. POST /api/catalogos/productos (crear producto)
3. GET /api/catalogos/productos/{id} (leer)
4. PUT /api/catalogos/productos/{id} (actualizar stock)
5. GET /api/catalogos/productos/disponibles (verificar en disponibles)
6. DELETE /api/catalogos/productos/{id} (eliminar)

### Caso 3: Auditoría
1. Crear un producto
2. Consultar `audit_log` en BD
3. Verificar que se registró CREATE
4. Actualizar producto
5. Verificar que se registró UPDATE
6. Eliminar producto
7. Verificar que se registró DELETE

### Caso 4: Caché
1. Primer GET de categorías (sin caché) - medir tiempo T1
2. Segundo GET de categorías (con caché) - medir tiempo T2
3. Esperar <20s
4. Tercer GET (aún con caché) - medir tiempo T3
5. Esperar 10+ minutos (expira caché)
6. Cuarto GET (sin caché nuevamente) - medir tiempo T4
7. Verificar: T2 < T1, T3 < T1, T4 ≈ T1

### Caso 5: Paginación
1. GET /api/catalogos/productos?page=0&size=5 (primeros 5)
2. GET /api/catalogos/productos?page=1&size=5 (siguientes 5)
3. Verificar que totalElements = 25 (o cantidad creada)
4. Verificar que totalPages = 5 (para 25 productos, tamaño 5)

---

## Checklist Antes de Entregar

- [ ] ✅ Compilación exitosa (`mvn clean compile`)
- [ ] ✅ Todos los unit tests pasan (`mvn test`)
- [ ] ✅ Tests de carga ejecutados y documentados
- [ ] ✅ Postman Collection importada y requests funcionan
- [ ] ✅ SOAP WSDL accesible en `/ws/divisas.wsdl`
- [ ] ✅ OAuth2 logins funcionan (Google + GitHub)
- [ ] ✅ Base de datos con datos de prueba (25 productos)
- [ ] ✅ Caché funciona (verificado con diferencia de tiempos)
- [ ] ✅ Auditoría registra cambios en BD y Firebase
- [ ] ✅ API Heroku devuelve respuesta (aunque sea vacía sin configuración)

---

## Solución de Problemas

### "Connection refused" en PostgreSQL
```
Solución: Verificar que PostgreSQL está corriendo
- Windows: Services > PostgreSQL
- Linux: systemctl start postgresql
```

### "OAuth2 credentials inválidos"
```
Solución: Actualizar en application.properties
- Ir a Google Cloud Console / GitHub Settings
- Copiar Client ID y Secret nuevos
```

### "Caché no funciona"
```
Solución: Verificar que @EnableCaching está en main class
- RestSoapWebServicesApplication.java debe tener @EnableCaching
```

### "JAXB classes not found"
```
Solución: Ejecutar compilación completa
- mvn clean compile (no mvn compile)
```

---

**Documento de Testing - Versión 1.0**  
**Fecha:** 6 de Marzo de 2026

