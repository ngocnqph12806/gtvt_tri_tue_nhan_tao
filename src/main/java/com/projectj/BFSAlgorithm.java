package com.projectj;

import java.io.*;
import java.util.*;

public class BFSAlgorithm {
    public static void main(String[] args) throws IOException {
        String startState = "A";  // Trạng thái đầu
        String goalState = "G";   // Trạng thái kết thúc

        // Đọc đồ thị từ file
        Map<String, List<String>> graph = readGraphFromFile("input.txt");

        // Thực hiện thuật toán BFS
        List<String> result = bfs(graph, startState, goalState);

        // Ghi kết quả ra file output
        writeOutputToFile(result, "output.txt");
    }

    // Hàm đọc đồ thị từ file
    public static Map<String, List<String>> readGraphFromFile(String filename) throws IOException {
        Map<String, List<String>> graph = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            String node = parts[0];
            List<String> neighbors = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                neighbors.add(parts[i]);
            }
            graph.put(node, neighbors);
        }
        reader.close();
        return graph;
    }

    // Thuật toán BFS
    public static List<String> bfs(Map<String, List<String>> graph, String startState, String goalState) {
        List<String> result = new ArrayList<>();
        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Khởi tạo hàng đợi với trạng thái đầu
        queue.add(Arrays.asList(startState));

        while (!queue.isEmpty()) {
            List<String> path = queue.poll();
            String currentNode = path.get(path.size() - 1);

            // Kiểm tra nếu đã đến trạng thái kết thúc
            if (currentNode.equals(goalState)) {
                result.addAll(path);
                return result;
            }

            // Đánh dấu đã thăm
            if (!visited.contains(currentNode)) {
                visited.add(currentNode);

                // Thêm hàng xóm của nút hiện tại vào hàng đợi
                List<String> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
                for (String neighbor : neighbors) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }

        return result;  // Trả về danh sách rỗng nếu không tìm thấy đường đi
    }

    // Ghi kết quả ra file output
    public static void writeOutputToFile(List<String> result, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        if (!result.isEmpty()) {
            writer.write("Các bước thực hiện thuật toán BFS:\n");
            for (int i = 0; i < result.size() - 1; i++) {
                writer.write(result.get(i) + " -> ");
            }
            writer.write(result.get(result.size() - 1) + "\n");
        } else {
            writer.write("Không tìm thấy đường đi từ trạng thái đầu đến trạng thái kết thúc.\n");
        }
        writer.close();
    }
}
