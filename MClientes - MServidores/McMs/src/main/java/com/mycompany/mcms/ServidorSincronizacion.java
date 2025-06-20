package com.mycompany.mcms;

import java.io.*;
import java.net.*;

public class ServidorSincronizacion {
    private static boolean enUso = false;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(6000)) {
            System.out.println("Servidor de Sincronizacion activo en el puerto 6000");

            while (true) {
                Socket cl = ss.accept();
                System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
                new Thread(new GestorAcceso(cl)).start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el Servidor de Sincronización: " + e.getMessage());
        }
    }

    private static class GestorAcceso implements Runnable {
        private final Socket cl;
        public GestorAcceso(Socket cl) {
            this.cl = cl;
        }
        @Override
        public void run() {
            try {
                BufferedReader ent = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                PrintWriter sal = new PrintWriter(cl.getOutputStream(), true);
                String solicitud = ent.readLine();
                if (!"S".equals(solicitud)) {
                    sal.println("ERROR: Solicitud no válida.");
                    return;
                }
                synchronized (lock) {
                    while (enUso) {
                        sal.println("E");
                        lock.wait();
                    }
                    enUso = true;
                    sal.println("P");
                }
                String fin = ent.readLine();
                if ("L".equals(fin)) {
                    synchronized (lock) {
                        enUso = false;
                        lock.notifyAll();
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error en comunicación con cliente: " + e.getMessage());
            }
        }
    }
}
