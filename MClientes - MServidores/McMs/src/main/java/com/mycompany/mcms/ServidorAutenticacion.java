package com.mycompany.mcms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServidorAutenticacion {
    private static Map<String, Integer> cuentas = new HashMap<>();
    
    public static void main(String[] args){
        cuentas.put("1234567890", 1234);
        cuentas.put("2468024680", 2468);
        cuentas.put("3692581470", 3692);
        cuentas.put("4826048260", 4826);
        cuentas.put("6284062840", 6284);
        
        try{
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("Servidor de autenticacion activo en el puerto 3000");
            
            while(true){
                Socket cl = ss.accept();
                System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
                new Thread(new MAutenticacion(cl)).start();
            }
            
        }catch(IOException e){
            System.out.println("Error en el Servidor de Autenticación "+ e.getMessage());
        }
    }
    
    private static class MAutenticacion implements Runnable{
        private Socket cl;
        public MAutenticacion(Socket cl){
            this.cl = cl;
        }
        @Override
        public void run() {
            try{
                BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                PrintWriter sal = new PrintWriter(cl.getOutputStream(),true);
                String cuenta = ent.readLine();
                String nipStr = ent.readLine();
                int nip = Integer.parseInt(nipStr);
                if(cuentas.containsKey(cuenta) && cuentas.get(cuenta).equals(nip)){
                    sal.println("Autenticacion Exitosa");
                }else{
                    sal.println("Error en autenticación");
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
