package com.graduationproject.exam_supervision_server.service.implementation;

import com.graduationproject.exam_supervision_server.model.ERole;
import com.graduationproject.exam_supervision_server.model.Role;
import com.graduationproject.exam_supervision_server.repository.RoleRepository;
import com.graduationproject.exam_supervision_server.service.serviceinterface.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
