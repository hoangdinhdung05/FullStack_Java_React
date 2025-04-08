package vn.hoangdung.restAPI.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoangdung.restAPI.domain.Skill;
import vn.hoangdung.restAPI.domain.response.ResultPaginationDTO;
import vn.hoangdung.restAPI.service.SkillService;
import vn.hoangdung.restAPI.util.anotation.ApiMessage;
import vn.hoangdung.restAPI.util.error.IdInvalidException;

@RestController
@RequestMapping("api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    //create skill
    @PostMapping("/skills")
    @ApiMessage("Create a Skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill skill) throws IdInvalidException {

        //check name skill
        if(skill.getName() != null && this.skillService.isNameExists(skill.getName())) {
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
    }

    //Update Skill
    @PutMapping("/skills")
    @ApiMessage("Update a Skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill skill) throws IdInvalidException {

        Skill newSkill = this.skillService.fetchSkillById(skill.getId());
        //check id
        if(newSkill == null) {
            throw new IdInvalidException("Skill id = " + newSkill + " không tồn tại");
        }

        //check tên
        if (skill.getName() != null && this.skillService.isNameExists(skill.getName())) {
            if (!newSkill.getName().equals(skill.getName())) {
                throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
            }
        }

        newSkill.setName(skill.getName());
        return ResponseEntity.ok().body(this.skillService.updateSkill(newSkill));
    }

    //Delete Skill
    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + id + " không tồn tại");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

    //Get Skill By Id
    @GetMapping("/skills/{id}")
    @ApiMessage("Get Skill")
    public ResponseEntity<Skill> getSkill(@PathVariable long id) throws IdInvalidException {
        Skill skill = this.skillService.fetchSkillById(id);
        if (skill == null) {
            throw new IdInvalidException("Skill với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(skill);
    }

    //Get list Skill
    @GetMapping("/skills")
    @ApiMessage("Get list Skills")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO skills = this.skillService.fetchAllSkills(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(skills);

    }

}
