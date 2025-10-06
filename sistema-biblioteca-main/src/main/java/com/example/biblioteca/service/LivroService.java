package com.example.biblioteca.service;

import com.example.biblioteca.model.Livro;
import com.example.biblioteca.model.StatusLivro;
import com.example.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LivroService {
    private final LivroRepository repo;
    public LivroService(LivroRepository repo){ this.repo = repo; }

    public List<Livro> findAll(){ return repo.findAll(); }
    public List<Livro> findDisponiveis(){ return repo.findByStatus(StatusLivro.DISPONIVEL); }
    public Livro findById(Long id){ return repo.findById(id).orElse(null); }
    public Livro save(Livro livro){ return repo.save(livro); }
    public void delete(Long id){ repo.deleteById(id); }

    @Transactional
    public void marcarEmprestado(Livro livro){
        livro.setStatus(StatusLivro.EMPRESTADO);
        repo.save(livro);
    }

    @Transactional
    public void marcarDisponivel(Livro livro){
        livro.setStatus(StatusLivro.DISPONIVEL);
        repo.save(livro);
    }
}
