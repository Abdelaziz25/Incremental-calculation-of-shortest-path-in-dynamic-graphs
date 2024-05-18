package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting application...");
        try {
            Map<String, String> configs = readConfigurations("application.properties");
            startClientThreads(configs);
        } catch (IOException | InterruptedException e) {
            logger.error("Error occurred: {}", e.getMessage());
        }
    }

    private static Map<String, String> readConfigurations(String configFile) throws IOException {
        Map<String, String> configMap = new HashMap<>();
        Properties prop = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IOException("Unable to find configuration file: " + configFile);
            }
            prop.load(input);
            configMap.put("serverHost", prop.getProperty("GSP.server"));
            configMap.put("serverPort", prop.getProperty("GSP.server.port"));
            configMap.put("numberOfNodes", prop.getProperty("GSP.numberOfnodes"));
            configMap.put("node0", prop.getProperty("GSP.node0"));
            configMap.put("node1", prop.getProperty("GSP.node1"));
            configMap.put("node2", prop.getProperty("GSP.node2"));
            configMap.put("node3", prop.getProperty("GSP.node3"));
            configMap.put("rmiRegistryPort", prop.getProperty("GSP.rmiregistry.port"));
        }
        return configMap;
    }

    private static void startClientThreads(Map<String, String> configs) throws InterruptedException {
        int numberOfClients = Integer.parseInt(configs.get("numberOfNodes")) - 1;
        for (int i = 0; i < numberOfClients; ++i) {
            new Client(configs).start();
            Thread.sleep(10);
        }
    }
}