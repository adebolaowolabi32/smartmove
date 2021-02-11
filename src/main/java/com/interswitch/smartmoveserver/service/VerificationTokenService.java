package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.VerificationToken;
import com.interswitch.smartmoveserver.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    public VerificationToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        return verificationTokenRepository.save(new VerificationToken(user, token));
    }

    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

}
