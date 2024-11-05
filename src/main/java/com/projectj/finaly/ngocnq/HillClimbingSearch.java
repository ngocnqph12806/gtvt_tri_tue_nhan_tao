package com.projectj.finaly.ngocnq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HillClimbingSearch {
    public static List<SearchState> hillClimbingSearch(String startNode, String endNode, Graph graph, Map<String, String> stringStringMap) {
        List<SearchState> results = new ArrayList<>();
        LinkedList<String> lstL2 = new LinkedList<>();
        String currentState = startNode;

        while (!endNode.equals(currentState)) {
            List<String> neighbors = graph.getNeighbors(currentState);
            String neighborStr = String.join(",", neighbors);
            stringStringMap.putIfAbsent(currentState, neighborStr);
            neighbors.sort((s1, s2) -> {
                String[] newS1s = s1.split("-");
                String news1 = newS1s[newS1s.length - 1];
                String[] newS2s = s2.split("-");
                String news2 = newS2s[newS2s.length - 1];
                int num1 = Integer.parseInt(news1.replaceAll("\\D+", ""));
                int num2 = Integer.parseInt(news2.replaceAll("\\D+", ""));
                if (num1 == num2) {
                    return newS1s[0].compareTo(newS2s[0]);
                } else {
                    return Integer.compare(num1, num2);
                }
            });
            if (!neighborStr.isEmpty()) {
                LinkedList<String> stringList = new LinkedList<>(neighbors);
                stringList.addAll(lstL2);
                lstL2 = new LinkedList<>(stringList);
            }
            String l1Str = String.join(",", neighbors);
            String l2Str = String.join(",", lstL2);
            results.add(new SearchState(currentState, neighborStr, l1Str, l2Str));
            if (lstL2.isEmpty()) {
                break;
            }
            currentState = lstL2.removeFirst();
            if (endNode.equals(currentState)) {
                results.add(new SearchState(endNode, "TTKT-DUNG", "", ""));
            }
        }
        return results;
    }

    public static void main(String[] args) {
        String start = "";
        String end = "";
        Graph g = new Graph();
        try (BufferedReader br = new BufferedReader(new FileReader("graph-inputHillClimbing.txt"))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String source = parts[0].trim();
                String destination = parts.length > 1 ? parts[1].trim() : "";
                if (i == 0) {
                    start = source;
                    end = destination;
                } else {
                    g.addEdge(source, destination);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> stringStringMap = new HashMap<>();
        List<SearchState> results = hillClimbingSearch(start, end, g, stringStringMap);
        printResult(results, start, end, stringStringMap);
    }

    private static void printResult(List<SearchState> results, String start, String end, Map<String, String> stringStringMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nQuá trình tìm kiếm theo PP leo đồi");
        stringBuilder.append("\n+-----------+---------------+---------------+------------------------+");
        stringBuilder.append("\n| Phát      | Trạng thái    | Danh sách L1  | Danh sách L2           |");
        stringBuilder.append("\n| triển TT  | kế            |               |                        |");
        stringBuilder.append("\n+-----------+---------------+---------------+------------------------+");

        for (SearchState result : results) {
            stringBuilder.append(String.format("\n| %-9s | %-13s | %-13s | %-22s |",
                    result.state,
                    result.nextStates,
                    result.listL1,
                    result.listL));
            stringBuilder.append("\n+-----------+---------------+---------------+------------------------+");
        }
        String slowPath = end;
        StringBuilder fastestPath = new StringBuilder(end);
        while (slowPath != start) {
            for (Map.Entry<String, String> x: stringStringMap.entrySet()) {
                if (x.getValue().contains(slowPath)) {
                    slowPath = x.getKey();
                    fastestPath.insert(0, x.getKey() + " → ");
                    break;
                }
            }
        }
        if (fastestPath.isEmpty() || !(fastestPath.toString().startsWith(start) && fastestPath.toString().endsWith(end))) {
            stringBuilder.append("\nKhông tìm thấy đường đi phù hợp.");
        } else {
            stringBuilder.append("\nĐường đi tìm được: ").append(fastestPath);
        }
        System.out.println(stringBuilder);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("graph-outputHillClimbing.txt"))) {
            writer.write(stringBuilder.toString());
            System.out.println("File written using BufferedWriter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}