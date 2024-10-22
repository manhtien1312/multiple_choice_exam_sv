package com.graduationproject.exam_supervision_server.repository;

import com.graduationproject.exam_supervision_server.model.ERole;
import com.graduationproject.exam_supervision_server.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByRoleName(ERole roleName);

}
