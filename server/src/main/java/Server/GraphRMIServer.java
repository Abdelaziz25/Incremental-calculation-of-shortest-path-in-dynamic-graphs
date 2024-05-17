package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;

public class GraphRMIServer implements GraphService {
    private final IGraph graph;
    private final static Logger logger = LogManager.getLogger(GraphRMIServer.class);


    public GraphRMIServer(){
        this.graph = new Graph("src/main/resources/graph.txt");
    }

    @Override
    public synchronized String getName() throws RemoteException {
        return "RMI Server Test";
    }

    private void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public synchronized String executeBatch(String batch, String algorithm) throws RemoteException {
        logInfo("Starting batch processing");
        long RequestStartTime = System.currentTimeMillis();

        String[] batchQueries = batch.split("\n");
        StringBuilder result = new StringBuilder();

        for (String query : batchQueries) {
            String[] operation = query.split(" ");
            char type = operation[0].charAt(0);
            if (type == 'F')
                break;

            int u = Integer.parseInt(operation[1]);
            int v = Integer.parseInt(operation[2]);


            switch (type) {
                case 'A' -> addEdge(u, v);
                case 'D' -> deleteEdge(u, v);
                default -> {
                    int shortestPath = calculateShortestPath(u, v, algorithm);
                    result.append(shortestPath).append("\n");
                }
            }
        }
        long RequestEndTime = System.currentTimeMillis();
        logInfo("Finished batch processing in " + (RequestEndTime - RequestStartTime) + " ms.");
        return result.toString();
    }

    private void addEdge(int u, int v) {
        graph.add(u, v);
        logInfo("Added edge from " + u + " to " + v);

    }

    private void deleteEdge(int u, int v) {
        graph.delete(u, v);
        logInfo("Deleted edge from " + u + " to " + v);
    }

    private int calculateShortestPath(int u, int v, String algorithm) {
        int shortestPath = graph.shortestPath(u, v, algorithm);
        logInfo("Shortest path between " + u + " and " + v + " using " + algorithm + " is: " + shortestPath);
        return shortestPath;
    }


    @Override
    public synchronized int getInitialSize() throws RemoteException {
        logInfo("Graph initial size have been requested: " + graph.getGraphInitialSize());
        return graph.getGraphInitialSize();
    }
}
