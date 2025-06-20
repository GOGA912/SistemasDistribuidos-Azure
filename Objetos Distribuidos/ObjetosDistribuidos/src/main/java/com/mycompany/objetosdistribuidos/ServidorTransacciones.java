package com.mycompany.objetosdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorTransacciones {
    public static void main(String[] args) {
        try {
            ITransacciones trans = new ImpTransacciones();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("TransaccionesService", trans);
            System.out.println("Servidor de Transacciones RMI listo.");
        } catch (Exception e) {
            System.err.println("Error al iniciar Servidor de Transacciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

