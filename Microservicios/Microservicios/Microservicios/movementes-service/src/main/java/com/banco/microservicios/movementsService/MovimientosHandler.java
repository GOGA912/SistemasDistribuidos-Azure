package com.banco.microservicios.movementsService;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.URI;
import java.util.*;

public class MovimientosHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
        String metodo = exchange.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(metodo)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        if (!"GET".equalsIgnoreCase(metodo)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery(); 
        String cuenta = null;
        if (query != null && query.startsWith("cuenta=")) {
            cuenta = query.substring("cuenta=".length());
        }
        if (cuenta == null || cuenta.isEmpty()) {
            enviarRespuesta(exchange, 400, "Cuenta no especificada");
            return;
        }
        List<Map<String, String>> movimientos = BaseDatos.consultarMovimientos(cuenta);
        String respuestaJson = gson.toJson(movimientos);
        enviarRespuesta(exchange, 200, respuestaJson);
    }

    private void enviarRespuesta(HttpExchange exchange, int codigo, String respuesta) throws IOException {
        byte[] bytes = respuesta.getBytes("UTF-8");
        exchange.sendResponseHeaders(codigo, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
