package vn.hoangdung.restAPI.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangdung.restAPI.domain.Permission;
import vn.hoangdung.restAPI.domain.Role;

public class RoleSpecification {

    public static Specification<Role> hasName(String name) {
        return (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if(name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

}
