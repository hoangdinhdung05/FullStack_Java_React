package vn.hoangdung.restAPI.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangdung.restAPI.domain.Resume;

public class ResumeSpecification {

    public static Specification<Resume> hasName(String name) {
        return (Root<Resume> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if(name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

}
