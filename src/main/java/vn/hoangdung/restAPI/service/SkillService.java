package vn.hoangdung.restAPI.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoangdung.restAPI.domain.Skill;
import vn.hoangdung.restAPI.domain.response.ResSkillDTO;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.repository.SkillRepository;
import vn.hoangdung.restAPI.util.SkillSpecification;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    //check Name skill
    public boolean isNameExists(String name) {
        return this.skillRepository.existsByName(name);
    }

    //fetch skill by id
    public Skill fetchSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        return skillOptional.orElse(null); //check rỗng
    }

    //create skill
    public Skill createSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    //update Skill
    public Skill updateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    //delete Skill by id
    public void deleteSkill(long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        // delete skill
        this.skillRepository.delete(currentSkill);
    }

    //fetch list skill
    public ResultPaginationDTO fetchAllSkills(String name, Pageable pageable) {
        Specification<Skill> spec = Specification.where(SkillSpecification.hasName(name));
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);

        // Tạo meta
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageSkill.getNumber() + 1);
        mt.setPageSize(pageSkill.getSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);

        // Map dữ liệu skill -> ResSkillDTO
        List<ResSkillDTO> listSkill = pageSkill.getContent()
                .stream()
                .map(item -> new ResSkillDTO(
                        item.getId(),
                        item.getName(),
                        item.getJobs().stream()
                                .map(job -> new ResSkillDTO.JobSkill(
                                        job.getId(),
                                        job.getName()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        rs.setResult(listSkill);
        return rs;
    }


}
