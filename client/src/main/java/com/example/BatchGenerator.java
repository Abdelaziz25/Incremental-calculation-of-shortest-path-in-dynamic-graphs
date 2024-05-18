package com.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class BatchGenerator {
    private static final String[] OPERATIONS = {"A", "D", "Q"};
    private final int graphSize;

    public BatchGenerator(int initialSize) {
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
    public String generateBatch(String filePath) {
        StringBuilder batch = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) == 'F') {
                    batch.append("F\n"); // Append 'F' to mark the end of batch
                    break;
                }
                String[] edge = line.split(" ");
                if (edge[0].equals("Q")) {
                    batch.append("Q").append(" ").append(Integer.parseInt(edge[1])).append(" ").append(Integer.parseInt(edge[2])).append("\n");
                }
                else if (edge[0].equals("A")) {
                    batch.append("A").append(" ").append(Integer.parseInt(edge[1])).append(" ").append(Integer.parseInt(edge[2])).append("\n");
                }
                else {
                    batch.append("D").append(" ").append(Integer.parseInt(edge[1])).append(" ").append(Integer.parseInt(edge[2])).append("\n");
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return batch.toString();
    }

    public String formatLog(long responseTime, String response, String request, String algorithmType, int updatePercentage, int batchSize, long threadID) {
        return "Thread ID: " + threadID + "\n" +
                "Response Time: " + responseTime + "\n" +
                "Algorithm Type: " + algorithmType + "\n" +
                "Update Percentage: " + updatePercentage + "\n" +
                "Batch Size: " + batchSize + "\n" +
                "Request: \n" + request + "\n" +
                "Response: \n" + response;
    }
}