package com.lyessou.authentification.service;

import com.lyessou.authentification.entity.AppRole;
import com.lyessou.authentification.entity.AppUser;

public interface AccountService {
    // cette couche permet de centraliser la gestion des utilisateurs et des roles

    public AppUser saveUser(AppUser user);
    public AppRole saveRole(AppRole role);
    public void addRoleToUser(String userName, String roleName);
    public AppUser findUserByUserName(String userName);
}
