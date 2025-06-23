package com.banco.microservicios.loginService;

import com.banco.microservicios.BaseDatos;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import java.io.*;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder jsonBuilder = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            jsonBuilder.append(linea);
        }
        Gson gson = new Gson();
        Login login = gson.fromJson(jsonBuilder.toString(), Login.class);
        boolean valido = BaseDatos.validarCuenta(login.getNumero(), login.getNip());
        String respuestaJson;
        int codigo;
        if (valido) {
            Titular titular = BaseDatos.consultarNombreSexo(login.getNumero());
            if (titular != null) {
                respuestaJson = gson.toJson(new RespuestaLogin(
                    "Autenticacion Exitosa",titular.getNombre(),titular.getSexo()));
                codigo = 200;
            } else {
                respuestaJson = gson.toJson(new RespuestaLogin("Error interno"));
                codigo = 500;
            }
        } else {
            respuestaJson = gson.toJson(new RespuestaLogin("Error en autenticación"));
            codigo = 401;
        }
        exchange.sendResponseHeaders(codigo, respuestaJson.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(respuestaJson.getBytes());
        os.close();
    }
}
