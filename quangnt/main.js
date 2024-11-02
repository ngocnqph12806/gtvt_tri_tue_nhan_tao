const fs = require('fs');

// Hàm đọc dữ liệu từ file input
function readInput(filePath) {
    const data = fs.readFileSync(filePath, 'utf8').split('\n');
    const graph = {};
    const heuristics = {};
    let startNode = null;
    let goalNode = null;

    data.forEach(line => {
        const tokens = line.trim().split(' ');
        if (tokens[0] === 'Start') {
            startNode = tokens[1];
        } else if (tokens[0] === 'Goal') {
            goalNode = tokens[1];
        } else if (tokens.length === 2) {
            heuristics[tokens[0]] = parseInt(tokens[1], 10);
        } else if (tokens.length === 3) {
            const [from, to, cost] = tokens;
            if (!graph[from]) graph[from] = [];
            graph[from].push({ node: to, cost: parseInt(cost, 10) });
        }
    });

    return { graph, heuristics, startNode, goalNode };
}

// Thuật toán nhánh và cận
function branchAndBound(graph, heuristics, startNode, goalNode) {
    const visited = new Set();
    const queue = [{ path: [startNode], cost: 0, heuristic: heuristics[startNode] }];
    const steps = [];

    while (queue.length > 0) {
        // Sắp xếp hàng đợi dựa trên tổng chi phí (cost + heuristic)
        queue.sort((a, b) => (a.cost + a.heuristic) - (b.cost + b.heuristic));
        
        const current = queue.shift();
        const currentNode = current.path[current.path.length - 1];

        // Ghi lại bước thực hiện
        steps.push(`Visiting Node: ${currentNode}, Path: ${current.path.join(' -> ')}, Cost: ${current.cost}`);

        // Kiểm tra nếu đã đến đích
        if (currentNode === goalNode) {
            return { path: current.path, cost: current.cost, steps };
        }

        // Đánh dấu nút đã được thăm
        visited.add(currentNode);

        // Mở rộng các nút kề chưa được thăm
        if (graph[currentNode]) {
            graph[currentNode].forEach(neighbor => {
                if (!visited.has(neighbor.node)) {
                    queue.push({
                        path: [...current.path, neighbor.node],
                        cost: current.cost + neighbor.cost,
                        heuristic: heuristics[neighbor.node]
                    });
                }
            });
        }
    }
    return { path: [], cost: Infinity, steps };
}

// Hàm ghi kết quả ra file output
function writeOutput(filePath, result) {
  const output = [];

  output.push("Bảng liệt kê các bước thực hiện thuật toán:");
  output.push("------------------------------------------------------------");
  output.push("| Bước |Nút hiện tại|     Đường đi       | Chi phí  |");
  output.push("------------------------------------------------------------");

  // Ghi từng bước vào bảng
  result.steps.forEach((step, index) => {
      const [_, nodeInfo, pathInfo, costInfo] = step.match(/Visiting Node: (\w+), Path: (.+), Cost: (\d+)/);
      output.push(`|  ${index + 1}   |      ${nodeInfo}     | ${pathInfo} |   ${costInfo}  |`);
  });

  output.push("------------------------------------------------------------");

  output.push("\nĐường đi từ Trạng thái đầu => Trạng thái kết thúc:");
  output.push("------------------------------------------------------------");
  output.push(`| ${result.path.join(" -> ")} |`);
  output.push("------------------------------------------------------------");
  output.push(`Tổng chi phí: ${result.cost}`);
  
  fs.writeFileSync(filePath, output.join('\n'), 'utf8');
}


// Đọc input, chạy thuật toán và ghi output
const inputFilePath = './input.txt'; // Đường dẫn đến file input
const outputFilePath = './output.txt'; // Đường dẫn đến file output

const { graph, heuristics, startNode, goalNode } = readInput(inputFilePath);
const result = branchAndBound(graph, heuristics, startNode, goalNode);
writeOutput(outputFilePath, result);

