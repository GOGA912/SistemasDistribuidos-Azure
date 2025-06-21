package com.banco.sistemabancario.controller;

import com.banco.sistemabancario.service.AutenticacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8000")
public class AutenticacionController {

    @Autowired
    private AutenticacionService authService;

    @GetMapping("/{cuenta}/{nip}")
    public String autenticar(@PathVariable String cuenta, @PathVariable int nip) {
        return authService.autenticar(cuenta, nip);
    }
}
