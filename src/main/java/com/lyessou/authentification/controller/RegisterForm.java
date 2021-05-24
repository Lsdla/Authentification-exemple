package com.lyessou.authentification.controller;

//cette classe nous sert a recuperer ce qu'on saisi dans le formulaire pour nous permettre de confirmer le password saisi

import lombok.Data;

@Data
public class RegisterForm {
    private String userName;
    private String password;
    private String repassword; // attribut de confirmation de password
}
