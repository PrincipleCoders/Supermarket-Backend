package com.principlecoders.common.dto;

import com.principlecoders.common.utils.UserRoles;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String telephone;
    private UserRoles role;
    private String name;
    private String password;
}
