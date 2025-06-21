package com.banco.sistemabancario.controller;

import com.banco.sistemabancario.model.TransaccionRequest;
import com.banco.sistemabancario.model.TransferenciaRequest;
import com.banco.sistemabancario.service.TransaccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacciones")
@CrossOrigin(origins = "http://localhost:8000")
public class TransaccionesController {

    @Autowired
    private TransaccionesService transaccionesService;

    @PostMapping("/depositar")
    public String depositar(@RequestBody TransaccionRequest request) {
        return transaccionesService.depositar(request.getCuenta(), request.getMonto());
    }

    @PostMapping("/retirar")
    public String retirar(@RequestBody TransaccionRequest request) {
        return transaccionesService.retirar(request.getCuenta(), request.getMonto());
    }

    @PostMapping("/transferir")
    public String transferir(@RequestBody TransferenciaRequest request) {
        return transaccionesService.transferir(
                request.getCuentaOrigen(),
                request.getCuentaDestino(),
                request.getMonto()
        );
    }
}
