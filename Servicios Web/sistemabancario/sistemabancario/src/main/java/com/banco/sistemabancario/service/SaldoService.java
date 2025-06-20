package com.banco.sistemabancario.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SaldoService {

    private final Map<String, Double> cuentas = new ConcurrentHashMap<>();

    public SaldoService() {
        cuentas.put("1234567890", 9500.0);
        cuentas.put("2468024680", 10000.0);
        cuentas.put("3692581470", 6348.3);
        cuentas.put("4826048260", 100.80);
        cuentas.put("6284062840", 1150000.0);
    }

    public double consultarSaldo(String cuenta) {
        return cuentas.getOrDefault(cuenta, -1.0);
    }

    public String actualizarSaldo(String cuenta, double nuevoSaldo) {
        if (cuentas.containsKey(cuenta)) {
            cuentas.put(cuenta, nuevoSaldo);
            return "Nuevo saldo $" + nuevoSaldo;
        }
        return "Cuenta no encontrada.";
    }
}
