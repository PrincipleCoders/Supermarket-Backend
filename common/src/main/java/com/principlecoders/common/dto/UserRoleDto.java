package com.principlecoders.common.dto;

import com.principlecoders.common.utils.UserRoles;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleDto {
    private String userId;
    private UserRoles role;
}
