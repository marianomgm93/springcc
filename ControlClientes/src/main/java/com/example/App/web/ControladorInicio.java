package com.example.App.web;

import com.example.App.dao.PersonaDao;
import com.example.App.domain.Persona;
import com.example.App.service.PersonaService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String inicio(Model model, Principal principal) {
        var personas = personaService.listarPersonas();
        log.info("Ejecutando el controlador de Spring MVC");
        log.info("Usuario que hizo login: "+principal.getName());
        model.addAttribute("personas", personas);
        return "index";

    }

    @GetMapping("/agregar")
    public String agregar(Persona persona) {
        return "modificar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errores) {
        if (errores.hasErrors()) {
            return "modificar";
        }
        personaService.guardar(persona);
        return "redirect:/";
    }

    @GetMapping("/editar/{idPersona}")
    public String editar(Persona persona, Model model) {
        persona = personaService.encontrarPersona(persona);
        model.addAttribute("persona", persona);
        return "modificar";
    }

    @GetMapping("/eliminar")
    public String eliminar(Persona persona) {
        personaService.eliminar(persona);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Nombre de la plantilla de Thymeleaf (login.html)
    }

    @GetMapping("/errores/403")
    public String accessDenied() {
        return "errores/403"; // Nombre de la plantilla de Thymeleaf (403.html)
    }

}
