package vn.hoangdung.restAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.service.UserService;


@RestController
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/user/create")
    @PostMapping("/user/create")
    public String createNewUser(@RequestBody User postManUser) {
        
        // User user = new User();

        // user.setName("Hoàng Hà");
        // user.setEmail("hoangdung0205@gmail.com.com");
        // user.setPassword("123456789");

        this.userService.handleCreateUser(postManUser);

        return "create User";
    }

}
