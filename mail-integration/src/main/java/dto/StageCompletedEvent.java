package dto;

import java.time.LocalDateTime;

public record StageCompletedEvent(
        Long  companyId,
        Long  bpId,
        Long  stageId,
        boolean success,
        LocalDateTime finishedAt
) {}
