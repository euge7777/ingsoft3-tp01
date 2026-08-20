package com.agustin.server.dtos.requests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    String email;
    String password;
}
