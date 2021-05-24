package com.lyessou.authentification.repository;

import com.lyessou.authentification.entity.AppUser;
import com.lyessou.authentification.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public AppUser findByUserName(String userName);
}
