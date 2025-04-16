package vn.hoangdung.restAPI.domain.response.Resume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoangdung.restAPI.domain.Job;

import java.time.Instant;

@Getter
@Setter
public class ResCreateResumeDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}

