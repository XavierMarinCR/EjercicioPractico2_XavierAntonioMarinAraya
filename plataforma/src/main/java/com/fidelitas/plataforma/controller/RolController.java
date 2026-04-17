package com.fidelitas.plataforma.controller;

import com.fidelitas.plataforma.domain.Rol;
import com.fidelitas.plataforma.service.RolService;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rol")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var lista = rolService.getRoles();
        model.addAttribute("roles", lista);
        model.addAttribute("totalRoles", lista.size());
        return "/rol/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Rol rol, RedirectAttributes redirectAttributes) {
        try {
            rolService.save(rol);
            redirectAttributes.addFlashAttribute("todoOk", "Registro guardado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rol/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long id, RedirectAttributes redirectAttributes) {

        try {
            rolService.delete(id);
            redirectAttributes.addFlashAttribute("todoOk", "Registro eliminado correctamente");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El rol no existe");

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar, tiene datos asociados");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado");
        }

        return "redirect:/rol/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Rol rol = rolService.getRol(id);
            model.addAttribute("rol", rol);
            return "/rol/modifica";
        } catch (NoSuchElementException e) {
            // Captura la excepción de 'no encontrado' del servicio
            redirectAttributes.addFlashAttribute("error", "El registro no existe");
            return "redirect:/rol/listado";
        }
    }
}
