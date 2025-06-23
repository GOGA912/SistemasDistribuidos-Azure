package com.banco.microservicios.movementsService;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class MovementsService {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/movimientos", new MovimientosHandler());
            server.setExecutor(null); 
            server.start();
            System.out.println("MovementsServices iniciado en http://localhost:"+port+"/movimientos");
        } catch (Exception e) {
            System.out.println("Error al iniciar AccountService: " + e.getMessage());
        }
    }
}
