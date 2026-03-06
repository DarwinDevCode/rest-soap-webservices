package org.uteq.restsoapwebservices.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.restsoapwebservices.dto.CategoriaDTO;
import org.uteq.restsoapwebservices.dto.ProductoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Tests de Carga y Concurrencia")
class ConcurrencyLoadTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    private Long categoriaId;

    @BeforeEach
    void setUp() {
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .nombre("Categoría de Prueba")
                .descripcion("Para tests de carga")
                .build();
        categoriaId = categoriaService.crear(categoriaDTO).getId();
    }

    @RepeatedTest(100)
    @DisplayName("Debe manejar 100 operaciones de lectura de catálogo sin fallar")
    void testOperacionesLecturaEnMasa() {
        var resultado = productoService.obtenerTodos(PageRequest.of(0, 10));
        assertNotNull(resultado);
    }

    @ParameterizedTest(name = "Paginación con tamaño de página: {0}")
    @ValueSource(ints = {5, 10, 20, 50})
    @DisplayName("Debe soportar diferentes tamaños de página")
    @Transactional
    void testPaginacionConDiferentesTamanos(int tamano) {
        for (int i = 0; i < tamano * 2; i++) {
            ProductoDTO dto = ProductoDTO.builder()
                    .nombre("Producto " + i)
                    .descripcion("Descripción " + i)
                    .precio(BigDecimal.valueOf(100 + i))
                    .stock(10)
                    .categoriaId(categoriaId)
                    .build();
            productoService.crear(dto);
        }

        var resultado = productoService.obtenerTodos(PageRequest.of(0, tamano));
        assertTrue(resultado.hasContent());
        assertEquals(tamano, resultado.getContent().size());
    }

    @Test
    @DisplayName("Medir tiempo de respuesta para 50 creaciones de productos")
    @Transactional
    void testTiempoRespuestaCreaciones() {
        LocalDateTime inicio = LocalDateTime.now();

        for (int i = 0; i < 50; i++) {
            ProductoDTO dto = ProductoDTO.builder()
                    .nombre("Producto Carga " + i)
                    .descripcion("Test de carga " + i)
                    .precio(BigDecimal.valueOf(50 + i * 10))
                    .stock(100)
                    .categoriaId(categoriaId)
                    .build();
            productoService.crear(dto);
        }

        LocalDateTime fin = LocalDateTime.now();
        long duracionMs = ChronoUnit.MILLIS.between(inicio, fin);

        System.out.println("Tiempo para 50 creaciones: " + duracionMs + "ms");
        System.out.println("Promedio por creación: " + (duracionMs / 50.0) + "ms");

        assertTrue(duracionMs < 5000, "Tiempo de creación muy alto: " + duracionMs + "ms");
    }

    @Test
    @DisplayName("Medir tiempo de respuesta para búsquedas")
    @Transactional
    void testTiempoRespuestaBusquedas() {
        for (int i = 0; i < 100; i++) {
            ProductoDTO dto = ProductoDTO.builder()
                    .nombre("SearchTest " + i)
                    .descripcion("Producto " + i)
                    .precio(BigDecimal.valueOf(100))
                    .stock(10)
                    .categoriaId(categoriaId)
                    .build();
            productoService.crear(dto);
        }

        LocalDateTime inicio = LocalDateTime.now();

        for (int i = 0; i < 50; i++) {
            productoService.buscarPorNombre("SearchTest", PageRequest.of(0, 10));
        }

        LocalDateTime fin = LocalDateTime.now();
        long duracionMs = ChronoUnit.MILLIS.between(inicio, fin);

        System.out.println("Tiempo para 50 búsquedas: " + duracionMs + "ms");
        System.out.println("Promedio por búsqueda: " + (duracionMs / 50.0) + "ms");

        assertTrue(duracionMs < 2000, "Tiempo de búsqueda muy alto: " + duracionMs + "ms");
    }

    @RepeatedTest(10)
    @DisplayName("Debe mantener integridad bajo operaciones CRUD simultáneas")
    @Transactional
    void testIntegridadCRUDSimultaneo() {
        ProductoDTO dto = ProductoDTO.builder()
                .nombre("Producto Simultáneo")
                .descripcion("Test CRUD")
                .precio(BigDecimal.valueOf(999))
                .stock(50)
                .categoriaId(categoriaId)
                .build();
        var producto = productoService.crear(dto);

        var obtenido = productoService.obtenerPorId(producto.getId());
        assertNotNull(obtenido);

        dto.setStock(25);
        var actualizado = productoService.actualizar(producto.getId(), dto);
        assertEquals(25, actualizado.getStock());

        obtenido = productoService.obtenerPorId(producto.getId());
        assertEquals(25, obtenido.getStock());

        productoService.eliminar(producto.getId());
        assertThrows(RuntimeException.class, () -> productoService.obtenerPorId(producto.getId()));
    }
}

