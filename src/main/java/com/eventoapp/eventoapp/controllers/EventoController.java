package com.eventoapp.eventoapp.controllers;

import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @GetMapping(value = "/cadastrarEvento")
    public String form() {
        return "evento/formEvento";
    }

    @PostMapping(value = "/cadastrarEvento")
    public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagemErro", "Verifique os Campos!");
            return "redirect:/cadastrarEvento";
        }

        er.save(evento);

        attributes.addFlashAttribute("mensagemSucesso", "Evento Adicionado Com Sucesso!!");
        return "redirect:/cadastrarEvento";
    }

    @RequestMapping(value = {"", "/eventos"})
    public ModelAndView listaEventos() {
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();
        mv.addObject("eventos", eventos);
        return mv;
    }

    @GetMapping(value = "/{codigo}")
    public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
        Evento evento = er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        mv.addObject("evento", evento);

        Iterable<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("convidados", convidados);
        return mv;
    }

    @PostMapping(value = "/{codigo}")
    public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado
            , BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagemErro", "Verifique os Campos!");
            return "redirect:/{codigo}";
        }
        Evento evento = er.findByCodigo(codigo);
        convidado.setEvento(evento);
        cr.save(convidado);
        attributes.addFlashAttribute("mensagemSucesso", "Convidado adicionado com Sucesso!!");
        return "redirect:/{codigo}";
    }

    @RequestMapping("/deletarEvento")
    public String deletarEvento(long codigo) {
        Evento evento = er.findByCodigo(codigo);
        er.delete(evento);
        return "redirect:/eventos";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg) {
        Convidado convidado = cr.findByRg(rg);
        cr.delete(convidado);

        Evento evento = convidado.getEvento();
        String codigo = "" + evento.getCodigo();
        return "redirect:/" + codigo;
    }
}
