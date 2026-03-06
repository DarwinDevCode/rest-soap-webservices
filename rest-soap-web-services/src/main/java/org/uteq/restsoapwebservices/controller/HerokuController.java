package org.uteq.restsoapwebservices.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/heroku")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HerokuController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${heroku.app-name:}")
    private String herokuAppName;

    @Value("${heroku.api-key:}")
    private String herokuApiKey;

    private static final String HEROKU_API_BASE = "https://api.heroku.com";

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> obtenerInfo() {
        Map<String, Object> respuesta = new HashMap<>();

        if (herokuAppName == null || herokuAppName.isBlank() ||
            herokuApiKey == null || herokuApiKey.isBlank()) {
            respuesta.put("configurado", false);
            respuesta.put("mensaje", "Heroku no está configurado en application.properties");
            return ResponseEntity.ok(respuesta);
        }

        try {
            respuesta.put("configurado", true);
            respuesta.put("appName", herokuAppName);

            // Consultar información del app
            String appInfoUrl = HEROKU_API_BASE + "/apps/" + herokuAppName;
            var appInfo = consultarHerokuAPI(appInfoUrl);
            respuesta.put("appInfo", appInfo);

            // Consultar dynos
            String dynosUrl = HEROKU_API_BASE + "/apps/" + herokuAppName + "/dynos";
            var dynos = consultarHerokuAPI(dynosUrl);
            respuesta.put("dynos", dynos);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", e.getMessage());
            return ResponseEntity.internalServerError().body(respuesta);
        }
    }

    private Object consultarHerokuAPI(String url) {
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + herokuApiKey);
            headers.set("Accept", "application/vnd.heroku+json; version=3");

            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            var response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Object.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error en Heroku API: {}", e.getMessage());
            return java.util.Map.of("error", true, "mensaje", e.getMessage());
        }
    }

    private Map<String, String> crearHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + herokuApiKey);
        headers.put("Accept", "application/vnd.heroku+json; version=3");
        return headers;
    }
}

