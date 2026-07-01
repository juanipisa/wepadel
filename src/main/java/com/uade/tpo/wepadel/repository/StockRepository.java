package com.uade.tpo.wepadel.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductoId(Long productoId);

    @Query("SELECT s FROM Stock s JOIN FETCH s.producto WHERE s.producto.id IN :productoIds")
    List<Stock> findByProductoIdIn(@Param("productoIds") Collection<Long> productoIds);

    void deleteByProductoId(Long productoId);
}
