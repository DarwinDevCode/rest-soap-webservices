package org.uteq.restsoapwebservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.restsoapwebservices.dto.ProductoDTO;
import org.uteq.restsoapwebservices.entity.Producto;
import org.uteq.restsoapwebservices.repository.ProductoRepository;

@Service
@Transactional
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AuditService auditService;

    @Cacheable(value = "productos")
    public Page<Producto> obtenerTodos(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Cacheable(value = "producto", key = "#id")
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Cacheable(value = "productoPorNombre", key = "#nombre")
    public Page<Producto> buscarPorNombre(String nombre, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Cacheable(value = "productosPorCategoria", key = "#categoriaId")
    public Page<Producto> obtenerPorCategoria(Long categoriaId, Pageable pageable) {
        return productoRepository.findByCategoriaId(categoriaId, pageable);
    }

    @Cacheable(value = "productosConStock")
    public Page<Producto> obtenerConStock(Pageable pageable) {
        return productoRepository.findByStockGreaterThan(0, pageable);
    }

    @CacheEvict(value = {"productos", "productosConStock"}, allEntries = true)
    public Producto crear(ProductoDTO dto) {
        var categoria = categoriaService.obtenerPorId(dto.getCategoriaId());

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .categoria(categoria)
                .build();

        Producto guardado = productoRepository.save(producto);
        auditService.registrarCreacion("PRODUCTO", guardado.getId(), "Producto creado: " + guardado.getNombre(), null, null);

        return guardado;
    }

    @CacheEvict(value = {"productos", "producto", "productosConStock", "productoPorNombre"}, allEntries = true)
    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto producto = obtenerPorId(id);
        String datosAntes = producto.toString();

        if (dto.getCategoriaId() != null) {
            var categoria = categoriaService.obtenerPorId(dto.getCategoriaId());
            producto.setCategoria(categoria);
        }

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        Producto actualizado = productoRepository.save(producto);
        auditService.registrarActualizacion("PRODUCTO", id, "Producto actualizado", datosAntes, actualizado.toString());

        return actualizado;
    }

    @CacheEvict(value = {"productos", "producto", "productosConStock", "productoPorNombre"}, allEntries = true)
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.deleteById(id);
        auditService.registrarEliminacion("PRODUCTO", id, "Producto eliminado: " + producto.getNombre(), producto.toString(), null);
    }

    public ProductoDTO toDTO(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .categoriaId(producto.getCategoria().getId())
                .categoriaNombre(producto.getCategoria().getNombre())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }
}

