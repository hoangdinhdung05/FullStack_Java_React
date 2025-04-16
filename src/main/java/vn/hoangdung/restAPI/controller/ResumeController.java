package vn.hoangdung.restAPI.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoangdung.restAPI.domain.Resume;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.domain.response.Resume.ResCreateResumeDTO;
import vn.hoangdung.restAPI.domain.response.Resume.ResFetchResumeDTO;
import vn.hoangdung.restAPI.domain.response.Resume.ResUpdateResumeDTO;
import vn.hoangdung.restAPI.service.ResumeService;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;
import vn.hoangdung.restAPI.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
        //check id exists
        boolean isIdExists = this.resumeService.checkResumeExistsByUserAndJob(resume);
        if(!isIdExists) {
            throw new IdInvalidException("User id hoặc Job id không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Cập nhật resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        //check id exists
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
        if(reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
        }

        Resume newResume = reqResumeOptional.get();
        newResume.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.update(newResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a Resume By ID")
    public ResponseEntity<Void> delete(@PathVariable long id) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if(reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get Resume By ID")
    public ResponseEntity<ResFetchResumeDTO> getResume(@PathVariable long id) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if(reqResumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.resumeService.getResume(reqResumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("List Resumes")
    public ResponseEntity<ResultPaginationDTO> getList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        return ResponseEntity.ok(resumeService.getListResume(name, pageable));
    }
}
