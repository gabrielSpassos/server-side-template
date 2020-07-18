package com.gabrielspassos.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String login;
    private String password;
    private String email;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;
}
