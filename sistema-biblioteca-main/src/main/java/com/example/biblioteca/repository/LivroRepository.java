package com.example.biblioteca.repository;

import com.example.biblioteca.model.Livro;
import com.example.biblioteca.model.StatusLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByStatus(StatusLivro status);
}
