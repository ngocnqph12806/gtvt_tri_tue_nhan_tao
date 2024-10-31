package com.projectj.finaly.ngocnq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<String, List<String>> graph;
    
    public Graph() {
        graph = new HashMap<>();
    }
    
    public void addEdge(String fromNode, String toNode) {
        graph.putIfAbsent(fromNode, new ArrayList<>());
        graph.get(fromNode).add(toNode);
    }
    
    public List<String> getNeighbors(String node) {
        return graph.getOrDefault(node, new ArrayList<>());
    }
}