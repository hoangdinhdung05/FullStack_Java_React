package vn.hoangdung.restAPI.service;



import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hoangdung.restAPI.domain.Company;
import vn.hoangdung.restAPI.domain.dto.Meta;
import vn.hoangdung.restAPI.domain.dto.ResultPaginationDTO;
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
    public ResultPaginationDTO fetchAllCompany(Pageable pageable) {

        Page<Company> pageCompany = this.companyRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta mt = new Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);

        rs.setResult(pageCompany.getContent());

        return rs;
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
