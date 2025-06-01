package dto;

public record BusinessProcessStageRequest(
        String name,
        String groupName,
        String mailText,
        int    priority
) {}
