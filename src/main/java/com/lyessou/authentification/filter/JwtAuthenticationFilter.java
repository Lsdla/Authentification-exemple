package com.lyessou.authentification.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyessou.authentification.constant.SecurityConstants;
import com.lyessou.authentification.entity.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AppUser appUser = null;
        try {
            appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // permet d'intersepter le login et password pour verifier le fonctionnememnt de cette methode avant de generer le token.
        System.out.println("**********************");
        System.out.println("UsreName: "+appUser.getUserName());
        System.out.println("Password: "+appUser.getPassword());
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUser.getUserName(), appUser.getPassword()));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        User springUser = (User) authResult.getPrincipal();
        // generation du token avec la classe des constantes deja créée ainsi que l'objet recuperer apres authentification.
        // on utilise la classe static builder.
        String jwt = Jwts.builder()
                .setSubject(springUser.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
                .claim("roles", springUser.getAuthorities())
                .compact();
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+jwt);
    }
}
