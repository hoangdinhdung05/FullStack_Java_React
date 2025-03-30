package vn.hoangdung.restAPI.service;


import java.util.List;
import java.util.Optional;

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

    //Get List Company
    public List<Company> fetchAllCompany() {
        return this.companyRepository.findAll();
    }

    //Update Company
    public Company handleUpdateCompany(Company c) {
        Optional<Company> companyOptional = this.companyRepository.findById(c.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(c.getLogo());
            currentCompany.setName(c.getName());
            currentCompany.setDescription(c.getDescription());
            currentCompany.setAddress(c.getAddress());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    //Delete Company
    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }

}
