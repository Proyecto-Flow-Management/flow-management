package com.proyecto.flowmanagement.security;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SecurePasswordGenerator {
    public SecurePasswordGenerator() {
    }

    public String getEncryptedPassword(String rawPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        return encodedPassword;
    }
}
