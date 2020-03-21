package io.maxilog.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.maxilog.security.oauth2.AuthorizationHeaderUtil;

import java.util.Optional;

public class TokenRelayRequestInterceptor implements RequestInterceptor {

    public static final String AUTHORIZATION = "Authorization";

    private final AuthorizationHeaderUtil authorizationHeaderUtil;

    public TokenRelayRequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) {
        super();
        this.authorizationHeaderUtil = authorizationHeaderUtil;
    }

    @Override
    public void apply(RequestTemplate template) {
        if(template.headers().get(AUTHORIZATION) == null){
            Optional<String> authorizationHeader = authorizationHeaderUtil.getAuthorizationHeader();
            authorizationHeader.ifPresent(s -> template.header(AUTHORIZATION, s));
        }
    }
}
