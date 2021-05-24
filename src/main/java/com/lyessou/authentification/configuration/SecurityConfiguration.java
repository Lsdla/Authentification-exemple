package com.lyessou.authentification.configuration;

import com.lyessou.authentification.filter.JwtAuthenticationFilter;
import com.lyessou.authentification.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //classe de configuration
@EnableWebSecurity // permet d'activer la securité web
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // permet d'encoder le mot de passe et le comparer avec celui deja crypter dans la bdd/// mais il faut l'instancier dans l'appli ou dans une classe de config car il n a pas de bean qui l'instancie par defaut

    // methode qui permet a spring security de chercher les utilisateur et leur role d'un endroit precis exemple de la bdd
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // configuration d'exemple d'utilisateur avec nom de user et mot de passe et role

       /* auth.inMemoryAuthentication() // les utilisateurs snt stockés en mémoire
                .withUser("admin").password("1234").roles("ADMIN", "USER")
                .and() // permet d'ajouter un autre urilisateru car on utilise un pattern builder
                .withUser("user").password("1234").roles("USER");*/

        // deuxième manière de s'authentifier est d'utiliser JDBC authentification: qui utilise des requetes que spring devrait utiliser pour recuperer les users de la base de donnée mais pas très utilisé

        /*auth.jdbcAuthentication()
                .usersByUsernameQuery("")
                .authoritiesByUsernameQuery("")*/

        // utilisation d'un systeme d'authentification basé sur une couche service, pour cela on va utiliser un attribut de type UserDetailsService plus Haut qui fait parti de spring
        // quand on utilise cette troisième methode==> il faut specifier quel type de hachage (ou encodage) de mot de passer
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    // methode qui permet de definir les droit d'acces et de definir les routes necessaire pour chaque utilisateur et ajout des filtres ou pas
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // pour utiliser les JWT on doit desactiver l'authentification basée sur les sessions et on maintient le fait de desactiver le CSRF.
        http.csrf().disable(); // permet de desactiver le synchrinized token qui est genéré automatiquement explication video part5 15ème minute
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //desactive concretement l'authentification par session et passer a un systeme d'authentification par valeur (jwt)

        // http.formLogin();//.loginPage("/login");      // permet d'utiliser un formulaire d'authentification fourni pas spring et en utilisant le loginPage c est pour creer un fomrulaire personnalisé
        http.authorizeRequests().antMatchers("/login/**", "/register/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/tasks/**").hasAuthority("ADMIN");      // ca veut dire si l'utilisateur envoie une requete de Post vers /tasks il ne faut l'accepter que s'il a le role "ADMIN"
        http.authorizeRequests().anyRequest().authenticated();       // permet d'indiquer a spring que tte les ressources de l'application necessitent une authentification
        http.addFilter(new JwtAuthenticationFilter(authenticationManager())); // utilisation du 1er filtre créé ds la class AuthenticationFilter
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class); // utilisation du 2eme filtre créé dans la classe Authorizationfilter.
    }
}
