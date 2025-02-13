package com.vdt.notification_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("consume_by")
    private String consumeBy;

    private String message;

    @JsonProperty("is_read")
    private boolean isRead;
}
