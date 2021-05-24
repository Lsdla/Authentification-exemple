package com.lyessou.authentification;

import com.lyessou.authentification.entity.AppRole;
import com.lyessou.authentification.entity.AppUser;
import com.lyessou.authentification.entity.Task;
import com.lyessou.authentification.repository.TaskRepository;
import com.lyessou.authentification.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication //(exclude = {SecurityAutoConfiguration.class }) // permet de desactiver la securite par defaut de spring

public class AuthentificationApplication implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(AuthentificationApplication.class, args);
    }

    @Bean //l'annotation bean permet, apres la run de l'appli, d'etre executer et donc d'etre transformer en bean spring et puis etre utilier dans toutes les classes de 'appli avec l'annotation @Autowired
    public BCryptPasswordEncoder getBCPE(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        accountService.saveUser(new AppUser(null, "admin", "1234", null));
        accountService.saveUser(new AppUser(null, "user", "1234", null));
        accountService.saveRole(new AppRole(null, "ADMIN"));
        accountService.saveRole(new AppRole(null, "USER"));
        accountService.addRoleToUser("admin", "ADMIN");
        accountService.addRoleToUser("admin", "USER");
        accountService.addRoleToUser("user", "USER");


        Stream.of("T1", "T2", "T3").forEach(t->{
            taskRepository.save(new Task(null, t));
        });
        taskRepository.findAll().forEach(t->{
            System.out.println(t.getTaskName());
        });
    }
}
