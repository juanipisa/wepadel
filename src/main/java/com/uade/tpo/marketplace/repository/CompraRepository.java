package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.Orden;

@Repository
public interface CompraRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuarioId(Long usuarioId);
}
