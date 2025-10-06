package com.example.biblioteca.controller;

import com.example.biblioteca.model.Livro;
import com.example.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livros")
public class LivroController {
    private final LivroService service;
    public LivroController(LivroService service){ this.service = service; }

    @GetMapping
    public String listar(Model model){
        model.addAttribute("livros", service.findAll());
        return "livros/list";
    }

    @GetMapping("/disponiveis")
    public String listarDisponiveis(Model model){
        model.addAttribute("livros", service.findDisponiveis());
        return "livros/list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model){
        model.addAttribute("livro", new Livro());
        return "livros/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("livro") Livro livro, BindingResult br){
        if(br.hasErrors()){
            return "livros/form";
        }
        service.save(livro);
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model){
        model.addAttribute("livro", service.findById(id));
        return "livros/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id){
        service.delete(id);
        return "redirect:/livros";
    }
}
