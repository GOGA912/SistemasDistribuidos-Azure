package com.banco.microservicios;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class ConcurrencyService {
    public static void main(String[] args) {
        try {
            // Crear servidor HTTP en el puerto 8084
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Rutas
            server.createContext("/acceso", new AccesoHandler(true));
            server.createContext("/liberar", new AccesoHandler(false));

            server.setExecutor(null); // Usa el executor por defecto
            server.start();

            System.out.println("ConcurrencyService iniciado en http://localhost:"+port);

        } catch (Exception e) {
            System.out.println("Error al iniciar ConcurrencyService: " + e.getMessage());
        }
    }
}
