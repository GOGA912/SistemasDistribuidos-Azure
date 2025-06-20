package com.banco.sistemabancario.model;

public class Cuenta {
    private String numero;
    private double saldo;
    private int nip;

    public Cuenta(String numero, double saldo, int nip) {
        this.numero = numero;
        this.saldo = saldo;
        this.nip = nip;
    }
    public Cuenta() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getNip() {
        return nip;
    }

    public void setNip(int nip) {
        this.nip = nip;
    }
}
