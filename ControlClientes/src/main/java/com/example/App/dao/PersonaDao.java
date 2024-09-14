package com.example.App.dao;

import com.example.App.domain.Persona;
import org.springframework.data.repository.CrudRepository;

public interface PersonaDao extends CrudRepository<Persona,Long>{
    
}
