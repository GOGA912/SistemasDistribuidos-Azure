package com.mycompany.objetosdistribuidos;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalTime;

public class ImpSaldo extends UnicastRemoteObject implements ISaldo {
    private ConcurrentHashMap<String, Double> cuentas;
    public ImpSaldo() throws RemoteException {
        super();
        cuentas = new ConcurrentHashMap<>();
        cuentas.put("1234567890", 9500.0);
        cuentas.put("2468024680", 10000.0);
        cuentas.put("3692581470", 6348.3);
        cuentas.put("4826048260", 100.80);
        cuentas.put("6284062840", 1150000.0);
        System.out.println("[" + LocalTime.now() + "] Servidor de Saldo inicializado con cuentas de prueba.");
    }
    @Override
    public double consultarSaldo(String cuenta) throws RemoteException {
        System.out.println("[" + LocalTime.now() + "] Consulta de saldo solicitada para cuenta: " + cuenta);
        if (cuentas.containsKey(cuenta)) {
            System.out.println("[" + LocalTime.now() + "] Saldo actual: $" + cuentas.get(cuenta));
            return cuentas.get(cuenta);
        } else {
            System.out.println("[" + LocalTime.now() + "] Cuenta no encontrada: " + cuenta);
            return -1; // Cuenta no encontrada
        }
    }
    @Override
    public String actualizarSaldo(String cuenta, double nuevoSaldo) throws RemoteException {
        System.out.println("[" + LocalTime.now() + "] Solicitud de actualización de saldo para cuenta: " + cuenta + " -> Nuevo saldo: $" + nuevoSaldo);
        if (cuentas.containsKey(cuenta)) {
            cuentas.put(cuenta, nuevoSaldo);
            System.out.println("[" + LocalTime.now() + "] Saldo actualizado correctamente para cuenta: " + cuenta);
            return "Nuevo saldo $" + consultarSaldo(cuenta);
        } else {
            System.out.println("[" + LocalTime.now() + "] Error: Cuenta no encontrada para actualizar saldo.");
            return "Cuenta no encontrada, no se puede actualizar saldo.";
        }
    }
}
