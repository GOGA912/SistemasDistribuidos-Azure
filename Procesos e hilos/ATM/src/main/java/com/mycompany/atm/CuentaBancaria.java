package com.mycompany.atm;
/**
 *
 * @author galva
 */
public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private int nip;
    
    public CuentaBancaria(String numeroCuenta, String titular, double saldo, int nip){
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.nip = nip;
    }
    
    public String getTitular(){
        return titular;
    }
    
    public double consultarSaldo(){
        return saldo;
    }
    
    public void depositar(double monto){
        if(monto > 0){
            saldo += monto;
            System.out.println("Deposito exitoso. Nuevo saldo $"+saldo);
        }
    }
    
    public void retirar(double monto){
        if(monto < saldo){
            saldo -= monto;
            System.out.println("Retiro exitoso. Nuevo saldo $"+saldo);
        }else{
            System.out.println("La cantidad debe ser menor al saldo total de la cuenta.");
        }
    }
    
    public boolean autenticar(int claveIngresada) {
        return nip==claveIngresada;
    }
}
