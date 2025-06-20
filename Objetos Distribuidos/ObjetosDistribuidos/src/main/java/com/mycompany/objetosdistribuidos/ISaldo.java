package com.mycompany.objetosdistribuidos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISaldo extends Remote {
    double consultarSaldo(String cuenta) throws RemoteException;
    String actualizarSaldo(String cuenta, double nuevoSaldo) throws RemoteException;
}
