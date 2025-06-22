package com.banco.microservicios.accountService;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class AccountService {
    public static void main(String[] args) {
        try {
            // Puerto dinámico para despliegue (Cloud Run) o 8080 por defecto
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Ruta para operaciones de saldo
            server.createContext("/saldo", new SaldoHandler());

            server.setExecutor(null); // Usa el executor por defecto
            server.start();

            System.out.println("AccountService iniciado en http://localhost:" + port + "/saldo");

        } catch (Exception e) {
            System.out.println("Error al iniciar AccountService: " + e.getMessage());
        }
    }
}
