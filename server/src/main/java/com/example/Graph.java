package com.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Graph implements GraphInterface {
    private HashMap<Integer, HashSet<Integer>> adjacencyMap;
    private HashMap<Integer, HashSet<Integer>> reversedAdjacencyMap;
    private int graphSize = 0;

    public Graph(String filePath) {
        adjacencyMap = new HashMap<>();
        reversedAdjacencyMap = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) == 'S') break;
                String[] edge = line.split(" ");
                int u = Integer.parseInt(edge[0]), v = Integer.parseInt(edge[1]);
                adjacencyMap.computeIfAbsent(u, k -> new HashSet<>()).add(v);
                reversedAdjacencyMap.computeIfAbsent(v, k -> new HashSet<>()).add(u);
                graphSize = Math.max(graphSize, Math.max(u, v));
            }

            System.out.println("Graph created with initial size: " + graphSize);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void addEdge(int u, int v) {
        adjacencyMap.computeIfAbsent(u, k -> new HashSet<>()).add(v);
        reversedAdjacencyMap.computeIfAbsent(v, k -> new HashSet<>()).add(u);
    }

    @Override
    public void deleteEdge(int u, int v) {
        adjacencyMap.getOrDefault(u, new HashSet<>()).remove(v);
        reversedAdjacencyMap.getOrDefault(v, new HashSet<>()).remove(u);
    }

    @Override
    public int shortestPath(int u, int v, String algorithm) {
        if ("BFS".equals(algorithm)) {
            return breadthFirstSearch(u, v);
        } else {
            return bidirectionalBFS(u, v);
        }
    }

    private int breadthFirstSearch(int u, int v) {
        if (u == v) return 0;

        HashMap<Integer, Integer> visited = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.put(u, 0);
        queue.add(u);

        while (!queue.isEmpty()) {
            int current = queue.remove();
            if (adjacencyMap.containsKey(current)) {
                for (int neighbor : adjacencyMap.get(current)) {
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
            Integer neighbor1 = getNeighbor(visitedForward, visitedBackward, queueForward, adjacencyMap);
            if (neighbor1 != null) return neighbor1;

            Integer neighbor = getNeighbor(visitedBackward, visitedForward, queueBackward, reversedAdjacencyMap);
            if (neighbor != null) return neighbor;
        }

        return -1;
    }

    private Integer getNeighbor(HashMap<Integer, Integer> visitedForward, HashMap<Integer, Integer> visitedBackward, Queue<Integer> queueForward, HashMap<Integer, HashSet<Integer>> graph) {
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
    public int getGraphSize() {
        return graphSize;
    }
}
