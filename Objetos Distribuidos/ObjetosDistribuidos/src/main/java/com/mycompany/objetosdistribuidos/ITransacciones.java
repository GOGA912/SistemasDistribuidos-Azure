package com.mycompany.objetosdistribuidos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITransacciones extends Remote {
    String depositar(String cuenta, double monto) throws RemoteException;
    String retirar(String cuenta, double monto) throws RemoteException;
    String transferir(String origen, String destino, double monto) throws RemoteException;
}