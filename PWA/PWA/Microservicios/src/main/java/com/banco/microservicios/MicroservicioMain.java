package com.banco.microservicios;

public class MicroservicioMain {
    public static void main(String[] args) {
        System.out.println("Iniciando todos los microservicios...");

        // Iniciar LoginService
        new Thread(() -> {
            try {
                LoginService.main(null);
            } catch (Exception e) {
                System.out.println("Error en LoginService: " + e.getMessage());
            }
        }).start();

        // Iniciar AccountService
        new Thread(() -> {
            try {
                AccountService.main(null);
            } catch (Exception e) {
                System.out.println("Error en AccountService: " + e.getMessage());
            }
        }).start();

        // Iniciar TransactionService
        new Thread(() -> {
            try {
                TransactionService.main(null);
            } catch (Exception e) {
                System.out.println("Error en TransactionService: " + e.getMessage());
            }
        }).start();
        
        // Iniciar ConcurrencyService
        new Thread(() -> {
            try {
                ConcurrencyService.main(null);
            } catch (Exception e) {
                System.out.println("Error en ConcurrencyService: " + e.getMessage());
            }
        }).start();

        // Iniciar MovementsService
        new Thread(() -> {
            try {
                MovementsService.main(null);
            } catch (Exception e) {
                System.out.println("Error en MovementsService: " + e.getMessage());
            }
        }).start();
        // Puedes seguir agregando los demás de igual forma

        System.out.println("Todos los microservicios están iniciados.");
    }
}
