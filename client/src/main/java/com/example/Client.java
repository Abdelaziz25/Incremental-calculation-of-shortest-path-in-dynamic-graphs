package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.LogManager;

import static java.lang.Math.pow;

public class Client extends Thread{
   private final int BATCH_SIZE = 10;
   private final int SLEEP_TIME = 1000;
   private final String ALGORITHM_USED = "BFS";
   private final Logger logger = LogManager.getLogger(Client.class);

   private final Map<String, String> configs;

   public Client(Map<String, String> configs) {
      this.configs = configs;
   }
   
   @Override
   public void run() {
      
      try {
         ThreadContext.put("threadName", Thread.currentThread().getName());
         System.err.println("ClientID: "+ Thread.currentThread().getId());

         Registry registry = LocateRegistry.getRegistry(configs.get("serverHost"), Integer.parseInt(configs.get("serverPort")));
         // lookup the graph object
         GraphService graphService = (GraphService) registry.lookup("graphService");
//         System.err.println("graphService found");

         String graphName = graphService.getName();
//         System.out.println("Name:" + graphName);

         int graphInitialSize = graphService.getInitialSize();
         logger.info("Graph Initial Size = " + graphInitialSize);
         logger.info("Graph Initial Name = " + graphName);


         for(int i=1;i<5;++i){
            long totalResponseTime = 0;
            GraphBatchGenerator batch = new GraphBatchGenerator(graphInitialSize);

            Random rand = new Random();
            int updatePercentage = 50;
            int batch_size = (int) pow(10,i);
            String requestBatch = batch.generateBatch(batch_size,updatePercentage);
            String batchResult = null;
            for(int j = 0; j < 5; j++){
               long startTime = System.currentTimeMillis();
               batchResult = graphService.processBatch(requestBatch,ALGORITHM_USED );
               long endTime = System.currentTimeMillis();
               long responseTime = endTime - startTime;
               totalResponseTime += responseTime;
            }

//            String log = batch.getLog(responseTime , batchResult ,requestBatch, ALGORITHM_USED , updatePercentage, BATCH_SIZE, Thread.currentThread().getId());
//            String log = batch.getLog(totalResponseTime/5 , batchResult ,requestBatch, ALGORITHM_USED , updatePercentage, BATCH_SIZE, Thread.currentThread().getId());
            logger.info(pow(10,i) + "size : "+totalResponseTime/5);

            int sleepTime = rand.nextInt(SLEEP_TIME);
            // int sleepTime = SLEEP_TIME;
            Thread.sleep(sleepTime);
         }
//         logger.info("total Response time for Client " + totalResponseTime);

      } catch (Exception e) {
         logger.error(e);
      }
   }
}
