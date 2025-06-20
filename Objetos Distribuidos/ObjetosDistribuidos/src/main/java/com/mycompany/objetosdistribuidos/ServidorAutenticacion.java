package com.mycompany.objetosdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorAutenticacion {
    public static void main(String[] args) {
        try {
            IAutenticacion obj = new ImpAutenticacion();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("AutenticacionService", obj);
            System.out.println("Servidor de Autenticacion RMI listo.");
        } catch (Exception e) {
            System.err.println("Error al iniciar Servidor de Autenticación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

