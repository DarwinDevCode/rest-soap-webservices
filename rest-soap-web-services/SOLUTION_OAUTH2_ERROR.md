# ✅ SOLUCIÓN - Error de OAuth2 ClientRegistrationRepository

## Problema Identificado

```
Parameter 0 of method setFilterChains in org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration required a bean of type 'org.springframework.security.oauth2.client.registration.ClientRegistrationRepository' that could not be found.
```

**Causa:** Spring Security OAuth2 necesita un bean `ClientRegistrationRepository` que registre los clientes OAuth2 (Google y GitHub), pero no estaba configurado en `SecurityConfig`.

---

## Solución Aplicada

### Actualizar `SecurityConfig.java`

Se agregaron tres componentes clave:

#### 1. Bean `ClientRegistrationRepository`
```java
@Bean
public ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(
            googleClientRegistration(),
            githubClientRegistration()
    );
}
```
- Registra Google y GitHub como proveedores OAuth2
- Almacenado en memoria (ideal para desarrollo)

#### 2. Registración de Google OAuth2
```java
private ClientRegistration googleClientRegistration() {
    return ClientRegistration.withRegistrationId("google")
            .clientId("876431555574-tnv9v7q1bljptlo9c9rkvjjm1tabr6j1.apps.googleusercontent.com")
            .clientSecret("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")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("openid", "profile", "email")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://www.googleapis.com/oauth2/v4/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v1/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .clientName("Google")
            .build();
}
```

#### 3. Registración de GitHub OAuth2
```java
private ClientRegistration githubClientRegistration() {
    return ClientRegistration.withRegistrationId("github")
            .clientId("Ov23liFqHuOkgQeJBevy")
            .clientSecret("cb9a3adad8d1af81c441ffdd7df610b2b961a352")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("read:user", "user:email")
            .authorizationUri("https://github.com/login/oauth/authorize")
            .tokenUri("https://github.com/login/oauth/access_token")
            .userInfoUri("https://api.github.com/user")
            .userNameAttributeName("id")
            .clientName("GitHub")
            .build();
}
```

---

## Resultado

✅ **BUILD SUCCESS**

La aplicación ahora tiene:
- Bean `ClientRegistrationRepository` correctamente configurado
- Clientes OAuth2 registrados (Google y GitHub)
- SecurityFilterChain que utiliza los clientes registrados
- CORS completamente configurado

---

## Próximo Paso

Intenta ejecutar el servidor nuevamente:

```bash
mvn spring-boot:run
```

Debería iniciar sin errores de OAuth2.

---

## URLs de Login

Una vez el servidor está corriendo:

- **Google Login:** `http://localhost:8080/oauth2/authorization/google`
- **GitHub Login:** `http://localhost:8080/oauth2/authorization/github`

Después de autenticarse, será redireccionado a `/api/usuarios/perfil` donde se guarda el usuario en la BD.

---

**La aplicación está lista para OAuth2.** ✅

