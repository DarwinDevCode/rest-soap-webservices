package org.uteq.restsoapwebservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uteq.restsoapwebservices.dto.ProductoDTO;
import org.uteq.restsoapwebservices.service.ProductoService;

@RestController
@RequestMapping("/api/catalogos/productos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<Page<ProductoDTO>> obtenerTodos(Pageable pageable) {
        var productos = productoService.obtenerTodos(pageable);
        var dtos = productos.map(productoService::toDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        var producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(productoService.toDTO(producto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoDTO>> buscar(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {
        if (nombre == null || nombre.isBlank()) {
            return obtenerTodos(pageable);
        }
        var productos = productoService.buscarPorNombre(nombre, pageable);
        var dtos = productos.map(productoService::toDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Page<ProductoDTO>> obtenerPorCategoria(
            @PathVariable Long categoriaId,
            Pageable pageable) {
        var productos = productoService.obtenerPorCategoria(categoriaId, pageable);
        var dtos = productos.map(productoService::toDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<Page<ProductoDTO>> obtenerConStock(Pageable pageable) {
        var productos = productoService.obtenerConStock(pageable);
        var dtos = productos.map(productoService::toDTO);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        var producto = productoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.toDTO(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoDTO dto) {
        var producto = productoService.actualizar(id, dto);
        return ResponseEntity.ok(productoService.toDTO(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

