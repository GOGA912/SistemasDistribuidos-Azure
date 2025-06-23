package com.banco.microservicios.loginService;

public class Titular {
    private String nombre;
    private String sexo;

    public Titular(String nombre, String sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
    }
    public String getNombre() {
        return nombre;
    }
    public String getSexo() {
        return sexo;
    }
}

