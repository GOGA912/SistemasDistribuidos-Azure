package com.mycompany.objetosdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorSincronizacion {
    public static void main(String[] args) {
        try {
            ISincronizacion sync = new ImpSincronizacion();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("SyncService", sync);
            System.out.println("Servidor de Sincronizacion RMI listo.");
        } catch (Exception e) {
            System.err.println("Error al iniciar Servidor de Sincronización: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

