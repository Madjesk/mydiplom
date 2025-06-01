package dto;

public record ActionLogResponse(
        String bpName,
        String stageName,
        String dateTime,
        boolean success
) {}
