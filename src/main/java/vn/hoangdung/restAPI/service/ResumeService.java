package vn.hoangdung.restAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoangdung.restAPI.domain.Job;
import vn.hoangdung.restAPI.domain.Resume;
import vn.hoangdung.restAPI.domain.User;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.domain.response.Resume.ResCreateResumeDTO;
import vn.hoangdung.restAPI.domain.response.Resume.ResFetchResumeDTO;
import vn.hoangdung.restAPI.domain.response.Resume.ResUpdateResumeDTO;
import vn.hoangdung.restAPI.repository.JobRepository;
import vn.hoangdung.restAPI.repository.ResumeRepository;
import vn.hoangdung.restAPI.repository.UserRepository;
import vn.hoangdung.restAPI.util.ResumeSpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository,
                         JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public Optional<Resume> fetchById(long id) {
        return this.resumeRepository.findById(id);
    }

    public boolean checkResumeExistsByUserAndJob(Resume resume) {
        if (resume.getUser() == null || resume.getJob() == null) return false;

        boolean userExists = userRepository.existsById(resume.getUser().getId());
        boolean jobExists = jobRepository.existsById(resume.getJob().getId());

        return userExists && jobExists;
    }

    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume.getId());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        return res;
    }

    public ResUpdateResumeDTO update(Resume resume) {

        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());
        return res;
    }

    public void delete(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume) {
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        if (resume.getUser() != null) {
            res.setUser(new ResFetchResumeDTO.UserResume(
                    resume.getUser().getId(), resume.getUser().getName()));
        }

        if (resume.getJob() != null) {
            res.setJob(new ResFetchResumeDTO.JobResume(
                    resume.getJob().getId(), resume.getJob().getName()));
        }

        return res;
    }

    public ResultPaginationDTO getListResume(String name, Pageable pageable) {
        Specification<Resume> spec = Specification.where(ResumeSpecification.hasName(name));
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageResume.getNumber() + 1);
        meta.setPageSize(pageResume.getSize());
        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());
        result.setMeta(meta);

        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream()
                .map(this::getResume)
                .collect(Collectors.toList());

        result.setResult(listResume);
        return result;
    }
}
