package com.example;

import java.util.Random;

public class GraphBatchGenerator {
    private static final String[] OPERATIONS = {"A", "D", "Q"};
    private final int graphSize;

    public GraphBatchGenerator(int initialSize) {
        this.graphSize = initialSize;
    }

    public String generateBatch(int batchSize, int updateRatio) {
        StringBuilder batch = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < batchSize; i++) {
            int operationType = rand.nextInt(100);
            String operation = OPERATIONS[operationType < updateRatio ? rand.nextInt(2) : 2];
            int node1 = rand.nextInt(graphSize) + 1;
            int node2 = rand.nextInt(graphSize) + 1;
            batch.append(operation).append(" ").append(node1).append(" ").append(node2).append("\n");
        }
        batch.append("F\n"); // Append 'F' to mark the end of batch
        return batch.toString();
    }

    public String formatLog(long responseTime, String response, String request, String algorithmType, int updatePercentage, int batchSize, long threadID) {
        return "Thread ID: " + threadID + "\n" +
                "Response Time: " + responseTime + "\n" +
                "Algorithm Type: " + algorithmType + "\n" +
                "Update Percentage: " + updatePercentage + "\n" +
                "Batch Size: " + batchSize + "\n" +
                "Request: \n" + request + "\n" +
                "Response: \n" + response + "\n" +
                "----------------------------\n";
    }
}