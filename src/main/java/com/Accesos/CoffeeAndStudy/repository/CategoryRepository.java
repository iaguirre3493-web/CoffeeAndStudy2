package com.Accesos.CoffeeAndStudy.repository;

import com.Accesos.CoffeeAndStudy.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByNameContainingIgnoreCase(String name);

    List<Category> findByActive(Boolean active);

    List<Category> findByPriority(Integer priority);

    List<Category> findByNameContainingIgnoreCaseAndActiveAndPriority(String name, Boolean active, Integer priority);
}