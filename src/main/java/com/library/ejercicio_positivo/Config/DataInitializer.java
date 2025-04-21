package com.library.ejercicio_positivo.Config;

import com.library.ejercicio_positivo.Model.RoleEnum;
import com.library.ejercicio_positivo.Model.UserEntity;
import com.library.ejercicio_positivo.Service.RoleService;
import com.library.ejercicio_positivo.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleService roleService, UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles
        roleService.initializeRoles();

        // Crear usuarios de prueba con contraseñas en texto plano
        createTestUser("admin", "admin123", "admin@ejemplo.com", RoleEnum.ADMIN);
        createTestUser("editor", "editor123", "editor@ejemplo.com", RoleEnum.EDITOR);
        createTestUser("creator", "creator123", "creator@ejemplo.com", RoleEnum.CREATOR);
        createTestUser("user", "user123", "user@ejemplo.com", RoleEnum.USER);
    }

    private void createTestUser(String username, String plainPassword,
                                String email, RoleEnum role) {
        if (!userService.existsByUsername(username)) {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setPassword(plainPassword); // Contraseña en texto plano
            user.setEmail(email);
            user.setEnabled(true);
            user.setRoles(Set.of(roleService.findByName(role)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"))));

            // El UserService se encargará de encriptar la contraseña
            userService.save(user);

            System.out.println("Usuario de prueba creado: " + username + " - Contraseña: " + plainPassword);
        }
    }
}