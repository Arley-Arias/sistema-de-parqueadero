package com.sistema.parqueadero.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModel {

    // Este código le dice a todas las pantallas HTML si el usuario es Admin o no
    @ModelAttribute("esAdmin")
    public boolean verificarAdmin(HttpServletRequest request) {
        return request.isUserInRole("ROLE_ADMIN");
    }
}