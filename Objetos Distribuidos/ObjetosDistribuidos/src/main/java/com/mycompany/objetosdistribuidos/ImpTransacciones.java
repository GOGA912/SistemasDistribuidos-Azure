package com.mycompany.objetosdistribuidos;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;

public class ImpTransacciones extends UnicastRemoteObject implements ITransacciones {

    private ISaldo saldo;
    private ISincronizacion sync;

    public ImpTransacciones() throws RemoteException {
        super();
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            saldo = (ISaldo) registry.lookup("SaldoService");
            sync = (ISincronizacion) registry.lookup("SyncService");
            System.out.println("[" + LocalTime.now() + "] Servidor de Transacciones conectado correctamente a los servicios remotos.");
        } catch (Exception e) {
            System.err.println("[" + LocalTime.now() + "] Error al conectar con servicios de saldo o sincronización: " + e.getMessage());
            throw new RemoteException("Servicios no disponibles.");
        }
    }

    @Override
    public String depositar(String cuenta, double monto) throws RemoteException {
        System.out.println("[" + LocalTime.now() + "] Cliente solicitó depósito en cuenta: " + cuenta + " por $" + monto);
        if (!sync.solicitarAcceso()) {
            return "Error: No se pudo obtener acceso a la transacción.";
        }

        try {
            double saldoActual = saldo.consultarSaldo(cuenta);
            if (saldoActual == -1) return "Cuenta no encontrada.";
            saldoActual += monto;
            String resultado = saldo.actualizarSaldo(cuenta, saldoActual);
            System.out.println("[" + LocalTime.now() + "] Depósito completado para cuenta " + cuenta + ". Nuevo saldo: $" + saldoActual);
            return resultado;
        } finally {
            sync.liberarAcceso();
        }
    }

    @Override
    public String retirar(String cuenta, double monto) throws RemoteException {
        System.out.println("[" + LocalTime.now() + "] Cliente solicitó retiro en cuenta: " + cuenta + " por $" + monto);
        if (!sync.solicitarAcceso()) {
            return "Error: No se pudo obtener acceso a la transacción.";
        }

        try {
            double saldoActual = saldo.consultarSaldo(cuenta);
            if (saldoActual == -1) return "Cuenta no encontrada.";
            if (monto <= 0 || monto > saldoActual) return "Error: Monto inválido o saldo insuficiente.";
            saldoActual -= monto;
            String resultado = saldo.actualizarSaldo(cuenta, saldoActual);
            System.out.println("[" + LocalTime.now() + "] Retiro realizado en cuenta " + cuenta + ". Nuevo saldo: $" + saldoActual);
            return resultado;
        } finally {
            sync.liberarAcceso();
        }
    }

    @Override
    public String transferir(String origen, String destino, double monto) throws RemoteException {
        System.out.println("[" + LocalTime.now() + "] Cliente solicitó transferencia de $" + monto + " desde " + origen + " a " + destino);
        if (!sync.solicitarAcceso()) {
            return "Error: No se pudo obtener acceso a la transacción.";
        }

        try {
            double saldoOrigen = saldo.consultarSaldo(origen);
            double saldoDestino = saldo.consultarSaldo(destino);

            if (saldoOrigen == -1 || saldoDestino == -1) return "Error: Cuenta(s) no encontrada(s).";
            if (monto <= 0 || monto > saldoOrigen) return "Error: Monto inválido o saldo insuficiente.";

            saldo.actualizarSaldo(origen, saldoOrigen - monto);
            saldo.actualizarSaldo(destino, saldoDestino + monto);
            double nuevoSaldoOrigen = saldo.consultarSaldo(origen);
            System.out.println("[" + LocalTime.now() + "] Transferencia completada. Nuevo saldo en cuenta origen (" + origen + "): $" + nuevoSaldoOrigen);
            return "Transferencia exitosa. Nuevo saldo en cuenta: $" + nuevoSaldoOrigen;
        } finally {
            sync.liberarAcceso();
        }
    }
}
