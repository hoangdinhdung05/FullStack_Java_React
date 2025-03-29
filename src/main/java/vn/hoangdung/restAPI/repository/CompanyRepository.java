package vn.hoangdung.restAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoangdung.restAPI.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
}
