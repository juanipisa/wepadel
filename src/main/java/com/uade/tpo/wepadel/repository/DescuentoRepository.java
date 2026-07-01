package com.uade.tpo.wepadel.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.Descuento;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {

    List<Descuento> findByProductoId(Long productoId);

    @Query("SELECT d FROM Descuento d JOIN FETCH d.producto WHERE d.producto.id IN :productoIds")
    List<Descuento> findByProductoIdIn(@Param("productoIds") Collection<Long> productoIds);

    void deleteByProductoId(Long productoId);
}