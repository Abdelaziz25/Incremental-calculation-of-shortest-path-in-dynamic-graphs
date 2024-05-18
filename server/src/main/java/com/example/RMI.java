package com.example;


import java.rmi.RemoteException;

public class RMI implements GraphService {
    private final GraphInterface graph;

    public RMI() {
        this.graph = new Graph("src/main/resources/graph.txt");
    }

    @Override
    public synchronized String getName() throws RemoteException {
        return "RMI Server Test";
    }

    @Override
    public synchronized String processBatch(String batch, String algorithm) throws RemoteException {
        StringBuilder result = new StringBuilder();
        String[] batchQueries = batch.split("\n");

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
                default -> result.append(calculateShortestPath(u, v, algorithm)).append("\n");
            }
        }

        return result.toString();
    }

    private void addEdge(int u, int v) {
        graph.addEdge(u, v);
    }

    private void deleteEdge(int u, int v) {
        graph.deleteEdge(u, v);
    }

    private int calculateShortestPath(int u, int v, String algorithm) {
        return graph.shortestPath(u, v, algorithm);
    }

    @Override
    public synchronized int getInitialSize() throws RemoteException {
        return graph.getGraphSize();
    }
}
