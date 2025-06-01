package dto;

import java.util.List;

public record BusinessProcessResponse(
        Long id,
        String name,
        String description,
        List<StageDto> stages
) {
    public record StageDto(Long id, String name, String groupName,
                           String mailText, int priority) {}
}
