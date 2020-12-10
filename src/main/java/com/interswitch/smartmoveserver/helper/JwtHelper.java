package com.interswitch.smartmoveserver.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author adebola.owolabi
 */
@Component
public class JwtHelper {

    public String getClaim(String key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt)) {
            return null;
        }
        Jwt jwt = (Jwt) principal;
        return jwt.getClaimAsString(key);
    }

/*    public static JWT decodeBearerToken(HttpHeaders headers) {
        String accesstoken = "";
        JWT jwtToken = null;
        if (headers != null && headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            List<String> accesstokens = headers.get(HttpHeaders.AUTHORIZATION);
            if (accesstokens != null && !accesstokens.isEmpty()) {
                accesstoken = accesstokens.get(0);
            }
        }
        if (accesstoken != null && accesstoken.contains("Bearer ")) {
            accesstoken = accesstoken.replaceFirst("Bearer ", "");
            if (!accesstoken.isEmpty()) {
                try {
                    jwtToken = JWTParser.parse(accesstoken);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return jwtToken;
    }


    public static String getClaimFromBearerToken(JWT jwtToken, String claim) {
        String claimAsString = "";
        if (jwtToken != null) {
            try {
                Object claimObject = jwtToken.getJWTClaimsSet().getClaim(claim);
                if (claimObject != null) {
                    claimAsString = claimObject.toString().trim();
                    if (!claim.equals("client_id")) claimAsString = claimAsString.toLowerCase();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return claimAsString;
    }

    public static JWTClaimsSet getClaimsFromBearerToken(JWT jwtToken) {
        JWTClaimsSet claimsObject = null;
        if (jwtToken != null) {
            try {
                claimsObject = jwtToken.getJWTClaimsSet();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return claimsObject;
    }*/

    public static boolean isInterswitchEmail(String email) {
        return email.endsWith("@interswitchgroup.com") ||
                email.endsWith("@interswitch.com") ||
                email.endsWith("@interswitchng.com") ||
                email.endsWith("@yahoo.com");
    }

    public static boolean match(String path, Set<String> paths) {
        return paths.stream().anyMatch(p -> path.matches(p));
    }

}