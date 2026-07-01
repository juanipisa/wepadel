package com.uade.tpo.wepadel.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByProductoIdOrderByIdAsc(Long productoId);

    @Query("""
            SELECT i.id AS id, i.nombre AS nombre, p.id AS productoId
            FROM Imagen i JOIN i.producto p
            WHERE p.id IN :productoIds
            ORDER BY i.id ASC
            """)
    List<ImagenMetadataProjection> findMetadataByProductoIdIn(@Param("productoIds") Collection<Long> productoIds);

    void deleteByProductoId(Long productoId);

    void deleteByProductoIdAndIdNot(Long productoId, Long id);

}
