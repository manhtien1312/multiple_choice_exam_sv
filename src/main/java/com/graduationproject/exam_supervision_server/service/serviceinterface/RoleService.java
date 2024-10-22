package com.graduationproject.exam_supervision_server.service.serviceinterface;

import com.graduationproject.exam_supervision_server.model.ERole;
import com.graduationproject.exam_supervision_server.model.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByRoleName(ERole roleName);

}
