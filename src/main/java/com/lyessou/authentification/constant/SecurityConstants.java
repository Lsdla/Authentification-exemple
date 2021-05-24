package com.lyessou.authentification.constant;

public class SecurityConstants {
    public static final String SECRET = "azulfellawen@dev.akhir";
    public static final long EXPIRATION_TIME = 864_000_000; //10days
    public static final String TOKEN_PREFIX = "Bearer "; // il y a un espace dans cette chaine de caractere
    public static final String HEADER_STRING = "Authorization";
}
