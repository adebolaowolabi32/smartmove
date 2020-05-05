package com.interswitch.smartmoveserver.service;

import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class WebViewService {
    public boolean isPermittedToView(Principal principal, Long owner){
        return true;
    }
}
