package com.github.tomaszgryczka.securenotes.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Jwt2AuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Jwt2AuthoritiesConverter converter;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new JwtAuthenticationToken(jwt, converter.convert(jwt), getPrincipalClaimName(jwt));
    }

    private static String getPrincipalClaimName(Jwt jwt) {
        return jwt.getClaim("preferred_username");
    }
}
