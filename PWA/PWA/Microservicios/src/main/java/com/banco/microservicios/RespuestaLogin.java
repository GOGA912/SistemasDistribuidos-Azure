package com.banco.microservicios;

public class RespuestaLogin {
    private String mensaje;
    private String nombre;
    private String sexo;

    // Constructor solo con mensaje
    public RespuestaLogin(String mensaje) {
        this.mensaje = mensaje;
    }

    // Constructor completo
    public RespuestaLogin(String mensaje, String nombre, String sexo) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.sexo = sexo;
    }

    // Getters
    public String getMensaje() {
        return mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSexo() {
        return sexo;
    }
}
