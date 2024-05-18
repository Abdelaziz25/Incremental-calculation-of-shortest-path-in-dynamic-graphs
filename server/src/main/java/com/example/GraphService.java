package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GraphService extends Remote {

    String getName() throws RemoteException;

    String processBatch(String batch, String algorithm) throws RemoteException;

    int getInitialSize() throws RemoteException;
}
