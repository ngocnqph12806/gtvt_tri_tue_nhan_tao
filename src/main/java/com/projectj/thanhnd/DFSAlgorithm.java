package com.projectj.thanhnd;

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
        // Đọc đồ thị từ file
        Map<String, List<String>> graph = readGraphFromFile("input.txt", trangThai);
        String startState = String.valueOf(trangThai.get("TTD"));  // Trạng thái đầu
        String goalState = String.valueOf(trangThai.get("TTKT"));   // Trạng thái kết thúc
        // Thực hiện thuật toán DFS
        List<String> duongDi = new ArrayList<>();
        List<String[]> result = dfs(graph, startState, goalState, duongDi);

        // Ghi kết quả ra file output
        writeOutputToFile(duongDi, result, "output.txt");
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
            // j <= 1 đọc 2 trạng thái đầu và trạng thái kết thúc
            if (j <= 1) {
                if (parts.length > 1) {
                    tt.put(parts[0], parts[1]);
                }
            } else {
                String node = parts[0]; // khởi tạo node gốc
                List<String> neighbors = new ArrayList<>(); // lấy danh sách các node con
                for (int i = 1; i < parts.length; i++) {
                    neighbors.add(parts[i]);
                }
                // kiểu dữ liệu map, key là node cha, value là danh sách node con
                graph.put(node, neighbors);
            }
            j++;
        }
        reader.close();
        return graph;
    }

    // Thuật toán DFS với việc lưu lại quá trình duyệt
    public static List<String[]> dfs(Map<String, List<String>> graph, String startState, String goalState, List<String> duongDi) {
        List<String[]> log = new ArrayList<>(); // log để tí in ra được các bước thực hiện
        Stack<List<String>> stack = new Stack<>(); // danh sách L
        Set<String> visited = new HashSet<>(); // danh sách Q: các node đã duyệt

        // Khởi tạo ngăn xếp với trạng thái đầu ví dụ tìm từ A thì đầu là A
        stack.push(Collections.singletonList(startState));

        while (!stack.isEmpty()) {
            List<String> path = stack.pop(); // lấy node ra từ stack và xóa, stack vào sau ra trước -> lấy cái cuối cùng, node này là danh sách đường đi tạm
            String currentNode = path.getLast(); // lấy node đang duyệt

            // Đánh dấu đã thăm
            visited.add(currentNode); // thêm vào danh sách đã duyệt

            // Ghi lại quá trình duyệt
            List<String> neighbors = graph.getOrDefault(currentNode, new ArrayList<>()); // lấy các node con, ko có thì cho rỗng

            // Kiểm tra nếu đã đến trạng thái kết thúc
            if (currentNode.equals(goalState)) { // nếu node đang duyệt là node cần tìm thì thêm vào kết quả và break
                duongDi.addAll(path);
                break;
            }

            // Thêm hàng xóm của nút hiện tại vào ngăn xếp
            // Thêm vào danh sách L để tí lấy cái cuối ra duyệt tiếp, nếu đỉnh nào đã duyệt thì ko duyệt lại nữa
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) { // chưa thăm node con này
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

        return log;  // Trả về log với các trạng thái duyệt
    }

    // Ghi kết quả ra file output
    public static void writeOutputToFile(List<String> result, List<String[]> log, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        String format = "%35s|%35s|%35s|%35s";
        writer.write(String.format("%35s|%33s|%33s|%35s", "Trạng thái phát triển", "Trạng thái liền kề", "Danh sách Q", "Danh sách L") + "\n");
        for (String[] entry : log) {
            writer.write(String.format(format, entry[0].trim(), entry[1].trim(), entry[2].trim(), entry[3].trim()).replace("[", "").replace("]", "") + "\n");
        }
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