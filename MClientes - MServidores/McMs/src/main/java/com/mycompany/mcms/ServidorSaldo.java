package com.mycompany.mcms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorSaldo {
    private static ConcurrentHashMap<String, Double> cuentas = new ConcurrentHashMap<>();
    
    public static void main(String[] args){
        cuentas.put("1234567890", 9500.0);
        cuentas.put("2468024680", 10000.0);
        cuentas.put("3692581470", 6348.3);
        cuentas.put("4826048260", 100.80);
        cuentas.put("6284062840", 1150000.0);
        
        try{
            ServerSocket ss = new ServerSocket(4000);
            System.out.println("Servidor de saldo activo en el puerto 4000");
            
            while(true){
                Socket cl = ss.accept();
                System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
                new Thread(new MSaldo(cl)).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private static class MSaldo implements Runnable{
        private Socket cl;
        public MSaldo(Socket cl){
            this.cl = cl;
        }
        @Override
        public void run() {
            try{
                BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                PrintWriter sal = new PrintWriter(cl.getOutputStream(),true);
                String opc = ent.readLine();
                String cuenta = ent.readLine();
                switch(opc){
                    case "C":   
                            if(cuentas.containsKey(cuenta)){
                                sal.println(cuentas.get(cuenta));
                            }else{
                                sal.println("Cuenta no encontrada");
                            }
                            break;
                    case "A":
                            double nuevoSaldo = Double.parseDouble(ent.readLine());
                            if (cuentas.containsKey(cuenta)) {
                                cuentas.put(cuenta, nuevoSaldo);
                                sal.println("Saldo actualizado correctamente.");
                            } else {
                                sal.println("Cuenta no encontrada, no se puede actualizar saldo.");
                            }
                            break;
                    default:sal.println("Operacion no valida");
                            break;
                }
            }catch(IOException e){
                System.err.println("Error con cliente: " + cl.getInetAddress() + ":" + cl.getPort());
            } finally {
                try {
                    cl.close();
                    System.out.println("Cliente desconectado.");
                } catch (IOException e) {
                    System.err.println("Error al cerrar la conexión con el cliente.");
                }
            }
        } 
    }
}
