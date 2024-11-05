package com.projectj.finaly.thanhnd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        Map<String, List<String>> graph = readGraphFromFile("input.txt", trangThai);
        String startState = String.valueOf(trangThai.get("TTD"));
        String goalState = String.valueOf(trangThai.get("TTKT"));
        List<String> duongDi = new ArrayList<>();
        List<String[]> result = dfs(graph, startState, goalState, duongDi);
        writeOutputToFile(duongDi, result, "output.txt");
    }

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

    public static List<String[]> dfs(Map<String, List<String>> graph, String startState, String goalState, List<String> duongDi) {
        List<String[]> log = new ArrayList<>();
        Stack<List<String>> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        stack.push(Collections.singletonList(startState));
        while (!stack.isEmpty()) {
            List<String> path = stack.pop();
            String currentNode = path.getLast();
            visited.add(currentNode);
            List<String> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
            if (currentNode.equals(goalState)) {
                duongDi.addAll(path);
                String[] logEntry = {
                        currentNode,
                        "",
                        "",
                       "TTHT"
                };
                log.add(logEntry);

                break;
            }
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    stack.push(newPath);
                }
            }
            List<String> dinh = List.of(stack.toString().split(",")); // cái này để log ra thooi
            List<String> danhSach = new ArrayList<>();
            for (String x : dinh) {
                String value = x.replace("[", "").replace("]", "").trim();
                if (!visited.contains(value)) {
                    danhSach.add(value);
                }
            }
            String[] logEntry = {
                    currentNode,
                    neighbors.toString(),
                    visited.toString(),
                    String.join(", ", danhSach)
            };
            log.add(logEntry);
        }

        return log;
    }

    public static void writeOutputToFile(List<String> result, List<String[]> log, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        String format = "| %-23s | %-25s | %-25s | %-23s |";
        writer.write("+-------------------------+-------------------------+-------------------------+-------------------------+\n");
        writer.write(String.format("| %-23s | %-23s | %-23s | %-23s |", "Trạng thái phát triển", "Trạng thái liền kề", "Danh sách Q", "Danh sách L") + "\n");
        for (String[] entry : log) {
            writer.write("+-------------------------+-------------------------+-------------------------+-------------------------+\n");
            writer.write(String.format(format, entry[0].trim(), entry[1].trim(), entry[2].trim(), entry[3].trim()).replace("[", "").replace("]", "") + "\n");
        }
        writer.write("+-------------------------+-------------------------+-------------------------+-------------------------+\n");
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