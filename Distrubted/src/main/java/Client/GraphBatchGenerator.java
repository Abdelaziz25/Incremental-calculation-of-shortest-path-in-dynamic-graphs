package Client;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GraphBatchGenerator {

    private static final String[] OPERATIONS = {"Q", "A", "D"};
    private static final int MAX_NODES = 20;
    private static final int OPERATIONS_PER_BATCH = 5;

    public static void main(String[] args) {
        String directoryPath = "src/main/java/Client/Batches/";
        generateBatches(directoryPath);
    }

    public static void generateBatches(String directoryPath) {
        try {
            Random random = new Random();
            int batchNumber = 1;
            while (true) {
                String batch = generateBatch(random);
                batch += "F\n"; // Append 'F' to mark the end of batch
                String filePath = directoryPath + "batch_" + batchNumber + ".txt";
                writeBatchToFile(filePath, batch);
                Thread.sleep(1000); // Wait for 1 second
                batchNumber++;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String generateBatch(Random random) {
        StringBuilder batch = new StringBuilder();
        for (int i = 0; i < OPERATIONS_PER_BATCH; i++) {
            String operation = OPERATIONS[random.nextInt(OPERATIONS.length)];
            int node1 = random.nextInt(MAX_NODES) + 1;
            int node2 = random.nextInt(MAX_NODES) + 1;
            batch.append(operation).append(" ").append(node1).append(" ").append(node2).append("\n");
        }
        return batch.toString();
    }

    private static void writeBatchToFile(String filePath, String batch) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(batch);
        }
    }
}
