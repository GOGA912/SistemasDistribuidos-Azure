package com.mycompany.objetosdistribuidos;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        int nip, opc = 0, montoR;
        double montoD;
        String cuentaD;

        Registry registry = LocateRegistry.getRegistry("localhost");
        IAutenticacion auth = (IAutenticacion) registry.lookup("AutenticacionService");
        ISaldo saldoStub = (ISaldo) registry.lookup("SaldoService");
        ITransacciones transStub = (ITransacciones) registry.lookup("TransaccionesService");

        Scanner scan = new Scanner(System.in);

        // Solicitar datos de autenticación
        System.out.print("Ingrese su numero de cuenta: ");
        String numCuenta = scan.nextLine();

        System.out.print("Ingrese su NIP: ");
        nip = scan.nextInt();

        String respuesta = auth.autenticar(numCuenta, nip);
        System.out.println(respuesta);

        if (!respuesta.startsWith("Autenticacion Exitosa")) {
            return;
        }

        while (true) {
            System.out.println("\nSeleccione la opcion deseada:");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Transferencia");
            System.out.println("5. Salir");

            opc = scan.nextInt();
            scan.nextLine(); // limpiar buffer

            switch (opc) {
                case 1: // Consultar saldo
                    double saldo = saldoStub.consultarSaldo(numCuenta);
                    if (saldo == -1) {
                        System.out.println("\nCuenta no encontrada.");
                    } else {
                        System.out.println("\nSaldo Actual: $" + saldo);
                    }
                    break;

                case 2: // Depositar
                    System.out.print("\nIngrese el monto a depositar: $");
                    montoD = scan.nextDouble();
                    scan.nextLine(); // limpiar buffer
                    respuesta = transStub.depositar(numCuenta, montoD);
                    System.out.println(respuesta);
                    break;

                case 3: // Retirar
                    System.out.print("\nIngrese el monto a retirar: $");
                    montoR = scan.nextInt();
                    scan.nextLine(); // limpiar buffer
                    respuesta = transStub.retirar(numCuenta, montoR);
                    System.out.println(respuesta);
                    break;

                case 4: // Transferencia
                    double saldoDisponible = saldoStub.consultarSaldo(numCuenta);
                    System.out.println("\nSaldo disponible: $" + saldoDisponible);

                    System.out.print("Ingrese el numero de cuenta destino: ");
                    cuentaD = scan.nextLine();

                    System.out.print("Ingrese el monto a transferir: $");
                    montoD = scan.nextDouble();
                    scan.nextLine(); // limpiar buffer

                    respuesta = transStub.transferir(numCuenta, cuentaD, montoD);
                    System.out.println("\n"+respuesta);
                    break;

                case 5:
                    System.out.println("Sesion finalizada.");
                    return;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }
}
