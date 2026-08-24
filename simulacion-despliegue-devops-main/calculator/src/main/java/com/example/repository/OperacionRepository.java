package com.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Operacion;

import java.util.List;

@Repository
public interface OperacionRepository extends JpaRepository<Operacion, Long> {

    List<Operacion> findTop5ByOrderByIdDesc();
}