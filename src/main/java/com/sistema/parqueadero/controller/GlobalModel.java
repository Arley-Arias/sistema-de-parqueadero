package com.sistema.parqueadero.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModel {

    // Verificar si es un Admin
    @ModelAttribute("esAdmin")
    public boolean verificarAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
    }
}