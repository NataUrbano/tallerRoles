package com.library.ejercicio_positivo.Service;

import com.library.ejercicio_positivo.Model.RoleEntity;
import com.library.ejercicio_positivo.Model.RoleEnum;
import com.library.ejercicio_positivo.Repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RoleEntity> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<RoleEntity> findByName(RoleEnum name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public RoleEntity save(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    public void initializeRoles() {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleRepository.findByName(roleEnum).isEmpty()) {
                RoleEntity role = new RoleEntity();
                role.setName(roleEnum);
                roleRepository.save(role);
            }
        }
    }
}