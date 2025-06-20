package com.mycompany.atm.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    private static Map<String, CuentaBancaria> cuentas = new HashMap<>();
    
    public static void main(String[] args) {
        String numCuenta;
        String operacion;
        int nip;
        double montoR,montoD;
        cuentas.put("1234567890", new CuentaBancaria("1234567890","Juan Perez",5000.0,1234));
        cuentas.put("2468024680", new CuentaBancaria("2468024680","Jesus Gonzalez",10000.0,2468));
        cuentas.put("3692581470", new CuentaBancaria("3692581470","Santiago Muñoz",6348.3,3692));
        cuentas.put("4826048260", new CuentaBancaria("4826048260","Rodrigo Peralta",1.80,4826));
        cuentas.put("6284062840", new CuentaBancaria("6284062840","Ernesto Caballero",1000000.1,6284));
        
        try{
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("Servidor bancario iniciado en el puerto 3000...");
            while (true) {
                Socket cl = ss.accept();
                System.out.println("Cliente conectado.");
                try{
                    BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                    PrintWriter sal = new PrintWriter(cl.getOutputStream(), true);
                    //Recibiendo numero de cuenta
                    numCuenta = ent.readLine();
                    //Recibiendo nip
                    nip = Integer.parseInt(ent.readLine());
                    //Validando numero de cuenta y nip
                    if(cuentas.containsKey(numCuenta)){
                        CuentaBancaria cuenta = cuentas.get(numCuenta);
                        if(cuenta.autenticar(nip)){
                            sal.println("Bienvenido "+cuenta.getTitular());
                            sal.flush();
                            while(true){
                                operacion = ent.readLine();
                                switch(operacion){
                                    case "C": sal.println("Saldo actual: $"+cuenta.consultarSaldo());
                                              sal.flush();
                                              break;
                                    case "D": montoD = Double.parseDouble(ent.readLine());
                                              cuenta.depositar(montoD);
                                              sal.println("Deposito exitoso. Nuevo saldo: $"+cuenta.consultarSaldo());
                                              sal.flush();
                                              break;
                                    case "R": montoR = Double.parseDouble(ent.readLine());
                                              if(montoR>cuenta.consultarSaldo()){
                                                  sal.println("La cuenta no cuenta con los fondos necesarios.\nSaldo: $"+cuenta.consultarSaldo());
                                              sal.flush();
                                              }else{
                                                cuenta.retirar(montoR);
                                                sal.println("Retiro exitoso. Nuevo saldo: $"+cuenta.consultarSaldo());
                                                sal.flush();
                                              }
                                              break;
                                    case "S": sal.println("Sesión finalizada.");
                                              sal.flush();
                                              break;
                                }
                                if(operacion.equals("S")){
                                    break;
                                }
                            }
                        }else
                            sal.println("NIP incorrecto.");
                            sal.flush();
                    }else{
                        sal.println("Numero de cuenta no encontrado.");
                        sal.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Cliente desconectado inesperadamente.");
                } finally {
                    try {
                        cl.close();
                        System.out.println("Cliente desconectado.");
                    } catch (IOException e) {
                        System.out.println("Error al cerrar la conexión con el cliente.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
