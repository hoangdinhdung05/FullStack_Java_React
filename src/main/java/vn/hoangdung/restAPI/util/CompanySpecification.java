package vn.hoangdung.restAPI.util;

import org.springframework.data.jpa.domain.Specification;

import vn.hoangdung.restAPI.domain.Company;
import jakarta.persistence.criteria.*;

public class CompanySpecification {
    
    public static Specification<Company> hasName(String name) {
        return (Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction(); // Trả về điều kiện luôn đúng nếu không có name
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
    

}
