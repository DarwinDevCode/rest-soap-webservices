package org.uteq.restsoapwebservices.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.uteq.restsoapwebservices.entity.AuditLog;
import org.uteq.restsoapwebservices.repository.AuditLogRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired(required = false)
    private FirebaseAuditService firebaseAuditService;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Value("${firebase.db.url:}")
    private String firebaseDbUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registrarCreacion(String tipoEntidad, Long idEntidad, String descripcion, String datosAntes, String datosDespues) {
        AuditLog log = AuditLog.builder()
                .tipoEntidad(tipoEntidad)
                .idEntidad(idEntidad)
                .operacion("CREATE")
                .usuarioEmail(obtenerUsuarioActual())
                .timestamp(LocalDateTime.now())
                .datosAntes(datosAntes)
                .datosDespues(datosDespues)
                .descripcion(descripcion)
                .replicadoFirebase(false)
                .build();

        AuditLog guardado = auditLogRepository.save(log);
        if (firebaseAuditService != null) {
            firebaseAuditService.replicarLog(guardado);
        }
    }

    public void registrarActualizacion(String tipoEntidad, Long idEntidad, String descripcion, String datosAntes, String datosDespues) {
        AuditLog log = AuditLog.builder()
                .tipoEntidad(tipoEntidad)
                .idEntidad(idEntidad)
                .operacion("UPDATE")
                .usuarioEmail(obtenerUsuarioActual())
                .timestamp(LocalDateTime.now())
                .datosAntes(datosAntes)
                .datosDespues(datosDespues)
                .descripcion(descripcion)
                .replicadoFirebase(false)
                .build();

        AuditLog guardado = auditLogRepository.save(log);
        if (firebaseAuditService != null) {
            firebaseAuditService.replicarLog(guardado);
        }
    }

    public void registrarEliminacion(String tipoEntidad, Long idEntidad, String descripcion, String datosAntes, String datosDespues) {
        AuditLog log = AuditLog.builder()
                .tipoEntidad(tipoEntidad)
                .idEntidad(idEntidad)
                .operacion("DELETE")
                .usuarioEmail(obtenerUsuarioActual())
                .timestamp(LocalDateTime.now())
                .datosAntes(datosAntes)
                .datosDespues(datosDespues)
                .descripcion(descripcion)
                .replicadoFirebase(false)
                .build();

        AuditLog guardado = auditLogRepository.save(log);
        if (firebaseAuditService != null) {
            firebaseAuditService.replicarLog(guardado);
        }
    }

    @Async
    public void replicarLog(Object logData) {
        if (firebaseDbUrl == null || firebaseDbUrl.isBlank() || restTemplate == null) {
            return;
        }

        try {
            String path = "audits.json";
            String url = firebaseDbUrl.endsWith("/") ? firebaseDbUrl + path : firebaseDbUrl + "/" + path;

            restTemplate.postForObject(url, logData, String.class);
            log.info("Sincronización con Firebase exitosa.");
        } catch (Exception e) {
            log.error("Error al replicar a Firebase: {}", e.getMessage());
        }
    }

    private String obtenerUsuarioActual() {
        try {
            var authentication = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User principal) {
                return principal.getAttribute("email");
            }
        } catch (Exception e) {
            return "SISTEMA";
        }
        return "ANONIMO";
    }
}

