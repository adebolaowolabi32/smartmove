package com.interswitch.smartmoveserver.config;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.IswCoreService;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
@author adebola.owolabi
*/



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    IswCoreService coreService;

    @Autowired
    UserService userService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/webjars/**", "/css/**", "/js/**","/swf/**", "/img/**", "/assets/**", "/vendor/**",
                        "/keep-alive", "/retry", "/", "/index", "/login", "/signup", "/health").permitAll()
                //.requestMatchers(new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**")))
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout()
                .logoutUrl("/smlogout")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and().csrf().and()
                .oauth2Login()
                .defaultSuccessUrl("/dashboard")
                .authorizationEndpoint()
                .baseUri("/oauth/authorize")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .redirectionEndpoint()
                .baseUri("/callback")
                .and()
                .userInfoEndpoint()
                .userAuthoritiesMapper(this.userAuthoritiesMapper());
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public  AuthenticationManager authenticationManager(){
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                authentication.setAuthenticated(true);
                return authentication;
            }
        };
    };


    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority) {
                    //
                } else if (authority instanceof OAuth2UserAuthority) {

                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;
                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
                    if (userAttributes.containsKey("username")){
                        String username = userAttributes.get("username").toString();
                        User user = userService.findOrCreateByUsername(username);
                        mappedAuthorities.addAll(getGrantedAuthorities(coreService.getPermissions(user)));
                    }
                }
            });

            return mappedAuthorities;
        };
    }

    private Set<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }
}
