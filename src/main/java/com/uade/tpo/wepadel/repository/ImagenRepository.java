package com.uade.tpo.wepadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByProductoIdOrderByIdAsc(Long productoId);

}
