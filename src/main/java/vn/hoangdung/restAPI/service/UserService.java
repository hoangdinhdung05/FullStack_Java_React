package vn.hoangdung.restAPI.service;

import org.springframework.stereotype.Service;

import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

}
