package com.mycompany.multicliente;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServidorMulticliente{
    private static Map<String, CuentaBancaria> cuentas = new HashMap<>();

    public static void main(String[] args) {
        cuentas.put("1234567890", new CuentaBancaria("1234567890", "Juan Perez", 5000.0, 1234));
        cuentas.put("2468024680", new CuentaBancaria("2468024680", "Jesus Gonzalez", 10000.0, 2468));
        cuentas.put("3692581470", new CuentaBancaria("3692581470", "Santiago Muñoz", 6348.3, 3692));
        cuentas.put("4826048260", new CuentaBancaria("4826048260", "Rodrigo Peralta", 1.80, 4826));
        cuentas.put("6284062840", new CuentaBancaria("6284062840", "Ernesto Caballero", 1000000.1, 6284));

        try(ServerSocket ss = new ServerSocket(3000)) {
            System.out.println("Servidor bancario iniciado en el puerto 3000...");
            while (true) {
                Socket cl = ss.accept();
                System.out.println("Cliente conectado desde puerto "+cl.getPort());
                //Creando hilo que atiende a cliente
                new Thread(new MClientes(cl)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MClientes implements Runnable{
        private Socket cl;
        public MClientes(Socket cl){
            this.cl = cl;
        }
        @Override
        public void run(){
            try {
                BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                PrintWriter sal = new PrintWriter(cl.getOutputStream(), true);
                // Recibiendo número de cuenta
                String numCuenta = ent.readLine();
                // Recibiendo NIP
                int nip = Integer.parseInt(ent.readLine());
                // Validando número de cuenta y NIP
                if (cuentas.containsKey(numCuenta)) {
                    CuentaBancaria cuenta = cuentas.get(numCuenta);
                    if (cuenta.autenticar(nip)) {
                        System.out.println("Cliente "+cuenta.getNumeroCuenta()+" autenticado correctamente");
                        sal.println("Bienvenido " + cuenta.getTitular());
                        MOperaciones(ent,sal,cuenta);
                    } else {
                        sal.println("NIP incorrecto.");
                    }
                } else {
                    sal.println("Número de cuenta no encontrado.");
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado inesperadamente desde puerto "+cl.getPort());
            } finally {
                try {
                    cl.close();
                    System.out.println("Cliente desconectado desde puerto "+cl.getPort());
                } catch (IOException e) {
                    System.out.println("Error al cerrar la conexión con el cliente.");
                }
            }
        }
    }

    private static void MOperaciones(BufferedReader ent,PrintWriter sal, CuentaBancaria cuenta) throws IOException{
        String operacion;
        while (true) {
            operacion = ent.readLine();
            if (operacion == null) break;
            switch (operacion) {
                case "C":   sal.println("Saldo actual: $" + cuenta.consultarSaldo());
                            break;
                case "D":   double montoD = Double.parseDouble(ent.readLine());
                            cuenta.depositar(montoD);
                            sal.println("Deposito exitoso. Nuevo saldo: $" + cuenta.consultarSaldo());
                            break;
                case "R":   sal.println(cuenta.consultarSaldo());
                            double montoR = Double.parseDouble(ent.readLine());
                            if (montoR > cuenta.consultarSaldo()) {
                                sal.println("Fondos insuficientes. Saldo actual: $" + cuenta.consultarSaldo());
                            } else {
                                cuenta.retirar(montoR);
                                sal.println("Retiro exitoso. Nuevo saldo: $" + cuenta.consultarSaldo());
                            }
                            break;
                case "T":   sal.println(cuenta.consultarSaldo());
                            String cuentaDestino = ent.readLine();
                            double montoT = Double.parseDouble(ent.readLine());
                            if (cuentas.containsKey(cuentaDestino)) {
                                CuentaBancaria destino = cuentas.get(cuentaDestino);
                                if (cuenta.transferir(destino, montoT)) {
                                    sal.println("Transferencia exitosa. Nuevo saldo: $" + cuenta.consultarSaldo());
                                } else {
                                    sal.println("Saldo insuficiente");
                                }
                            } else 
                                sal.println("Cuenta de destino no encontrada.");
                            break;
                case "S":   sal.println("Sesion finalizada.");
                            break;
            }
        }
    }
}

