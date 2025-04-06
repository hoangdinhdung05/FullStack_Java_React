package vn.hoangdung.restAPI.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.domain.dto.LoginDTO;
import vn.hoangdung.restAPI.domain.dto.ResLoginDTO;
import vn.hoangdung.restAPI.service.UserService;
import vn.hoangdung.restAPI.util.SecurityUtil;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${hoangdung.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }
    
    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        //Nạp user và pass vào security
        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        //Xác thực người dùng loadUserByUsername()
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); 

        //Lưu vào securityContextHolder các thông tin user
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

        String access_token = this.securityUtil.createAccessToken(authentication, res.getUser());

        //set token
        res.setAccessToken(access_token);
        //create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);
        //update User
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());
        //set cookie
        ResponseCookie resCookies = ResponseCookie
            .from("refresh_token", refresh_token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(refreshTokenExpiration)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, resCookies.toString())
            .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        //Lấy thông tin người dùng từ security
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        //Lấy thông tin người dùng từ DB
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if(currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
        }

        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<String> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token ) {
        //check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        return ResponseEntity.ok().body(email);
    }

}
