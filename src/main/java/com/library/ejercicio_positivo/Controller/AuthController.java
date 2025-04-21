package com.library.ejercicio_positivo.Controller;

import com.library.ejercicio_positivo.Model.UserEntity;
import com.library.ejercicio_positivo.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }

        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }

        return "auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "auth/logout";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity userEntity, Model model) {
        if (userService.existsByUsername(userEntity.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "auth/register";
        }

        userService.save(userEntity);
        return "redirect:/auth/login?success=registered";
    }
}
