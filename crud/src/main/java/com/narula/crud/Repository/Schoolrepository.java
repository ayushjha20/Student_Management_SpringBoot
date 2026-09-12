package com.narula.crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narula.crud.Entity.School;

@Repository 
public interface Schoolrepository extends JpaRepository<School,Long> {

    
}