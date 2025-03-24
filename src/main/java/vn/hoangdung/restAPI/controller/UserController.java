package vn.hoangdung.restAPI.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.service.UserService;
import vn.hoangdung.restAPI.service.error.IdInvalidException;


@RestController
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(
            @RequestBody User postUser) {

        User createUser = this.userService.handleCreateUser(postUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdException(IdInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {

        if(id <= 0 || id >= 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }

        this.userService.handleDeleteUser(id);
        // return "Delete User";
        return ResponseEntity.ok("Delete User");
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User fetchUser = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    // fetch all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = this.userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User userUpdate = this.userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdate);
    }

}
