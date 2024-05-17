package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GraphService extends Remote{

    public String getName() throws RemoteException;

    public String processBatch(String batch , String algorithm) throws RemoteException;

    public int getInitialSize()throws RemoteException;
}
