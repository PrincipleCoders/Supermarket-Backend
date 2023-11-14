package com.principlecoders.common.dto;

import com.principlecoders.common.utils.UserRoles;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResultDto {
    private String id;
    private String email;
    private String address;
    private String telephone;
    private String name;
    private UserRoles role;
    private boolean isEmailVerified;
    private String imageUrl;
    private boolean isSocialLogin;
}
