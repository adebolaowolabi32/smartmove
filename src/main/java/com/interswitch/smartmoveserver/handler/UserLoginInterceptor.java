package com.interswitch.smartmoveserver.handler;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/*
 * Created by adebola.owolabi on 12/7/2020
 */
@Component
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

    private static List<String> excludedEndpoints = Arrays.asList("/", "/auth/login","/api/.*", "/signup/?", "/smlogout/?", "/index/?", "/assets/.*", "/css/.*", "/fonts/.*", "/images/.*", "/img/.*", "/js/.*", "/sass/.*", "/scss/.*", "/vendor/.*");

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    public static boolean match(String path) {
        return excludedEndpoints.stream().anyMatch(p -> path.matches(p));
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (match(path))
            return true;
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            if (user != null) {
                if (user.getRole() == null) {
                    response.sendRedirect("/signup");
                    return false;
                } else if (!user.isEnabled()) {
                    response.sendRedirect("/smlogout");
                    return false;
                } else return true;
            }
        }
        return false;
    }
}