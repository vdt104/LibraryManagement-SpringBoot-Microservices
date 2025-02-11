package com.vdt.user_service.event;

import com.vdt.user_service.entity.Reader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReaderEvent {
    private String message;
    private Reader reader;
}
