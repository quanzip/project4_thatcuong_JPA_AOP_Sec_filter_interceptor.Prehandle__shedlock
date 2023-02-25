package com.viettel.project.repository;

import com.viettel.project.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "select c from Category c where lower(c.name) like lower(:name) ")
    Page<Category> findCategoriesByName(@Param(value = "name") String search, Pageable pageable);
}
