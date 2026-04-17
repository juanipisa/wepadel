package com.uade.tpo.wepadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.Descuento;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {

    List<Descuento> findByProductoId(Long productoId);
}