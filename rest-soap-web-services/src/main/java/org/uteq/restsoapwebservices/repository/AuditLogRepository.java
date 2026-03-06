package org.uteq.restsoapwebservices.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uteq.restsoapwebservices.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByTipoEntidad(String tipoEntidad, Pageable pageable);
    Page<AuditLog> findByUsuarioEmail(String usuarioEmail, Pageable pageable);
    Page<AuditLog> findByReplicadoFirebaseFalse(Pageable pageable);
}

