package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGraphRMI extends Remote{

    public String getName() throws RemoteException;

    public String processBatch(String batch , String algoritm) throws RemoteException;

    public int getInitialSize()throws RemoteException;
}
