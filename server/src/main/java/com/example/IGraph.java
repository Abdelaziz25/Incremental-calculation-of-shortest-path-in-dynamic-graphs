package com.example;


public interface IGraph {

    public void add(int u, int v);

    public void delete(int u, int v) ;
    
    public int shortestPath(int u, int v , String algorithm);

    int getGraphInitialSize();
}
