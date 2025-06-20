package com.mycompany.mcms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorTransacciones {
   
    public static void main(String[] args){
        try{
            ServerSocket ss = new ServerSocket(5000);
            System.out.println("Servidor de transacciones activo en el puerto 5000");
            
            while(true){
                Socket cl = ss.accept();
                System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
                new Thread(new MTransacciones(cl)).start();
            }
        }catch(IOException e){
            System.out.println("Error al iniciar el Servidor de Transacciones: "+e.getMessage());
        }
    }
    
    private static class MTransacciones implements Runnable{
        private Socket cl;
        public MTransacciones(Socket cl){
            this.cl = cl;
        }
        @Override
        public void run() {
            try{
                BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                PrintWriter sal = new PrintWriter(cl.getOutputStream(),true);
                
                String opc = ent.readLine();
                String cuenta = ent.readLine();
                double saldoActual = consultarSaldo(cuenta);
                if (!solicitarAccesoSincronizacion()) {
                    sal.println("Error: No se pudo obtener acceso a la transacción.");
                    return;
                }
                switch(opc){
                    case "D": // Depósito
                        double montoD = Double.parseDouble(ent.readLine());
                        if (montoD > 0) {
                            saldoActual += montoD;
                            if (actualizarSaldo(cuenta, saldoActual)) {
                                sal.println("Deposito exitoso. Nuevo saldo: $" + saldoActual);
                            } else {
                                sal.println("Error al actualizar saldo en el servidor de saldo.");
                            }
                        } else {
                            sal.println("Error: El monto debe ser mayor a 0.");
                        }
                        break;

                    case "R": // Retiro
                        double montoR = Double.parseDouble(ent.readLine());
                        if (saldoActual >= montoR && montoR > 0) {
                            saldoActual -= montoR;
                            if (actualizarSaldo(cuenta, saldoActual)) {
                                sal.println("Retiro exitoso. Nuevo saldo: $" + saldoActual);
                            } else {
                                sal.println("Error al actualizar saldo en el servidor de saldo.");
                            }
                        } else {
                            sal.println("Error: Saldo insuficiente o monto inválido.");
                        }
                        break;

                    case "T": // Transferencia
                        sal.println(saldoActual);
                        String cuentaDestino = ent.readLine();
                        double montoT = Double.parseDouble(ent.readLine());
                        double saldoDestino = consultarSaldo(cuentaDestino);
                        
                        if (saldoDestino < 0) {
                            sal.println("Error: Cuenta de destino no encontrada.");
                        } else if (saldoActual >= montoT && montoT > 0) {
                            saldoActual -= montoT;
                            saldoDestino += montoT;

                            if (actualizarSaldo(cuenta, saldoActual) && actualizarSaldo(cuentaDestino, saldoDestino)) {
                                sal.println("Transferencia exitosa. Nuevo saldo: $" + saldoActual);
                            } else {
                                sal.println("Error al actualizar los saldos en el servidor de saldo.");
                            }
                        } else {
                            sal.println("Error: Saldo insuficiente, cuenta inexistente o monto inválido.");
                        }
                        break;

                    default:
                        sal.println("Operación no válida.");
                        break;
                }
                liberarAccesoSincronizacion();
            }catch(IOException e){
                System.err.println("Error en transacción: " + e.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ServidorTransacciones.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    cl.close();
                    System.out.println("Cliente desconectado del servidor de transacciones.");
                } catch (IOException e) {
                    System.err.println("Error al cerrar la conexión con el cliente.");
                }
            }
        }
    }
    
    private static boolean solicitarAccesoSincronizacion() throws IOException, InterruptedException {
        try {
            Socket sSync = new Socket("localhost", 6000);
            BufferedReader entSync = new BufferedReader(new InputStreamReader(sSync.getInputStream()));
            PrintWriter salSync = new PrintWriter(sSync.getOutputStream(), true);
            
            salSync.println("S");

            String respuesta;
            while (true) {
                respuesta = entSync.readLine();
                if ("P".equals(respuesta)) {
                    System.out.println("Entro a servidor de sincronizacion");
                    return true;
                } else if ("E".equals(respuesta)) {
                    System.out.println("En espera");
                    Thread.sleep(500); // Espera un momento antes de reintentar
                }
            }

        } catch (IOException e) {
            System.out.println("Error al solicitar acceso al Servidor de Sincronización.");
        }
        return false;
    }

    private static void liberarAccesoSincronizacion() {
        try (Socket sSync = new Socket("localhost", 6000);
             PrintWriter salSync = new PrintWriter(sSync.getOutputStream(), true)) {

            salSync.println("L");

        } catch (IOException e) {
            System.out.println("Error al liberar acceso en el Servidor de Sincronización.");
        }
    }
    
     private static double consultarSaldo(String cuenta) {
        try (Socket sSaldo = new Socket("localhost", 4000);
             BufferedReader entSaldo = new BufferedReader(new InputStreamReader(sSaldo.getInputStream()));
             PrintWriter salSaldo = new PrintWriter(sSaldo.getOutputStream(), true)) {

            salSaldo.println("C");
            salSaldo.println(cuenta);
            salSaldo.flush();

            String respuesta = entSaldo.readLine();
            if (respuesta == null || respuesta.equals("Cuenta no encontrada")) {
                return -1; // Indicar que la cuenta no existe
            }
            return Double.parseDouble(respuesta);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al consultar el saldo en el Servidor de Saldo: " + e.getMessage());
            return -1; // Indicar que hubo un error
        }
    }
    
    private static boolean actualizarSaldo(String cuenta, double nuevoSaldo) {
        try (Socket sSaldo = new Socket("localhost", 4000);
             PrintWriter salSaldo = new PrintWriter(sSaldo.getOutputStream(), true);
             BufferedReader entSaldo = new BufferedReader(new InputStreamReader(sSaldo.getInputStream()))) {

            salSaldo.println("A");
            salSaldo.println(cuenta);
            salSaldo.println(nuevoSaldo);
            salSaldo.flush();

            String respuesta = entSaldo.readLine();
            return respuesta != null && respuesta.equals("Saldo actualizado correctamente.");

        } catch (IOException e) {
            System.err.println("Error al actualizar saldo en el Servidor de Saldo: " + e.getMessage());
            return false;
        }
    }
}
