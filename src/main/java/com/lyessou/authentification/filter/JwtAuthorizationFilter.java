package com.lyessou.authentification.filter;

import com.lyessou.authentification.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*"); // authorise tt les domaine a envoyer des requetes
        response.addHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, " +
                        "Access-Control-Request-Method, " +
                        "Access-Control-Request-Headers, Authorization"); // autorise ts ces types de reponses
        response.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
        String jwt = request.getHeader(SecurityConstants.HEADER_STRING);
        System.out.println(jwt);
        if (request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            // si le token et null et s'il ne commence pas par le prefixe deja defini ==> pas la peine de le signer
            if(jwt == null || !jwt.startsWith(SecurityConstants.TOKEN_PREFIX)){
                filterChain.doFilter(request, response);
                return;
            }
            // sinon on signe le token
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(jwt.replace(SecurityConstants.TOKEN_PREFIX, "")) // remplacer le prefixe par une chaine vide.
                    .getBody();

            //recuperer le userName car dans le subject du token il y a le userName
            String userName = claims.getSubject();
            // recuperer la liste des role
            ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");

            // les roles qu'on vient de recurperer garce au token on doit les convertir en collection car spring a beoin d'une collection et non d'une arraylist.
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(r->{
                authorities.add(new SimpleGrantedAuthority(r.get("authority")));
            });

            // une fois qu'on a recuperer les username et les roles; sachant que le paseword est hasher donc pas besoin ==> on creer un objet de type usernamepassewordauthenticationtoken
            UsernamePasswordAuthenticationToken authenticatedUser =
                    new UsernamePasswordAuthenticationToken(userName, null, authorities);

            // puis on charge le contexte securité de spring on le charge par l'urtilisateur authentifié.
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

            // puis on procede au filtre.
            filterChain.doFilter(request, response);
        }
    }
}
