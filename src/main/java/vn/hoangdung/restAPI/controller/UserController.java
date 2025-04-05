package vn.hoangdung.restAPI.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.domain.dto.ResCreateUserDTO;
import vn.hoangdung.restAPI.domain.dto.ResUpdateUserDTO;
import vn.hoangdung.restAPI.domain.dto.ResUserDTO;
import vn.hoangdung.restAPI.domain.dto.ResultPaginationDTO;
import vn.hoangdung.restAPI.service.UserService;
import vn.hoangdung.restAPI.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody @Valid User postUser) throws IdInvalidException {

        //Check email exist
        boolean isEmailExist = this.userService.isEmailExist(postUser.getEmail());
        if(isEmailExist) {
            throw new IdInvalidException(
                    "Email " + postUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
        }

        String hashPassword = this.passwordEncoder.encode(postUser.getPassword());
        postUser.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(postUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if(user == null) {
            throw new IdInvalidException("User với id " + id + " không tồn tại.");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.fetchUserById(id);
        if(fetchUser == null) {
            throw new IdInvalidException("User với id " + id + " không tồn tại.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    // fetch all users
    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO users = this.userService.fetchAllUser(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User userUpdate = this.userService.handleUpdateUser(user);
        if(userUpdate == null) {
            throw new IdInvalidException("User với id " + user.getId() + " không tồn tại.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(userUpdate));
    }

}
