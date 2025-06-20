package com.banco.sistemabancario.service;

import com.banco.sistemabancario.model.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {

    @Autowired
    private AutenticacionService auth;

    public String consultarSaldo(String numero) {
        Cuenta c = auth.obtenerCuenta(numero);
        return (c != null) ? "Saldo: $" + c.getSaldo() : "Cuenta no encontrada";
    }

    public String depositar(String numero, double monto) {
        Cuenta c = auth.obtenerCuenta(numero);
        if (c != null && monto > 0) {
            c.setSaldo(c.getSaldo() + monto);
            return "Depósito exitoso. Nuevo saldo: $" + c.getSaldo();
        }
        return "Error en depósito";
    }

    public String retirar(String numero, double monto) {
        Cuenta c = auth.obtenerCuenta(numero);
        if (c != null && monto > 0 && c.getSaldo() >= monto) {
            c.setSaldo(c.getSaldo() - monto);
            return "Retiro exitoso. Nuevo saldo: $" + c.getSaldo();
        }
        return "Error en retiro";
    }

    public String transferir(String origen, String destino, double monto) {
        Cuenta c1 = auth.obtenerCuenta(origen);
        Cuenta c2 = auth.obtenerCuenta(destino);
        if (c1 == null || c2 == null) return "Cuenta(s) no encontrada(s)";
        if (monto <= 0 || monto > c1.getSaldo()) return "Monto inválido o saldo insuficiente";
        c1.setSaldo(c1.getSaldo() - monto);
        c2.setSaldo(c2.getSaldo() + monto);
        return "Transferencia exitosa. Saldo nuevo origen: $" + c1.getSaldo();
    }
}
