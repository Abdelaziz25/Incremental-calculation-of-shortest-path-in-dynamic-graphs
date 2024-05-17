package com.example;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class Graph implements IGraph{
//    private static Logger logger = LogManager.getLogger(DynamicGraph.class);
    private HashMap<Integer, HashSet<Integer>> graphMap;
    private HashMap<Integer, HashSet<Integer>> reversedGraphMap;
    private int graphInitialSize = 0;

    public Graph(String filePath){
        graphMap = new HashMap<>();
        reversedGraphMap = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) == 'S') break;
                String[] edge = line.split(" ");
                int u = Integer.parseInt(edge[0]), v = Integer.parseInt(edge[1]);
                graphMap.computeIfAbsent(u, k -> new HashSet<>()).add(v);
                reversedGraphMap.computeIfAbsent(v, k -> new HashSet<>()).add(u);
                graphInitialSize = Math.max(graphInitialSize, Math.max(u, v));
            }

//            logger.info("Graph created with initial size: " + graphInitialSize);
            System.out.println("Graph created with initial size: " + graphInitialSize);
        } catch (IOException e) {
//            logger.error("Error reading file: " + e.getMessage());
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void add(int u, int v) {
        graphMap.computeIfAbsent(u, k -> new HashSet<>()).add(v);
        reversedGraphMap.computeIfAbsent(v, k -> new HashSet<>()).add(u);
    }

    @Override
    public void delete(int u, int v) {
        graphMap.getOrDefault(u, new HashSet<>()).remove(v);
        reversedGraphMap.getOrDefault(v, new HashSet<>()).remove(u);
    }

    @Override
    public int shortestPath(int u, int v, String algorithm) {
        if ("BFS".equals(algorithm)) {
            return BFS(u, v);
        } else {
            return bidirectionalBFS(u, v);
        }
    }


    private int BFS(int u, int v) {
        if (u == v) return 0;

        HashMap<Integer, Integer> visited = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.put(u, 0);
        queue.add(u);

        while (!queue.isEmpty()) {
            int current = queue.remove();
            if (graphMap.containsKey(current)) {
                for (int neighbor : graphMap.get(current)) {
                    if (!visited.containsKey(neighbor)) {
                        if (neighbor == v)
                            return visited.get(current) + 1;

                        visited.put(neighbor, visited.get(current) + 1);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return -1;
    }

    private int bidirectionalBFS(int u, int v) {
        if (u == v) return 0;

        HashMap<Integer, Integer> visitedForward = new HashMap<>();
        HashMap<Integer, Integer> visitedBackward = new HashMap<>();
        Queue<Integer> queueForward = new LinkedList<>();
        Queue<Integer> queueBackward = new LinkedList<>();

        visitedForward.put(u, 0);
        visitedBackward.put(v, 0);
        queueForward.add(u);
        queueBackward.add(v);

        while (!queueForward.isEmpty() && !queueBackward.isEmpty()) {
            // Forward BFS
            Integer neighbor1 = getInteger(visitedForward, visitedBackward, queueForward, graphMap);
            if (neighbor1 != null) return neighbor1;

            // Backward BFS
            Integer neighbor = getInteger(visitedBackward, visitedForward, queueBackward, reversedGraphMap);
            if (neighbor != null) return neighbor;
        }

        return -1;
    }

    private Integer getInteger(HashMap<Integer, Integer> visitedForward, HashMap<Integer, Integer> visitedBackward, Queue<Integer> queueForward, HashMap<Integer, HashSet<Integer>> graph) {
        int currentForward = queueForward.remove();
        if (graph.containsKey(currentForward)) {
            for (int neighbor : graph.get(currentForward)) {
                if (!visitedForward.containsKey(neighbor)) {
                    visitedForward.put(neighbor, visitedForward.get(currentForward) + 1);
                    queueForward.add(neighbor);
                }
                if (visitedBackward.containsKey(neighbor)) {
                    return visitedForward.get(neighbor) + visitedBackward.get(neighbor);
                }
            }
        }
        return null;
    }

    @Override
    public int getGraphInitialSize() {
        return graphInitialSize;
    }
}
