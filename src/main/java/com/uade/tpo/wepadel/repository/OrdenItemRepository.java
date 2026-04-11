package com.uade.tpo.wepadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.wepadel.entity.OrdenItem;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {
    List<OrdenItem> findByOrdenId(Long ordenId);
}
