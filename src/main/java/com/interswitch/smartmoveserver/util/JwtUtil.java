package com.interswitch.smartmoveserver.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;

/*
 * Created by adebola.owolabi on 10/28/2020
 */
public class JwtUtil {
    public static String getUsername(Authentication authToken) {
        String userName = "";
        Map<String, Object> attributes;
        if (authToken != null) {
            if (authToken instanceof OAuth2AuthenticationToken) {
                attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
            }
            else if (authToken instanceof JwtAuthenticationToken) {
                attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
            }
            else return "";
            Object user = attributes.get("user_name");
            if (user != null) {
                userName = user.toString().trim();
            }
        }
        return userName;
    }
}