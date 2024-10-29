package com.projectj.ngocnq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HillClimbingSearch {
    public static List<SearchState> hillClimbingSearch(String startNode, String endNode, Graph graph) {
        List<SearchState> results = new ArrayList<>();
        LinkedList<String> lstL2 = new LinkedList<>();  // Danh sách L
        String currentState = startNode;
        
        while (!endNode.equals(currentState)) {
            // lấy danh sách trạng thái kề
            List<String> neighbors = graph.getNeighbors(currentState);
            String neighborStr = String.join(",", neighbors);
            // Sắp xếp list L1 theo giá trị số tăng dần
            neighbors.sort((s1, s2) -> {
                // Lấy phần số từ chuỗi và so sánh
                int num1 = Integer.parseInt(s1.replaceAll("\\D+", ""));
                int num2 = Integer.parseInt(s2.replaceAll("\\D+", ""));
                return Integer.compare(num1, num2);
            });
            if (!neighborStr.isEmpty()) {
                // Thêm trạng thái kế ở list L1 vào đầu lstL2
                LinkedList<String> stringList = new LinkedList<>(neighbors);
                stringList.addAll(lstL2);
                lstL2 = new LinkedList<>(stringList);
            }
            // Lưu kết quả theo định dạng trong bảng mẫu
            String l1Str = String.join(",", neighbors);
            String l2Str = String.join(",", lstL2);
            results.add(new SearchState(currentState, neighborStr, l1Str, l2Str));
            if (lstL2.isEmpty()) {
                break;
            }
            // Lấy trạng thái tiếp theo từ đầu danh sách L2
            currentState = lstL2.removeFirst();
            if (endNode.equals(currentState)) {
                results.add(new SearchState(endNode, "TTKT-DUNG", "", ""));
            }
        }
        return results;
    }
    
    public static void main(String[] args) {
        String start = "A20";
        String end = "B0";
        Graph g = new Graph();
        try (BufferedReader br = new BufferedReader(new FileReader("graph-inputHillClimbing.txt"))) {
            String line;
            // đọc file txt
            while ((line = br.readLine()) != null) {
                // tách giá trị thành 2 phần bởi dấu phẩy
                // trước giấy phẩy sẽ là trạng thái phát triển
                // sau dấu phẩy là trạng thái kề
                String[] parts = line.split(",");
                String source = parts[0].trim();
                // nếu tách nhau bởi dấu phẩy ra mà có 2 phần tử thì phần tử đầu làm trạng thái pt,
                // phần tử sau là trạng thái kề nếu không có để rỗng
                String destination = parts.length > 1 ? parts[1].trim() : "";

                // Thêm cạnh vào đồ thị
                g.addEdge(source, destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchState> results = hillClimbingSearch(start, end, g);
        printResult(results, end);
    }

    private static void printResult(List<SearchState> results, String end) {
        // In tiêu đề bảng
        System.out.println("\nQuá trình tìm kiếm theo PP leo đồi");
        System.out.println("\n+-----------+---------------+---------------+------------------------+");
        System.out.println("| Phát      | Trạng thái    | Danh sách L1  | Danh sách L2           |");
        System.out.println("| triển TT  | kế            |               |                        |");
        System.out.println("+-----------+---------------+---------------+------------------------+");

        // In nội dung bảng
        for (SearchState result : results) {
            System.out.printf("| %-9s | %-13s | %-13s | %-22s |\n",
                    result.state,
                    result.nextStates,
                    result.listL1,
                    result.listL);
            System.out.println("+-----------+---------------+---------------+------------------------+");
        }

        if (end.equals(results.getLast().state)) {
            // In đường đi
            System.out.println("\nĐường đi tìm được:");
            for (int i = 0; i < results.size(); i++) {
                System.out.print(results.get(i).state);
                if (i < results.size() - 1) {
                    System.out.print(" → ");
                }
            }
        } else {
            System.out.println("\nKhông tìm thấy đường đi phù hợp.");
        }
        System.out.println();
    }
}