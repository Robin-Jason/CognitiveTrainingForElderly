package org.example.server.dataobject.Login;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
