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
    
    public synchronized String getTitular(){
        return titular;
    }

    public synchronized String getNumeroCuenta(){
        return numeroCuenta;
    }
    
    public synchronized double consultarSaldo(){
        return saldo;
    }
    
    public synchronized void depositar(double monto){
        if(monto > 0){
            saldo += monto;
        }
    }
    
    public synchronized void retirar(double monto){
        if(monto < saldo){
            saldo -= monto;
        }
    }
    
    public synchronized boolean autenticar(int claveIngresada) {
        return nip==claveIngresada;
    }

    public synchronized boolean transferir(CuentaBancaria cuentaD, double monto) {
        if(monto > 0 && monto <= saldo){
            this.saldo -= monto;
            cuentaD.depositar(monto);
            return true;
        }
        return false;
    }
}
