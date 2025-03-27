package vn.hoangdung.restAPI.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;


@Service
public class SecurityUtil {
    
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512; 

    @Value("${hoangdung.jwt.base64-secret}") 
    private String jwtKey; 

    @Value("${hoangdung.jwt.token-validity-in-seconds}")
    private String jwtKeyExpiration;

    public void createToken(Authentication authentication) {

    }

}
