package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.CompraItem;

@Repository
public interface CompraItemRepository extends JpaRepository<CompraItem, Long> {
    List<CompraItem> findByCompraId(Long compraId);
}
