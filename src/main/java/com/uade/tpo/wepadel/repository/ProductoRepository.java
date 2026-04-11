package com.uade.tpo.wepadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
