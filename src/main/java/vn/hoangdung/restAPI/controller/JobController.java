package vn.hoangdung.restAPI.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoangdung.restAPI.domain.Job;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.domain.response.job.ResCreateJobDTO;
import vn.hoangdung.restAPI.domain.response.job.ResUpdateJobDTO;
import vn.hoangdung.restAPI.service.JobService;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;
import vn.hoangdung.restAPI.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ✅ Create Job
    @PostMapping("/jobs")
    @ApiMessage("Create a Job")
    public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) throws IdInvalidException {
        ResCreateJobDTO currentJob = this.jobService.create(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(currentJob);
    }

    // ✅ Update Job
    @PutMapping("/jobs")
    @ApiMessage("Update a Job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobService.fetchJobById(job.getId());
        if (jobOptional.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok(this.jobService.update(job));
    }

    // ✅ Delete Job
    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a Job")
    public ResponseEntity<Void> delete(@PathVariable long id) throws IdInvalidException {
        Optional<Job> deleteJob = this.jobService.fetchJobById(id);
        if (deleteJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content là đúng chuẩn
    }

    // ✅ Get Job by ID
    @GetMapping("/jobs/{id}")
    @ApiMessage("Get Job By Id")
    public ResponseEntity<Job> getJob(@PathVariable long id) throws IdInvalidException {
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if (jobOptional.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        return ResponseEntity.ok(jobOptional.get());
    }

    // ✅ Get list Job
    @GetMapping("/jobs")
    @ApiMessage("Get List Job")
    public ResponseEntity<ResultPaginationDTO> getListJob(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        ResultPaginationDTO result = jobService.fetchAllJobs(name, location, level, pageable);
        return ResponseEntity.ok(result);
    }
}
