package com.projectj.ngocnq;

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
        // nếu đồ thị chưa có trạng thái phát triển thì sẽ tạo list rỗng và add node mới vào (toNode)
        graph.putIfAbsent(fromNode, new ArrayList<>());
        graph.get(fromNode).add(toNode);
    }
    
    public List<String> getNeighbors(String node) {
        return graph.getOrDefault(node, new ArrayList<>());
    }
}