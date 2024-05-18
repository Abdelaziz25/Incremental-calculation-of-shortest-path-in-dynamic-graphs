package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationReader {
    public static Map<String, String> readConfigurations(String configFile) throws IOException {
        Map<String, String> configMap = new HashMap<>();
        Properties prop = new Properties();
        try (InputStream input = ConfigurationReader.class.getClassLoader().getResourceAsStream(configFile)) {
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
}
