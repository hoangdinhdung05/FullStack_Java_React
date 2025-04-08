package vn.hoangdung.restAPI.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.hoangdung.restAPI.domain.Job;

public class JobSpecification {

    public static Specification<Job> hasName(String name) {
        return (Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    // Ví dụ mở rộng: tìm theo location
    public static Specification<Job> hasLocation(String location) {
        return (Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (location == null || location.isEmpty()) return null;
            return cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
        };
    }

    // Ví dụ mở rộng: tìm theo level
    public static Specification<Job> hasLevel(String level) {
        return (Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (level == null || level.isEmpty()) return null;
            return cb.equal(cb.lower(root.get("level")), level.toLowerCase());
        };
    }
}
