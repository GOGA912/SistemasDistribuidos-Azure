package com.mycompany.atm;

import java.util.Scanner;

/**
 *
 * @author galva
 */
public class ATM implements Runnable{
    private CuentaBancaria cuenta;
    private int nip;
    
    public ATM(CuentaBancaria cuenta, int nip){
        this.cuenta = cuenta;
        this.nip = nip;
    }
    
    @Override
    public void run(){
        if(!cuenta.autenticar(nip)){
            System.out.println("Nip incorrecto. Acceso denegado.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        int opc;
        do{
            System.out.println("\n Bienvenido a su cajero automatico, " + cuenta.getTitular());
            System.out.println("1. Consultar Saldo\n2. Depositar Dinero\n3. Retirar Dinero\n4. Salir");
            System.out.print("Seleccione una opcion: ");
            opc = scanner.nextInt();
            switch (opc) {
                case 1:
                    System.out.println("Saldo actual: " + cuenta.consultarSaldo());
                    break;
                case 2:
                    System.out.print("Ingrese monto a depositar: ");
                    double montoDeposito = scanner.nextDouble();
                    cuenta.depositar(montoDeposito);
                    break;
                case 3:
                    System.out.print("Ingrese monto a retirar: ");
                    double montoRetiro = scanner.nextDouble();
                    cuenta.retirar(montoRetiro);
                    break;
                case 4:
                    System.out.println("Sesión finalizada.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opc != 4);
    }
}
