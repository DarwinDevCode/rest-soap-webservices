package org.uteq.restsoapwebservices.entity;

import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log", indexes = {
        @Index(name = "idx_tipo_entidad", columnList = "tipo_entidad"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Column(nullable = false, length = 50, name = "tipo_entidad")
    private String tipoEntidad;

    @Column(nullable = false, name = "id_entidad")
    private Long idEntidad;

    @Column(nullable = false, length = 20, name = "operacion")
    private String operacion;

    @Column(length = 255, name = "usuario_email")
    private String usuarioEmail;

    @Column(nullable = false, name = "timestamp")
    private LocalDateTime timestamp;

    @Column(columnDefinition = "TEXT", name = "datos_antes")
    private String datosAntes;

    @Column(columnDefinition = "TEXT", name = "datos_despues")
    private String datosDespues;

    @Column(length = 1000, name = "descripcion")
    private String descripcion;

    @Column(nullable = false, name = "replicado_firebase")
    @Builder.Default
    private Boolean replicadoFirebase = false;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
        if (replicadoFirebase == null) {
            replicadoFirebase = false;
        }
    }
}

