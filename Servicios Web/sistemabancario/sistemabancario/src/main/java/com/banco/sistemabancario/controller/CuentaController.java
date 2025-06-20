package com.banco.sistemabancario.controller;

import com.banco.sistemabancario.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/{numero}/saldo")
    public String consultarSaldo(@PathVariable String numero) {
        return cuentaService.consultarSaldo(numero);
    }

    @PostMapping("/{numero}/deposito")
    public String depositar(@PathVariable String numero, @RequestParam double monto) {
        return cuentaService.depositar(numero, monto);
    }

    @PostMapping("/{numero}/retiro")
    public String retirar(@PathVariable String numero, @RequestParam double monto) {
        return cuentaService.retirar(numero, monto);
    }

    @PostMapping("/{origen}/transferencia")
    public String transferir(@PathVariable String origen,
                             @RequestParam String destino,
                             @RequestParam double monto) {
        return cuentaService.transferir(origen, destino, monto);
    }
}
