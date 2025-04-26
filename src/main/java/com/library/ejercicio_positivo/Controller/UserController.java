package com.library.ejercicio_positivo.Controller;

import com.library.ejercicio_positivo.Model.RoleEntity;
import com.library.ejercicio_positivo.Model.RoleEnum;
import com.library.ejercicio_positivo.Model.UserEntity;
import com.library.ejercicio_positivo.Service.RoleService;
import com.library.ejercicio_positivo.Service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        return "users/view";
    }

    @PostMapping("/{id}/add-role")
    @PreAuthorize("hasRole('ADMIN')")
    public String addRole(@PathVariable Long id, @RequestParam RoleEnum roleEnum) {
        UserEntity user = userService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RoleEntity role = roleService.findByName(roleEnum).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.getRoles().add(role);
        userService.save(user);
        return "redirect:/users/" + id;
    }

    @PostMapping("/{id}/remove-role")
    @PreAuthorize("hasRole('ADMIN')")
    public String removeRole(@PathVariable Long id, @RequestParam RoleEnum roleEnum) {
        UserEntity user = userService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RoleEntity role = roleService.findByName(roleEnum).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.getRoles().remove(role);
        userService.save(user);
        return "redirect:/users/" + id;
    }
}
