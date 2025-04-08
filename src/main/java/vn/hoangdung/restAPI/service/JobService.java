package vn.hoangdung.restAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoangdung.restAPI.domain.Job;
import vn.hoangdung.restAPI.domain.Skill;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.domain.response.job.ResCreateJobDTO;
import vn.hoangdung.restAPI.domain.response.job.ResJobDTO;
import vn.hoangdung.restAPI.domain.response.job.ResUpdateJobDTO;
import vn.hoangdung.restAPI.repository.JobRepository;
import vn.hoangdung.restAPI.repository.SkillRepository;
import vn.hoangdung.restAPI.util.JobSpecification;
import vn.hoangdung.restAPI.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    // fetch job by id
    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    // Create Job
    public ResCreateJobDTO create(Job j) throws IdInvalidException {
        // check skills
        if (j.getSkills() != null) {
            List<Long> reqSkills = j.getSkills()
                    .stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);

            if (dbSkills.isEmpty()) {
                // Không có skill nào hợp lệ => có thể throw nếu muốn
                throw new IdInvalidException("Không có kỹ năng nào hợp lệ.");
            }

            j.setSkills(dbSkills);
        }

        // create job
        Job currentJob = this.jobRepository.save(j);

        // convert response
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(currentJob.getId());
        dto.setName(currentJob.getName());
        dto.setSalary(currentJob.getSalary());
        dto.setQuantity(currentJob.getQuantity());
        dto.setLocation(currentJob.getLocation());
        dto.setLevel(currentJob.getLevel());
        dto.setStartDate(currentJob.getStartDate());
        dto.setEndDate(currentJob.getEndDate());
        dto.setActive(currentJob.isActive());
        dto.setCreatedAt(currentJob.getCreatedAt());
        dto.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> skills = currentJob.getSkills()
                    .stream().map(Skill::getName)
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }

        return dto;
    }

    // Update Job
    public ResUpdateJobDTO update(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");
        }

        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream()
                    .map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        Job updatedJob = this.jobRepository.save(job);
        return convertToResUpdateDTO(updatedJob);
    }

    // Delete Job
    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    // Fetch All Jobs with Filter + Pagination
    public ResultPaginationDTO fetchAllJobs(String name, String location, String level, Pageable pageable) {
        Specification<Job> spec = Specification.where(JobSpecification.hasName(name))
                .and(JobSpecification.hasLocation(location))
                .and(JobSpecification.hasLevel(level));

        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        // Build meta
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageJob.getNumber() + 1);
        meta.setPageSize(pageJob.getSize());
        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());
        result.setMeta(meta);

        // Map data
        List<ResJobDTO> data = pageJob.getContent()
                .stream()
                .map(this::convertToResJobDTO)
                .collect(Collectors.toList());

        result.setResult(data);
        return result;
    }

    // Convert Job -> ResJobDTO
    private ResJobDTO convertToResJobDTO(Job job) {
        ResJobDTO dto = new ResJobDTO();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setLevel(job.getLevel());
        dto.setStartDate(job.getStartDate());
        dto.setEndDate(job.getEndDate());
        dto.setActive(job.isActive());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setCreatedBy(job.getCreatedBy());
        dto.setQuantity(job.getQuantity());

        if (job.getSkills() != null) {
            List<String> skills = job.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    // Convert Job -> ResUpdateJobDTO
    private ResUpdateJobDTO convertToResUpdateDTO(Job job) {
        ResUpdateJobDTO rs = new ResUpdateJobDTO();
        rs.setId(job.getId());
        rs.setName(job.getName());
        rs.setSalary(job.getSalary());
        rs.setQuantity(job.getQuantity());
        rs.setLocation(job.getLocation());
        rs.setLevel(job.getLevel());
        rs.setStartDate(job.getStartDate());
        rs.setEndDate(job.getEndDate());
        rs.setActive(job.isActive());
        rs.setUpdatedAt(job.getUpdatedAt());
        rs.setUpdatedBy(job.getUpdatedBy());

        if (job.getSkills() != null) {
            List<String> skills = job.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .collect(Collectors.toList());
            rs.setSkills(skills);
        }
        return rs;
    }
}
