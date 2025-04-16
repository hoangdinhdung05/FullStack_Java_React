package vn.hoangdung.restAPI.controller;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import vn.hoangdung.restAPI.domain.Company;
import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.repository.UserRepository;
import vn.hoangdung.restAPI.service.CompanyService;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;
import vn.hoangdung.restAPI.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    
    private final CompanyService companyService;
    private final UserRepository userRepository;

    public CompanyController(CompanyService companyService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    //create
    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqCompany));
    }

    //get list
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO companies = this.companyService.fetchAllCompany(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }
    

    //Update Company
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        Company updatedCompany = this.companyService.handleUpdateCompany(reqCompany);
        return ResponseEntity.ok(updatedCompany);
    }

    //Delete Company
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        Optional<Company> companyOptional = this.companyService.getCompanyById(id);
        //phải xóa user trước khi xóa company
        if(companyOptional.isPresent()) {
            Company com = companyOptional.get();
            //fetch all user
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }

        this.companyService.handleDeleteCompany(id);

        return ResponseEntity.ok(null);
    }

    //get by id
    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch Comany by id")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable long id) throws IdInvalidException {
        Optional<Company> companyOptional = this.companyService.getCompanyById(id);
        if(companyOptional.isEmpty()) {
            throw new IdInvalidException("Company với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(companyOptional.get());
    }

}

