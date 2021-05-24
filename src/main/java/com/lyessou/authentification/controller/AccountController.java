package com.lyessou.authentification.controller;

import com.lyessou.authentification.entity.AppUser;
import com.lyessou.authentification.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public AppUser register(@RequestBody RegisterForm registerForm){
        if(!registerForm.getPassword().equals(registerForm.getRepassword())){
            throw new RuntimeException("You must confirm your password");
        }
        // vérifier si le user existe dans la bdd et ne pas l'accepter
        AppUser user = accountService.findUserByUserName(registerForm.getUserName());
        if(user != null){
            throw new RuntimeException("this user already exists");
        }
        AppUser appUser = new AppUser();
        appUser.setUserName(registerForm.getUserName());
        appUser.setPassword(registerForm.getPassword());
        accountService.saveUser(appUser);
        // une fois user enregistré on lui donne un role par defaul (role = USER)
        accountService.addRoleToUser(registerForm.getUserName(), "USER");
        return appUser;
    }
}
