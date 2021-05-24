package com.lyessou.authentification.repository;

import com.lyessou.authentification.entity.AppRole;
import com.lyessou.authentification.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    public AppRole findByRoleName(String roleName);
}
