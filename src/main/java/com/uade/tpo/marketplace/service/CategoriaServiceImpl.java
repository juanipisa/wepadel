package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Categoria;
import com.uade.tpo.marketplace.exceptions.CategoriaDuplicateException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> getCategoriaById(Long categoriaId) {
        return categoriaRepository.findById(categoriaId);
    }

    public Categoria createCategoria(String descripcion) throws CategoriaDuplicateException {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.stream().anyMatch(c -> c.getDescripcion().equals(descripcion))) {
            throw new CategoriaDuplicateException();
        }
        return categoriaRepository.save(new Categoria(descripcion));
    }

    public Optional<Categoria> updateCategoria(Long categoriaId, String descripcion, Boolean habilitada) {
        return categoriaRepository.findById(categoriaId).map(categoria -> {
            if (descripcion != null) categoria.setDescripcion(descripcion);
            if (habilitada != null) categoria.setHabilitada(habilitada);
            return categoriaRepository.save(categoria);
        });
    }

}
