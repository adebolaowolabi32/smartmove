package com.interswitch.smartmoveserver.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/*
 * Created by adebola.owolabi on 10/28/2020
 */
@Component
public class JwtUtil implements Serializable {

    public static final String TOKEN_PARAM = "auth_token";
    @Value("${spring.security.oauth2.resourceserver.jwt.public-key}")
    private String SIGNING_KEY;

    public static String getUsername(Authentication authToken) {
        String userName = "";
        Map<String, Object> attributes;
        if (authToken != null) {
            if (authToken instanceof OAuth2AuthenticationToken) {
                attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
            } else if (authToken instanceof JwtAuthenticationToken) {
                attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
            } else return "";
            Object user = attributes.get("user_name");
            if (user != null) {
                userName = user.toString().trim();
            }
        }
        return userName;
    }

    public String getUsernameFromToken(String token) throws GeneralSecurityException {
        Map<String, Object> claims = getClaimFromToken(token);
        return claims.get("user_name").toString();
    }

    public Date getExpirationDateFromToken(String token) throws GeneralSecurityException, ParseException {
        Map<String, Object> claims = getClaimFromToken(token);
        return Date.from(Instant.parse(claims.get("exp").toString()));
    }

    private Map<String, Object> getClaimFromToken(String token) throws GeneralSecurityException {
        byte[] spec;
        spec = Base64.getDecoder().decode(SIGNING_KEY);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(spec));
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(pubKey).build();
        OAuth2TokenValidator<Jwt> withExpiry = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> validators = new DelegatingOAuth2TokenValidator<>(withExpiry);
        jwtDecoder.setJwtValidator(validators);
        return jwtDecoder.decode(token).getClaims();
    }

    private Boolean isTokenExpired(String token) throws GeneralSecurityException, ParseException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws GeneralSecurityException, ParseException {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }
}