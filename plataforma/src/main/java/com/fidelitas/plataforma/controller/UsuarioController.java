package com.fidelitas.plataforma.controller;

import com.fidelitas.plataforma.domain.Usuario;
import com.fidelitas.plataforma.service.EventoService;
import com.fidelitas.plataforma.service.RolService;
import com.fidelitas.plataforma.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioController(UsuarioService usuarioService,
                             RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var usuarios = usuarioService.getUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("roles", rolService.getRoles());
        return "/usuario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Redirige al formulario de edición/creación para mostrar errores
            redirectAttributes.addFlashAttribute("error","Error en los datos del usuario");
            // Si no hay id, redirige al listado con modal para agregar
            if (usuario.getId() == null) {
                return "redirect:/usuario/listado";
            }
            // Si hay id, redirige al formulario de modificación
            return "redirect:/usuario/modificar/" + usuario.getId();
        }
        try {
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("todoOk", "Usuario guardado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long id,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(id);
            redirectAttributes.addFlashAttribute("todoOk", "Usuario eliminado correctamente");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe");

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar, tiene datos asociados");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado");
        }

        return "redirect:/usuario/listado";
    }
    

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        var usuarioOpt = usuarioService.getUsuario(id);

        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            model.addAttribute("roles", rolService.getRoles());
            return "/usuario/modifica";
        } else {
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado");
            return "redirect:/usuario/listado";
        }
    }
}
