package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Categoria;
import com.uade.tpo.marketplace.exceptions.CategoriaDuplicateException;

public interface CategoriaService {
    public List<Categoria> getCategorias();
    public Optional<Categoria> getCategoriaById(Long categoriaId);
    public Categoria createCategoria(String descripcion) throws CategoriaDuplicateException;
    public Optional<Categoria> updateCategoria(Long categoriaId, String descripcion, Boolean habilitada);
}
