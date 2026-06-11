package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, String> {
}