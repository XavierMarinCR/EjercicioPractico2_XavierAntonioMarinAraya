package com.fidelitas.plataforma.controller;

import com.fidelitas.plataforma.domain.Evento;
import com.fidelitas.plataforma.service.EventoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/evento")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var lista = eventoService.getEventos();
        model.addAttribute("eventos", lista);
        model.addAttribute("totalEventos", lista.size());
        return "/evento/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Evento evento, RedirectAttributes redirectAttributes) {
        eventoService.save(evento);
        redirectAttributes.addFlashAttribute("todoOk", "Evento guardado correctamente");
        return "redirect:/evento/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        String mensaje = "Evento eliminado correctamente";
        
        try {
            eventoService.delete(id);
        } catch (IllegalArgumentException e) {
            mensaje = "El evento no existe";
            redirectAttributes.addFlashAttribute("error", mensaje);
            return "redirect:/evento/listado";
        } catch (IllegalStateException e) {
            mensaje = "No se puede eliminar el evento";
            redirectAttributes.addFlashAttribute("error", mensaje);
            return "redirect:/evento/listado";
        } catch (Exception e) {
            mensaje = "Error inesperado al eliminar";
            redirectAttributes.addFlashAttribute("error", mensaje);
            return "redirect:/evento/listado";
        }
        redirectAttributes.addFlashAttribute("todoOk", mensaje);
        return "redirect:/evento/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            var eventoOpt = eventoService.getEvento(id);

            if (eventoOpt.isPresent()) {
                model.addAttribute("evento", eventoOpt.get());
                return "/evento/modifica";
            } else {
                redirectAttributes.addFlashAttribute("error", "El evento no existe");
                return "redirect:/evento/listado";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al buscar el evento");
            return "redirect:/evento/listado";
        }
    }
    
    @GetMapping("/buscar")
    public String buscar(@RequestParam String nombre, Model model) {
        var lista = eventoService.buscarPorNombre(nombre);
        model.addAttribute("eventos", lista);
        model.addAttribute("totalEventos", lista.size());
        return "/evento/listado";
    }

    @GetMapping("/buscarFechas")
    public String buscarFechas(@RequestParam String inicio,
            @RequestParam String fin,
            Model model) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);

        var lista = eventoService.buscarPorFechas(fechaInicio, fechaFin);
        model.addAttribute("eventos", lista);
        model.addAttribute("totalEventos", lista.size());

        return "/evento/listado";
    }

    @GetMapping("/activos")
    public String activos(Model model) {
        var lista = eventoService.buscarActivos();
        model.addAttribute("eventos", lista);
        model.addAttribute("totalEventos", lista.size());
        return "/evento/listado";
    }
}
