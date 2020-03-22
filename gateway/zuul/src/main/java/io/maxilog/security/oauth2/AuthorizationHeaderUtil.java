package io.maxilog.security.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class AuthorizationHeaderUtil {
    private final Logger log = LoggerFactory.getLogger(AuthorizationHeaderUtil.class);

    private final RestTemplateBuilder restTemplateBuilder;
    private final ClientRegistrationRepository clientRegistrationRepository;

    private OAuthIdpTokenResponseDTO currentToken;
    private ClientRegistration clientRegistration;
    private long expirationTime;
    private long minTokenValidity = 30L;

    public AuthorizationHeaderUtil(RestTemplateBuilder restTemplateBuilder, ClientRegistrationRepository clientRegistrationRepository) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    public Optional<String> getAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken accessToken = (JwtAuthenticationToken) authentication;
            String tokenValue = accessToken.getToken().getTokenValue();
            String authorizationHeaderValue = String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), tokenValue);
            return Optional.of(authorizationHeaderValue);
        }
        return Optional.empty();
    }

    public synchronized Optional<String> getSystemAuthorizationHeader() {
        if (clientRegistration == null) clientRegistration = clientRegistrationRepository.findByRegistrationId("oidc");
        if (currentToken == null) {
            grantToken();
        } else if (tokenExpired()) {
            refreshToken();
        }
        return Optional.of(currentToken.getAccessToken());
    }

    private void grantToken() {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, clientRegistration.getAuthorizationGrantType().getValue());
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
        RequestEntity requestEntity = getRequestEntity(formParameters);
        RestTemplate r = restTemplate(clientRegistration.getClientId(), clientRegistration.getClientSecret());
        ResponseEntity<OAuthIdpTokenResponseDTO> responseEntity = r.exchange(requestEntity, OAuthIdpTokenResponseDTO.class);
        long requestTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        synchronized (AuthorizationHeaderUtil.class) {
            currentToken = responseEntity.getBody();
            expirationTime = requestTime + currentToken.getExpiresIn();
        }
    }

    private void refreshToken() {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.REFRESH_TOKEN.getValue());
        formParameters.add(OAuth2ParameterNames.REFRESH_TOKEN, currentToken.getRefreshToken());
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        RequestEntity requestEntity = getRequestEntity(formParameters);
        try {
            RestTemplate r = restTemplate(clientRegistration.getClientId(), clientRegistration.getClientSecret());
            ResponseEntity<OAuthIdpTokenResponseDTO> responseEntity = r.exchange(requestEntity, OAuthIdpTokenResponseDTO.class);

            long requestTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
            currentToken = responseEntity.getBody();
            expirationTime = requestTime + currentToken.getExpiresIn();
        } catch (OAuth2AuthorizationException e) {
            grantToken();
        }
    }

    private RequestEntity<MultiValueMap<String, String>> getRequestEntity(MultiValueMap<String, String> formParameters) {
        return RequestEntity
                .post(URI.create(clientRegistration.getProviderDetails().getTokenUri()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formParameters);
    }

    private boolean tokenExpired() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() + minTokenValidity >= expirationTime;
    }


    private RestTemplate restTemplate(String clientId, String clientSecret) {
        return restTemplateBuilder
                .additionalMessageConverters(
                        new FormHttpMessageConverter(),
                        new OAuth2AccessTokenResponseHttpMessageConverter())
                .errorHandler(new OAuth2ErrorResponseErrorHandler())
                .basicAuthentication(clientId, clientSecret)
                .build();
    }

}
