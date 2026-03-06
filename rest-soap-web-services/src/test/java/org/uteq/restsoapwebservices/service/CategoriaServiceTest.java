package org.uteq.restsoapwebservices.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.restsoapwebservices.dto.CategoriaDTO;
import org.uteq.restsoapwebservices.entity.Categoria;
import org.uteq.restsoapwebservices.repository.CategoriaRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Tests de Categoría")
class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoriaDTO = CategoriaDTO.builder()
                .nombre("Electrónica")
                .descripcion("Productos electrónicos")
                .build();
    }

    @Test
    @DisplayName("Debe crear una categoría exitosamente")
    void testCrearCategoria() {
        Categoria categoria = categoriaService.crear(categoriaDTO);

        assertNotNull(categoria.getId());
        assertEquals("Electrónica", categoria.getNombre());
        assertEquals("Productos electrónicos", categoria.getDescripcion());
        assertNotNull(categoria.getFechaCreacion());
    }

    @Test
    @DisplayName("Debe obtener una categoría por ID")
    void testObtenerPorId() {
        Categoria creada = categoriaService.crear(categoriaDTO);
        Categoria obtenida = categoriaService.obtenerPorId(creada.getId());

        assertNotNull(obtenida);
        assertEquals(creada.getId(), obtenida.getId());
        assertEquals(creada.getNombre(), obtenida.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción al obtener categoría inexistente")
    void testObtenerPorIdNoExistente() {
        assertThrows(RuntimeException.class, () -> categoriaService.obtenerPorId(9999L));
    }

    @Test
    @DisplayName("Debe actualizar una categoría")
    void testActualizarCategoria() {
        Categoria creada = categoriaService.crear(categoriaDTO);

        CategoriaDTO actualizado = CategoriaDTO.builder()
                .nombre("Electrónica Actualizado")
                .descripcion("Descripción actualizada")
                .build();

        Categoria resultado = categoriaService.actualizar(creada.getId(), actualizado);

        assertEquals("Electrónica Actualizado", resultado.getNombre());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
    }

    @Test
    @DisplayName("Debe eliminar una categoría")
    void testEliminarCategoria() {
        Categoria creada = categoriaService.crear(categoriaDTO);
        Long id = creada.getId();

        categoriaService.eliminar(id);

        assertThrows(RuntimeException.class, () -> categoriaService.obtenerPorId(id));
    }

    @Test
    @DisplayName("Debe buscar categorías por nombre")
    void testBuscarPorNombre() {
        categoriaService.crear(categoriaDTO);

        var resultado = categoriaService.buscarPorNombre("Electrónica", PageRequest.of(0, 10));

        assertTrue(resultado.hasContent());
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    @DisplayName("Debe convertir entidad a DTO correctamente")
    void testToDTO() {
        Categoria categoria = categoriaService.crear(categoriaDTO);
        CategoriaDTO dto = categoriaService.toDTO(categoria);

        assertNotNull(dto);
        assertEquals(categoria.getId(), dto.getId());
        assertEquals(categoria.getNombre(), dto.getNombre());
        assertEquals(categoria.getDescripcion(), dto.getDescripcion());
    }
}

