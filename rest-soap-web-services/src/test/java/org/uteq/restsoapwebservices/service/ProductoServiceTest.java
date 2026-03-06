package org.uteq.restsoapwebservices.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.restsoapwebservices.dto.CategoriaDTO;
import org.uteq.restsoapwebservices.dto.ProductoDTO;
import org.uteq.restsoapwebservices.entity.Producto;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Tests de Producto")
class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    private ProductoDTO productoDTO;
    private Long categoriaId;

    @BeforeEach
    void setUp() {
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .nombre("Test Categoría")
                .descripcion("Categoría para pruebas")
                .build();
        var categoria = categoriaService.crear(categoriaDTO);
        categoriaId = categoria.getId();

        productoDTO = ProductoDTO.builder()
                .nombre("Laptop")
                .descripcion("Laptop de prueba")
                .precio(new BigDecimal("999.99"))
                .stock(10)
                .categoriaId(categoriaId)
                .build();
    }

    @Test
    @DisplayName("Debe crear un producto exitosamente")
    void testCrearProducto() {
        Producto producto = productoService.crear(productoDTO);

        assertNotNull(producto.getId());
        assertEquals("Laptop", producto.getNombre());
        assertEquals(new BigDecimal("999.99"), producto.getPrecio());
        assertEquals(10, producto.getStock());
        assertEquals(categoriaId, producto.getCategoria().getId());
    }

    @Test
    @DisplayName("Debe obtener un producto por ID")
    void testObtenerPorId() {
        Producto creado = productoService.crear(productoDTO);
        Producto obtenido = productoService.obtenerPorId(creado.getId());

        assertNotNull(obtenido);
        assertEquals(creado.getId(), obtenido.getId());
        assertEquals(creado.getNombre(), obtenido.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción al obtener producto inexistente")
    void testObtenerPorIdNoExistente() {
        assertThrows(RuntimeException.class, () -> productoService.obtenerPorId(9999L));
    }

    @Test
    @DisplayName("Debe actualizar un producto")
    void testActualizarProducto() {
        Producto creado = productoService.crear(productoDTO);

        ProductoDTO actualizado = ProductoDTO.builder()
                .nombre("Laptop Pro")
                .descripcion("Laptop actualizada")
                .precio(new BigDecimal("1299.99"))
                .stock(5)
                .categoriaId(categoriaId)
                .build();

        Producto resultado = productoService.actualizar(creado.getId(), actualizado);

        assertEquals("Laptop Pro", resultado.getNombre());
        assertEquals(new BigDecimal("1299.99"), resultado.getPrecio());
        assertEquals(5, resultado.getStock());
    }

    @Test
    @DisplayName("Debe eliminar un producto")
    void testEliminarProducto() {
        Producto creado = productoService.crear(productoDTO);
        Long id = creado.getId();

        productoService.eliminar(id);

        assertThrows(RuntimeException.class, () -> productoService.obtenerPorId(id));
    }

    @Test
    @DisplayName("Debe buscar productos por nombre")
    void testBuscarPorNombre() {
        productoService.crear(productoDTO);

        var resultado = productoService.buscarPorNombre("Laptop", PageRequest.of(0, 10));

        assertTrue(resultado.hasContent());
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    @DisplayName("Debe obtener productos por categoría")
    void testObtenerPorCategoria() {
        productoService.crear(productoDTO);

        var resultado = productoService.obtenerPorCategoria(categoriaId, PageRequest.of(0, 10));

        assertTrue(resultado.hasContent());
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    @DisplayName("Debe obtener productos con stock disponible")
    void testObtenerConStock() {
        productoService.crear(productoDTO);

        var resultado = productoService.obtenerConStock(PageRequest.of(0, 10));

        assertTrue(resultado.hasContent());
    }

    @Test
    @DisplayName("Debe convertir entidad a DTO correctamente")
    void testToDTO() {
        Producto producto = productoService.crear(productoDTO);
        ProductoDTO dto = productoService.toDTO(producto);

        assertNotNull(dto);
        assertEquals(producto.getId(), dto.getId());
        assertEquals(producto.getNombre(), dto.getNombre());
        assertEquals(producto.getPrecio(), dto.getPrecio());
        assertEquals(producto.getStock(), dto.getStock());
    }
}

