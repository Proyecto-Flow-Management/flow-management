package com.proyecto.flowmanagement.backend.persistence.repository;

import com.proyecto.flowmanagement.backend.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.flowmanagement.backend.persistence.entity.Guide;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGuideRepo extends JpaRepository<Guide,Long>{

    @Query("select c from Guide c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.label) like lower(concat('%', :searchTerm, '%'))") //
    List<Guide> search(@Param("searchTerm") String searchTerm); //
}
