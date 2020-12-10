package com.interswitch.smartmoveserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author adebola.owolabi
 */
@Configuration
@Order(1)
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.oauth2.resourceserver.jwt.public-key}")
    private String publicKey;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS)
                .antMatchers("/error", "/health","/signup");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
                .authorizeRequests().antMatchers("/api/**")
                .authenticated()
                .and().csrf().disable()
                .oauth2ResourceServer()
                .jwt();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws GeneralSecurityException {
        byte[] spec;
        spec = Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(spec));
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(pubKey).build();
        OAuth2TokenValidator<Jwt> withExpiry = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> validators = new DelegatingOAuth2TokenValidator<>(withExpiry);
        jwtDecoder.setJwtValidator(validators);

        return jwtDecoder;
    }
}
