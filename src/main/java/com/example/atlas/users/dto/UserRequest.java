package com.example.atlas.users.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NonNull
    private String name;
    @Email
    @NonNull
    private String email;
    @NonNull
    @Length(min = 8)
    private String password;
}
