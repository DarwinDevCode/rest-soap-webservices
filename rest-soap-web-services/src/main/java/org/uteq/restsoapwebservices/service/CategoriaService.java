package org.uteq.restsoapwebservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.restsoapwebservices.dto.CategoriaDTO;
import org.uteq.restsoapwebservices.entity.Categoria;
import org.uteq.restsoapwebservices.repository.CategoriaRepository;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AuditService auditService;

    @Cacheable(value = "categorias")
    public Page<Categoria> obtenerTodas(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    @Cacheable(value = "categoria", key = "#id")
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    @Cacheable(value = "categoriaPorNombre", key = "#nombre")
    public Page<Categoria> buscarPorNombre(String nombre, Pageable pageable) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @CacheEvict(value = "categorias", allEntries = true)
    public Categoria crear(CategoriaDTO dto) {
        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();

        Categoria guardada = categoriaRepository.save(categoria);
        auditService.registrarCreacion("CATEGORIA", guardada.getId(), "Categoría creada: " + guardada.getNombre(), null, null);

        return guardada;
    }

    @CacheEvict(value = {"categorias", "categoria"}, allEntries = true)
    public Categoria actualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = obtenerPorId(id);
        String datosAntes = categoria.toString();

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria actualizada = categoriaRepository.save(categoria);
        auditService.registrarActualizacion("CATEGORIA", id, "Categoría actualizada", datosAntes, actualizada.toString());

        return actualizada;
    }

    @CacheEvict(value = {"categorias", "categoria"}, allEntries = true)
    public void eliminar(Long id) {
        Categoria categoria = obtenerPorId(id);
        categoriaRepository.deleteById(id);
        auditService.registrarEliminacion("CATEGORIA", id, "Categoría eliminada: " + categoria.getNombre(), categoria.toString(), null);
    }

    public CategoriaDTO toDTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .fechaCreacion(categoria.getFechaCreacion())
                .fechaActualizacion(categoria.getFechaActualizacion())
                .build();
    }
}

