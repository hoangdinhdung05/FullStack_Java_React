package vn.hoangdung.restAPI.domain.dto;

import jakarta.validation.constraints.NotBlank;


public class LoginDTO {

    @NotBlank(message = "Email không được để trống")
    private String username;
    @NotBlank(message = "Password không được để trống")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
