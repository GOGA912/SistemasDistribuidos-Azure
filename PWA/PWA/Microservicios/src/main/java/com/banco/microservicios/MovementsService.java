package com.banco.microservicios;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class MovementsService {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8085), 0);
            server.createContext("/movimientos", new MovimientosHandler());
            server.setExecutor(null); 
            server.start();
            System.out.println("MovementsServices iniciado en http://localhost:8085/movimientos");
        } catch (Exception e) {
            System.out.println("Error al iniciar AccountService: " + e.getMessage());
        }
    }
}
