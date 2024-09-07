package com.projectj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DFSAlgorithm {


    public static void main(String[] args) throws IOException {
        Map<String, String> trangThai = new HashMap<>();
        // Đọc đồ thị từ file
        Map<String, List<String>> graph = readGraphFromFile("input.txt", trangThai);
        String startState = String.valueOf(trangThai.get("TTD"));  // Trạng thái đầu
        String goalState = String.valueOf(trangThai.get("TTKT"));   // Trạng thái kết thúc
        // Thực hiện thuật toán DFS

        List<String> result = dfs(graph, startState, goalState);

        // Ghi kết quả ra file output
        writeOutputToFile(result, "output.txt");
    }

    // Hàm đọc đồ thị từ file
    public static Map<String, List<String>> readGraphFromFile(String filename, Map<String, String> tt) throws IOException {
        Map<String, List<String>> graph = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int j = 0;
        while ((line = reader.readLine()) != null) {
            line = line.toUpperCase().replace("  ", " ");
            String[] parts = line.split(" ");
            if (j <= 1) {
                if (parts.length > 1) {
                    tt.put(parts[0], parts[1]);
                }
            } else {
                String node = parts[0];
                List<String> neighbors = new ArrayList<>();
                for (int i = 1; i < parts.length; i++) {
                    neighbors.add(parts[i]);
                }
                graph.put(node, neighbors);
            }
            j++;
        }
        reader.close();
        return graph;
    }

    // Thuật toán DFS
    public static List<String> dfs(Map<String, List<String>> graph, String startState, String goalState) {
        List<String> result = new ArrayList<>();
        Stack<List<String>> stack = new Stack<>();
        Set<String> visited = new HashSet<>(); // Đánh dấu đã thăm

        // Khởi tạo ngăn xếp với trạng thái đầu
        stack.push(Collections.singletonList(startState));

        while (!stack.isEmpty()) {
            List<String> path = stack.pop();
            String currentNode = path.get(path.size() - 1);

            // Kiểm tra nếu đã đến trạng thái kết thúc
            if (currentNode.equals(goalState)) {
                result.addAll(path);
                return result;
            }

            // Đánh dấu đã thăm
            if (!visited.contains(currentNode)) {
                visited.add(currentNode);

                // Thêm hàng xóm của nút hiện tại vào ngăn xếp
                List<String> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
                for (String neighbor : neighbors) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    stack.push(newPath);
                }
            }
        }

        return result;  // Trả về danh sách rỗng nếu không tìm thấy đường đi
    }

    // Ghi kết quả ra file output
    public static void writeOutputToFile(List<String> result, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        if (!result.isEmpty()) {
            writer.write("Các bước thực hiện thuật toán DFS:\n");
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