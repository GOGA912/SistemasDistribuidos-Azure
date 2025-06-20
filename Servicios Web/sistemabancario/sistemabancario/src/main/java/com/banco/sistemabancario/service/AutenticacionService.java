package com.banco.sistemabancario.service;

import com.banco.sistemabancario.model.Cuenta;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AutenticacionService {

    private final Map<String, Cuenta> cuentas = new HashMap<>();

    public AutenticacionService() {
        cuentas.put("1234567890", new Cuenta("1234567890", 9500.0, 1234));
        cuentas.put("2468024680", new Cuenta("2468024680", 10000.0, 2468));
        cuentas.put("3692581470", new Cuenta("3692581470", 100000.0, 3692));
        cuentas.put("4826048260", new Cuenta("4826048260", 5000.0, 4862));
    }

    public String autenticar(String cuenta, int nip) {
        Cuenta c = cuentas.get(cuenta);
        if (c != null && c.getNip() == nip) {
            return "Autenticacion Exitosa";
        }
        return "Error en autenticación";
    }

    public Cuenta obtenerCuenta(String numero) {
        return cuentas.get(numero);
    }

    public Map<String, Cuenta> getTodas() {
        return cuentas;
    }
}
