package org.uteq.restsoapwebservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.restsoapwebservices.entity.Usuario;
import org.uteq.restsoapwebservices.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario crearOActualizar(String email, String nombre, String foto, String proveedor) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setNombre(nombre);
            usuario.setFoto(foto);
            usuario.setFechaActualizacion(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        } else {
            Usuario nuevoUsuario = Usuario.builder()
                    .email(email)
                    .nombre(nombre)
                    .foto(foto)
                    .oauth2Provider(proveedor)
                    .build();
            return usuarioRepository.save(nuevoUsuario);
        }
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}

