package com.mycompany.objetosdistribuidos;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;

public class ImpAutenticacion extends UnicastRemoteObject implements IAutenticacion {

    private Map<String, Integer> cuentas;

    public ImpAutenticacion() throws RemoteException {
        super();
        cuentas = new HashMap<>();
        cuentas.put("1234567890", 1234);
        cuentas.put("2468024680", 2468);
        cuentas.put("3692581470", 3692);
        cuentas.put("4826048260", 4826);
        cuentas.put("6284062840", 6284);
        System.out.println("[" + LocalTime.now() + "] Servidor de Autenticacion inicializado con cuentas de prueba.");
    }

    @Override
    public String autenticar(String cuenta, int nip) throws RemoteException {
        System.out.println("[" + LocalTime.now() + "] Solicitud de autenticación para cuenta: " + cuenta);
        if (cuentas.containsKey(cuenta) && cuentas.get(cuenta).equals(nip)) {
            System.out.println("[" + LocalTime.now() + "] Autenticación exitosa para cuenta: " + cuenta);
            return "Autenticacion Exitosa";
        } else {
            System.out.println("[" + LocalTime.now() + "] Fallo en autenticación para cuenta: " + cuenta);
            return "Error en autenticación";
        }
    }
}
