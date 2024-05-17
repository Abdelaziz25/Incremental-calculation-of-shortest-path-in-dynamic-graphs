package com.example;

import java.util.Random;

public class GraphBatchGenerator {

    private static final String[] OPERATIONS = {"A", "D", "Q"};
    private int graphSize;

    public GraphBatchGenerator(int initialSize) {
        this.graphSize = initialSize;
    }

    public static void main(String[] args) {
        String directoryPath = "src/main/java/Client/Batches/";
        GraphBatchGenerator generator = new GraphBatchGenerator(100);
        generator.generateBatch(5, 50);
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
    public String getLog(Long responseTime, String response , String request, String algorithmType , int updataPercentage , int batchSize , long ThreadID){
        return "threat ID = " +ThreadID +"\n"+
                "response time = "+responseTime+"\n"+
                "algorithm Type = "+ algorithmType+"\n"+
                "updata Percentage = "+ updataPercentage+"\n"+
                "batchSize = "+ batchSize+"\n"+
                "request = \n"+ request+"\n"+
                "response = \n"+ response+"\n"+
                "----------------------------\n";
    }
}
