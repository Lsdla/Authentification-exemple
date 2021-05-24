package com.lyessou.authentification.service.impl;

import com.lyessou.authentification.entity.AppRole;
import com.lyessou.authentification.entity.AppUser;
import com.lyessou.authentification.repository.AppRoleRepository;
import com.lyessou.authentification.repository.AppUserRepository;
import com.lyessou.authentification.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired // on a besoin de ça: car à l'enregistrement d'un utilisateur on recupere son mot de passe donc on doit l'encoder d'abord
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Override
    public AppUser saveUser(AppUser user) {
        String hashPassWord = bCryptPasswordEncoder.encode(user.getPassword()); //varibale pour stocker le mot de passe encoder recuperé apres inscription du user
        user.setPassword(hashPassWord);
        return appUserRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        AppRole role = appRoleRepository.findByRoleName(roleName);
        AppUser user = appUserRepository.findByUserName(userName);
        user.getRoles().add(role);
    }

    @Override
    public AppUser findUserByUserName(String userName) {
        return appUserRepository.findByUserName(userName);
    }
}
