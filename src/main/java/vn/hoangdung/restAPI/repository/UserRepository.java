package vn.hoangdung.restAPI.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoangdung.restAPI.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
