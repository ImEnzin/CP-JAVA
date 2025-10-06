package com.example.biblioteca.service;

import com.example.biblioteca.model.Emprestimo;
import com.example.biblioteca.model.Livro;
import com.example.biblioteca.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepo;
    private final LivroService livroService;
    public EmprestimoService(EmprestimoRepository emprestimoRepo, LivroService livroService){
        this.emprestimoRepo = emprestimoRepo;
        this.livroService = livroService;
    }

    @Transactional
    public Emprestimo criarEmprestimo(Emprestimo e) {
        Livro l = e.getLivro();
        if (l.getStatus() == null || l.getStatus().name().equals("DISPONIVEL")) {
            l.setStatus(com.example.biblioteca.model.StatusLivro.EMPRESTADO);
            livroService.save(l);
            return emprestimoRepo.save(e);
        } else {
            throw new IllegalStateException("Livro já está emprestado");
        }
    }

    public List<Emprestimo> findAtivos(){ return emprestimoRepo.findByDataDevolucaoIsNull(); }

    @Transactional
    public void devolver(Long emprestimoId) {
        Emprestimo e = emprestimoRepo.findById(emprestimoId).orElseThrow();
        e.setDataDevolucao(LocalDate.now());
        Livro l = e.getLivro();
        l.setStatus(com.example.biblioteca.model.StatusLivro.DISPONIVEL);
        livroService.save(l);
        emprestimoRepo.save(e);
    }
}
