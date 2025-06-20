package com.mycompany.atm;

import java.util.Scanner;
/**
 *
 * @author galva
 */
public class SimuladorCajero {
    public static void main(String[] args) {
        // Crear una cuenta bancaria de ejemplo
        CuentaBancaria cuenta = new CuentaBancaria("123456", "Juan Perez", 5000, 1234);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese su clave (4 digitos): ");
        int claveIngresada = scanner.nextInt();

        // Crear e iniciar un único hilo para manejar la sesión del cliente
        Thread cliente = new Thread(new ATM(cuenta, claveIngresada), "UsuarioATM");
        cliente.start();
    }
}

