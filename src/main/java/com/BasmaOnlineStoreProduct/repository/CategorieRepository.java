package com.BasmaOnlineStoreProduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BasmaOnlineStoreProduct.beans.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
 public Categorie findByName(String name);
}
