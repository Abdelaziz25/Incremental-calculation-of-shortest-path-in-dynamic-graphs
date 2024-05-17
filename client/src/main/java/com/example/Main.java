package com.example;

import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);

    public static Map<String, String> readConfigurations(String configFile) {
            Map<String, String> configMap = new HashMap<>();
            Properties prop = new Properties();
            InputStream input = null;

            try {
                input = Main.class.getClassLoader().getResourceAsStream(configFile);
                prop.load(input);

                // Get values and store in map
                configMap.put("serverHost", prop.getProperty("GSP.server"));
                configMap.put("serverPort", prop.getProperty("GSP.server.port"));
                configMap.put("numberOfNodes", prop.getProperty("GSP.numberOfnodes"));
                configMap.put("node0", prop.getProperty("GSP.node0"));
                configMap.put("node1", prop.getProperty("GSP.node1"));
                configMap.put("node2", prop.getProperty("GSP.node2"));
                configMap.put("node3", prop.getProperty("GSP.node3"));
                configMap.put("rmiRegistryPort", prop.getProperty("GSP.rmiregistry.port"));
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return configMap;
        }
    public static void main(String[] args) {
//        logger.info("Starting.....");
        try {
            Map<String, String> configs = readConfigurations("application.properties");
            ArrayList<Client> clientsThreads = new ArrayList<>();
            
//            int numberOfClients = Integer.parseInt(configs.get("numberOfNodes")) ;

            for (int j=0;j<15;j++){
                for(int i=0;i<= j;i++){
                    clientsThreads.add(new Client(configs,j+1));
                    clientsThreads.get((j*(j+1))/2+i).start();
                    Thread.sleep(10);
                }
                for (int i = 0; i < j; i++) {
                    clientsThreads.get(((j*(j+1))/2) + i).join();
                }
            }

            
        } catch (Exception e) {
            System.err.println("GraphService exception");
            logger.error(e.getMessage());
        }
    }
}
