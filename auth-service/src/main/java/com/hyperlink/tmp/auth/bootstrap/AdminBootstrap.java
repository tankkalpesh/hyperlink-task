package com.hyperlink.tmp.auth.bootstrap;

import com.hyperlink.tmp.auth.model.User;
import com.hyperlink.tmp.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${auth.bootstrap.admin.enabled}")
    private boolean enabled;

    @Value("${auth.bootstrap.admin.email}")
    private String adminEmail;

    @Value("${auth.bootstrap.admin.password}")
    private String adminPassword;

    @Value("${auth.bootstrap.admin.full-name}")
    private String adminFullName;

    public AdminBootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (!enabled) {
            return;
        }

        Optional<User> existing = userRepository.findByEmail(adminEmail);
        if (existing.isPresent()) {
            User user = existing.get();
            user.setRole("ADMIN");
            user.setActive(true);
            userRepository.save(user);
            log.info("Admin user already existed and was ensured active: {}", adminEmail);
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setFullName(adminFullName);
        admin.setRole("ADMIN");
        admin.setActive(true);
        userRepository.save(admin);
        log.info("Bootstrapped admin user: {}", adminEmail);
    }
}
