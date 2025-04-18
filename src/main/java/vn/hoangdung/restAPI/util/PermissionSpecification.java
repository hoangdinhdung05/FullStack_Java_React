package vn.hoangdung.restAPI.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangdung.restAPI.domain.Permission;

public class PermissionSpecification {

    public static Specification<Permission> hasName(String name) {
        return (Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if(name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

}
