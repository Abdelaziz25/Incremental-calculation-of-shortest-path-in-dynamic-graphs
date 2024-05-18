package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting application...");
        try {
            Map<String, String> configs = ConfigurationReader.readConfigurations("application.properties");
            startClients(configs);
        } catch (IOException | InterruptedException e) {
            logger.error("Error occurred: {}", e.getMessage());
        }
    }

    private static void startClients(Map<String, String> configs) throws InterruptedException {
        int numberOfClients = Integer.parseInt(configs.get("numberOfNodes"));
        for (int i = 0; i < numberOfClients; ++i) {
            new ClientThread(configs).start();
            Thread.sleep(10);
        }
    }
}