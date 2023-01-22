package com.routing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {
    private LocalDateTime timeStamp;
    private Boolean success = true;
    private String message;
}
