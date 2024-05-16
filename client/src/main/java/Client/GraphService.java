package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GraphService extends Remote{

    public String getName() throws RemoteException;

    public String executeBatch(String batch , String algorithm) throws RemoteException;

    public int getInitialSize()throws RemoteException;
}
