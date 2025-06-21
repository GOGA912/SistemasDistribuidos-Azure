package com.banco.sistemabancario.controller;

import com.banco.sistemabancario.model.SaldoRequest;
import com.banco.sistemabancario.service.SaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saldo")
@CrossOrigin(origins = "http://localhost:8000")
public class SaldoController {

    @Autowired
    private SaldoService saldoService;

    @GetMapping("/{cuenta}")
    public double consultarSaldo(@PathVariable String cuenta) {
        return saldoService.consultarSaldo(cuenta);
    }

    @PutMapping
    public String actualizarSaldo(@RequestBody SaldoRequest request) {
        return saldoService.actualizarSaldo(request.getCuenta(), request.getNuevoSaldo());
    }
}
