package com.example;

public interface GraphInterface {

    void addEdge(int u, int v);

    void deleteEdge(int u, int v);

    int shortestPath(int u, int v, String algorithm);

    int getGraphSize();
}
