package com.banco.sistemabancario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionesService {

    @Autowired
    private SaldoService saldoService;

    public String depositar(String cuenta, double monto) {
        double saldoActual = saldoService.consultarSaldo(cuenta);
        if (saldoActual == -1) return "Cuenta no encontrada.";
        saldoActual += monto;
        return saldoService.actualizarSaldo(cuenta, saldoActual);
    }

    public String retirar(String cuenta, double monto) {
        synchronized (cuenta.intern()) {
            double saldoActual = saldoService.consultarSaldo(cuenta);
            if (saldoActual == -1) return "Cuenta no encontrada.";
            if (monto > saldoActual || monto <= 0) return "Error: Monto inválido o saldo insuficiente.";
            saldoActual -= monto;
            return saldoService.actualizarSaldo(cuenta, saldoActual);
        }
    }

    public String transferir(String origen, String destino, double monto) {
        // Para evitar deadlocks, siempre sincronizamos en orden (por orden alfabético de cuenta)
        String first = origen.compareTo(destino) < 0 ? origen : destino;
        String second = origen.compareTo(destino) < 0 ? destino : origen;

        synchronized (first.intern()) {
            synchronized (second.intern()) {
                double saldoOrigen = saldoService.consultarSaldo(origen);
                double saldoDestino = saldoService.consultarSaldo(destino);

                if (saldoOrigen == -1 || saldoDestino == -1)
                    return "Error: Cuenta(s) no encontrada(s).";
                if (monto <= 0 || monto > saldoOrigen)
                    return "Error: Monto inválido o saldo insuficiente.";

                saldoService.actualizarSaldo(origen, saldoOrigen - monto);
                saldoService.actualizarSaldo(destino, saldoDestino + monto);

                return "Transferencia exitosa. Nuevo saldo: $" + saldoService.consultarSaldo(origen);
            }
        }
    }
}
