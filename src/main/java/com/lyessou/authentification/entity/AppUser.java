package com.lyessou.authentification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
//@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true) // annotation qui permet d'indiquer que cet attribut doit etre unique
    private String userName;
    //@JsonIgnore //permet de ne pas retourner l'encodage du mot de passe depuis la bdd par contre avec cette annotation ça ignore aussi l'envoie du password vers la bdd lors de l'inscription et donc ca fera une erreur==> la solution c'est de generer le getter/setter du password et mettre l'annotation @JsonIgnore sur le getter et l'annotation @JsonSetter sur le setter comme ca le setter passer et le getter sera bloqué.
    private String password;
    @ManyToMany(fetch = FetchType.EAGER) // permet au chargement de chaque utilisateur de charger aussi ses roles
    private Collection<AppRole> roles = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<AppRole> roles) {
        this.roles = roles;
    }

}
