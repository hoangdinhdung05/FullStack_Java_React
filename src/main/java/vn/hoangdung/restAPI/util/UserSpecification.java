package vn.hoangdung.restAPI.util;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import vn.hoangdung.restAPI.domain.User;

public class UserSpecification {
    
    public static Specification<User> hasName(String name) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if(name == null || name.isEmpty())  return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

}
