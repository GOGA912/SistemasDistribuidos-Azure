package com.mycompany.objetosdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorSaldo {
    public static void main(String[] args) {
        try {
            ISaldo saldo = new ImpSaldo();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("SaldoService", saldo);
            System.out.println("Servidor de Saldo RMI listo.");
        } catch (Exception e) {
            System.err.println("Error al iniciar Servidor de Saldo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

