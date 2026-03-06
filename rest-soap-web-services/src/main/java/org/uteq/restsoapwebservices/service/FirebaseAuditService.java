package org.uteq.restsoapwebservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FirebaseAuditService {

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Value("${firebase.db.url:}")
    private String firebaseDbUrl;

    @Async
    public void replicarLog(Object logData) {
        if (firebaseDbUrl == null || firebaseDbUrl.isBlank() || restTemplate == null) {
            log.warn("Firebase no configurado o RestTemplate no disponible.");
            return;
        }

        try {
            String path = "audits.json";
            String url = firebaseDbUrl.endsWith("/") ? firebaseDbUrl + path : firebaseDbUrl + "/" + path;
            log.info("Enviando auditoria a Firebase: {}", url);
            String response = restTemplate.postForObject(url, logData, String.class);
            log.info("Respuesta de Firebase: {}", response);

        } catch (Exception e) {
            log.error("Error al replicar a Firebase: {}", e.getMessage());
        }
    }
}