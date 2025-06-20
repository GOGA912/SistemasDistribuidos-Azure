package com.mycompany.objetosdistribuidos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAutenticacion extends Remote {
    String autenticar(String cuenta, int nip) throws RemoteException;
}

