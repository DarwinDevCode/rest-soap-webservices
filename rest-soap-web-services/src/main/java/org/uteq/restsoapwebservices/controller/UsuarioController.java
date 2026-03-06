package org.uteq.restsoapwebservices.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uteq.restsoapwebservices.entity.Usuario;
import org.uteq.restsoapwebservices.service.UsuarioService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> obtenerPerfil(
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            return ResponseEntity.ok(Map.of("autenticado", false));
        }

        Map<String, Object> attributes = principal.getAttributes();
        String email = principal.getAttribute("email");
        String nombre = principal.getAttribute("name");
        String foto = principal.getAttribute("picture");
        String proveedor = "GOOGLE"; // Asunción inicial

        if (attributes.containsKey("login")) {
            proveedor = "GITHUB";
            foto = (String) attributes.get("avatar_url");

            if (email == null) {
                email = attributes.get("login") + "@github.com";
            }
        } else if (attributes.containsKey("sub")) {
            proveedor = "GOOGLE";
        }

        Usuario usuario = usuarioService.crearOActualizar(email, nombre, foto, proveedor);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("autenticado", true);
        respuesta.put("id", usuario.getId());
        respuesta.put("email", usuario.getEmail());
        respuesta.put("nombre", usuario.getNombre());
        respuesta.put("foto", usuario.getFoto());
        respuesta.put("proveedor", usuario.getOauth2Provider());
        respuesta.put("fechaCreacion", usuario.getFechaCreacion());

        return ResponseEntity.ok(respuesta);
    }
}

