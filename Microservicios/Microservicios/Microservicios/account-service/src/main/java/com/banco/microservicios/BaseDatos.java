package com.banco.microservicios;

import com.banco.microservicios.accountService.Titular;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDatos {
    private static final String BD = "jdbc:postgresql://banco-bd-sistemas-distribuidos.postgres.database.azure.com:5432/banco?sslmode=require";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "Banco2025";

    private static Connection conectar() throws SQLException {
        return DriverManager.getConnection(BD, USUARIO, PASSWORD);
    }


    public static boolean validarCuenta(String numero, int nip) {
        String sql = "SELECT * FROM \"Cuentas\" WHERE numero = ? AND nip = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numero);
            stmt.setInt(2, nip);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
            return false;
        }
    }
    
    public static Titular consultarNombreSexo(String cuenta) {
        String sqlID = "SELECT titular_id FROM \"Cuentas\" WHERE numero = ?";
        String sqlNombre = "SELECT nombre, sexo FROM \"Clientes\" WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt1 = conn.prepareStatement(sqlID)) {
            stmt1.setString(1, cuenta);
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next()) {
                int id = rs1.getInt("titular_id");
                try (PreparedStatement stmt2 = conn.prepareStatement(sqlNombre)) {
                    stmt2.setInt(1, id);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        String nombre = rs2.getString("nombre");
                        String sexo = rs2.getString("sexo");
                        return new Titular(nombre, sexo);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de datos (consultarNombreSexo): " + e.getMessage());
        }
        return null;
    }


    public static double consultarSaldo(String cuenta) {
        String sql = "SELECT saldo FROM \"Cuentas\" WHERE numero = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar saldo: " + e.getMessage());
            return -1;
        }
    }


    public static boolean actualizarSaldo(String cuenta, double nuevoSaldo) {
        String sql = "UPDATE \"Cuentas\" SET saldo = ? WHERE numero = ?";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoSaldo);
            stmt.setString(2, cuenta);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar saldo: " + e.getMessage());
            return false;
        }
    }
    
    public static void registrarMovimiento(String cuenta, String tipo, double monto) {
        String sql = "INSERT INTO \"Movimientos\" (cuenta, tipo, monto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuenta);
            stmt.setString(2, tipo);
            stmt.setDouble(3, monto);

            // Obtener la fecha y hora en zona horaria de Ciudad de México
            ZoneId zonaCDMX = ZoneId.of("America/Mexico_City");
            ZonedDateTime ahoraCDMX = ZonedDateTime.now(zonaCDMX);
            Timestamp fechaCDMX = Timestamp.from(ahoraCDMX.toInstant());

            stmt.setTimestamp(4, fechaCDMX);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        }
    }
    
    public static void registrarTransferencia(String origen, String destino, double monto) {
        String sql = "INSERT INTO transferencias (cuenta_origen, cuenta_destino, monto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, origen);
            stmt.setString(2, destino);
            stmt.setDouble(3, monto);

            // Obtener hora de Ciudad de México
            ZoneId zonaCDMX = ZoneId.of("America/Mexico_City");
            ZonedDateTime ahoraCDMX = ZonedDateTime.now(zonaCDMX);
            Timestamp fechaCDMX = Timestamp.from(ahoraCDMX.toInstant());

            stmt.setTimestamp(4, fechaCDMX);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al registrar transferencia: " + e.getMessage());
        }
    }
    
    public static List<Map<String, String>> consultarMovimientos(String cuenta) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT tipo, monto, fecha FROM \"Movimientos\" WHERE cuenta = ? ORDER BY fecha DESC LIMIT 10";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cuenta);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> movimiento = new HashMap<>();
                OffsetDateTime fechaUTC = rs.getObject("fecha", OffsetDateTime.class);
                String fechaFormateada = fechaUTC.atZoneSameInstant(ZoneId.of("America/Mexico_City"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                movimiento.put("tipo", rs.getString("tipo"));
                movimiento.put("monto", String.valueOf(rs.getDouble("monto")));
                movimiento.put("fecha", fechaFormateada);
                lista.add(movimiento);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar movimientos: " + e.getMessage());
        }
        return lista;
    }


}
