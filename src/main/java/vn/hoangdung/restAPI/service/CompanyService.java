package vn.hoangdung.restAPI.service;

import org.springframework.stereotype.Service;

import vn.hoangdung.restAPI.domain.Company;
import vn.hoangdung.restAPI.repository.CompanyRepository;


@Service
public class CompanyService {
    
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    //Create Company
    public Company handleCreateCompany(Company c) {
        return this.companyRepository.save(c);
    }

}
