package Server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ServerInitializer{
    private final static Logger logger = LogManager.getLogger(ServerInitializer.class);

    public static void main(String[] args) {
        new ServerInitializer().startServer();
    }

    public void startServer() {
        try {
            logMessage("Server is starting up...");
            configureServer();
            // TODO:: Replace DummyGraph with the actual graph implementation
            IGraphRMI graphService = new GraphRMIServer(new DummyGraph());

            IGraphRMI stub = (IGraphRMI) UnicastRemoteObject.exportObject(graphService, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("graphService", stub);

            logMessage("Server is ready and running.");
        } catch (Exception e) {
            logger.error("An error occurred while starting the server: ", e);
        }
    }

    private void configureServer() {
        System.setProperty("java.rmi.server.hostname", "localhost");
    }

    private void logMessage(String message) {
        System.out.println(message);
        logger.info(message);
    }
}
