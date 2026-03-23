package com.example.content_calender.config;

import com.example.content_calender.model.Role;
import com.example.content_calender.model.User;
import com.example.content_calender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        User admin = userRepository.findByUsername(adminUsername).orElse(null);

        if(admin==null){
            User newAdmin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(Role.ADMIN,Role.USER))
                    .build();
            userRepository.save(newAdmin);
            return;
        }

        Set<Role> roles = admin.getRoles()==null?new HashSet<>(): new HashSet<>(admin.getRoles());
        if(!roles.contains(Role.ADMIN)) {
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            admin.setRoles(roles);
        }
        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(adminPassword));
        }
        userRepository.save(admin);
    }
}
