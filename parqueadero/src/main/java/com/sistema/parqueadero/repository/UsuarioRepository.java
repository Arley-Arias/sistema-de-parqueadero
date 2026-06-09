package com.sistema.parqueadero.repository;

import com.sistema.parqueadero.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}