package com.banco.microservicios;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class LoginService {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
            server.createContext("/login", new LoginHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("LoginService iniciado en http://localhost:8081/login");
        } catch (Exception e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}

