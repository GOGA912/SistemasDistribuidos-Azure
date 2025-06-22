package com.banco.microservicios;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class TransactionService {
    public static void main(String[] args) {
        try {
            // Puerto del microservicio de transacciones
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Asociar rutas a su manejador
            server.createContext("/deposito", new OperacionHandler("deposito"));
            server.createContext("/retiro", new OperacionHandler("retiro"));
            server.createContext("/transferencia", new OperacionHandler("transferencia"));

            server.setExecutor(null); // Executor por defecto
            server.start();

            System.out.println("TransactionService iniciado en http://localhost:"+port);

        } catch (Exception e) {
            System.out.println("Error al iniciar TransactionService: " + e.getMessage());
        }
    }
}
