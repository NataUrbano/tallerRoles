package com.library.ejercicio_positivo.Service;

import com.library.ejercicio_positivo.Model.RoleEntity;
import com.library.ejercicio_positivo.Model.RoleEnum;
import com.library.ejercicio_positivo.Model.UserEntity;
import com.library.ejercicio_positivo.Repository.RoleRepository;
import com.library.ejercicio_positivo.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        // Codificar la contraseña antes de guardar
        if (userEntity.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }

        // Asegurarse de que el usuario tenga al menos un rol
        if (userEntity.getRoles() == null || userEntity.getRoles().isEmpty()) {
            RoleEntity userRole = roleRepository.findByName(RoleEnum.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            userEntity.setRoles(Set.of(userRole));
        } else {
            // Ensure all roles are managed entities
            Set<RoleEntity> managedRoles = new HashSet<>();
            for (RoleEntity role : userEntity.getRoles()) {
                RoleEntity managedRole = roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                managedRoles.add(managedRole);
            }
            userEntity.setRoles(managedRoles);
        }

        return userRepository.save(userEntity);
    }
}
