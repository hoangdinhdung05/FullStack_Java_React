package vn.hoangdung.restAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.domain.dto.LoginDTO;
import vn.hoangdung.restAPI.domain.dto.ResLoginDTO;
import vn.hoangdung.restAPI.service.UserService;
import vn.hoangdung.restAPI.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        //Nạp user và pass vào security
        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //Xác thực người dùng loadUserByUsername()
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); 

        //Create token
        String access_token = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if(currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName()
            );
            res.setUser(userLogin);
        }
        //set token
        res.setAccessToken(access_token);
        return ResponseEntity.ok().body(res);

    }

}
