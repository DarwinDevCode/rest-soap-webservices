package org.uteq.restsoapwebservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uteq.restsoapwebservices.dto.CategoriaDTO;
import org.uteq.restsoapwebservices.service.CategoriaService;

@RestController
@RequestMapping("/api/catalogos/categorias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<Page<CategoriaDTO>> obtenerTodas(Pageable pageable) {
        var categorias = categoriaService.obtenerTodas(pageable);
        var dtos = categorias.map(categoriaService::toDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable Long id) {
        var categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(categoriaService.toDTO(categoria));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<CategoriaDTO>> buscar(
            @RequestParam(required = false) String nombre,
            Pageable pageable) {
        if (nombre == null || nombre.isBlank()) {
            return obtenerTodas(pageable);
        }
        var categorias = categoriaService.buscarPorNombre(nombre, pageable);
        var dtos = categorias.map(categoriaService::toDTO);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaDTO dto) {
        var categoria = categoriaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.toDTO(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CategoriaDTO dto) {
        var categoria = categoriaService.actualizar(id, dto);
        return ResponseEntity.ok(categoriaService.toDTO(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

