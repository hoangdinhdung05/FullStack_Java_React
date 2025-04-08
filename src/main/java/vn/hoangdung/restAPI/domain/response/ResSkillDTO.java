package vn.hoangdung.restAPI.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResSkillDTO {

    private long id;
    private String name;
    private List<JobSkill> jobSkills;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobSkill {
        private long id;
        private String name;
    }

}
