package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Random;

public class ClientThread extends Thread {
   private static final int BATCH_SIZE = 10;
   private static final int SLEEP_TIME = 1000;
   private static final String ALGORITHM_USED = "BFS";
   private static final Logger logger = LogManager.getLogger(ClientThread.class);
   private final Map<String, String> configs;

   public ClientThread(Map<String, String> configs) {
      this.configs = configs;
   }

   @Override
   public void run() {
      try {
         ThreadContext.put("threadName", Thread.currentThread().getName());
         logger.info("Client ID: {}", Thread.currentThread().getId());

         Registry registry = LocateRegistry.getRegistry(configs.get("serverHost"), Integer.parseInt(configs.get("serverPort")));
         GraphService graphService = (GraphService) registry.lookup("graphService");
         logger.info("Graph service found");

         String graphName = graphService.getName();
         logger.info("Graph Name: {}", graphName);

         int graphInitialSize = graphService.getInitialSize();
         logger.info("Graph Initial Size = {}", graphInitialSize);

         long totalResponseTime = 0;

         for (int i = 0; i < 5; ++i) {
            BatchGenerator batch = new BatchGenerator(graphInitialSize);
            Random rand = new Random();
            int updatePercentage = rand.nextInt(100);
            String requestBatch = batch.generateBatch(BATCH_SIZE, updatePercentage);
            long startTime = System.currentTimeMillis();
            String batchResult = graphService.processBatch(requestBatch, ALGORITHM_USED);
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            totalResponseTime += responseTime;
            String log = batch.formatLog(responseTime, batchResult, requestBatch, ALGORITHM_USED, updatePercentage, BATCH_SIZE, Thread.currentThread().getId());
            logger.info(log);
            int sleepTime = rand.nextInt(SLEEP_TIME);
            Thread.sleep(sleepTime);
         }
         logger.info("Total Response Time for Client {}: {}", Thread.currentThread().getId(), totalResponseTime);

      } catch (Exception e) {
         logger.error("Error occurred: {}", e.getMessage());
      }
   }
}