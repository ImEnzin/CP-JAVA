package com.example.biblioteca.controller;

import com.example.biblioteca.model.Emprestimo;
import com.example.biblioteca.model.Livro;
import com.example.biblioteca.model.Usuario;
import com.example.biblioteca.service.EmprestimoService;
import com.example.biblioteca.service.LivroService;
import com.example.biblioteca.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final LivroService livroService;
    private final UsuarioRepository usuarioRepo;

    public EmprestimoController(EmprestimoService emprestimoService, LivroService livroService, UsuarioRepository usuarioRepo) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("livros", livroService.findDisponiveis());
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "emprestimos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("emprestimo") Emprestimo emprestimo,
                         BindingResult bindingResult,
                         Model model) {

        // 🚨 Aqui entra a validação da data
        if (emprestimo.getDataPrevistaDevolucao() != null && emprestimo.getDataRetirada() != null) {
            if (!emprestimo.getDataPrevistaDevolucao().isAfter(emprestimo.getDataRetirada())) {
                bindingResult.rejectValue("dataPrevistaDevolucao",
                        "error.dataPrevista",
                        "Data de devolução deve ser posterior à retirada");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("livros", livroService.findDisponiveis());
            model.addAttribute("usuarios", usuarioRepo.findAll());
            return "emprestimos/form";
        }

        emprestimoService.criarEmprestimo(emprestimo);
        return "redirect:/emprestimos/ativos";
    }

    @GetMapping("/ativos")
    public String listarAtivos(Model model) {
        model.addAttribute("emprestimos", emprestimoService.findAtivos());
        return "emprestimos/list";
    }

    @GetMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        emprestimoService.devolver(id);
        return "redirect:/emprestimos/ativos";
    }
}
