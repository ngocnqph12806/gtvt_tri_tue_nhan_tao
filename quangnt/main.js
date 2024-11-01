const fs = require("fs");

// Phân tích tệp đầu vào
function parseGraph(filename) {
  const data = fs.readFileSync(filename, "utf-8").split("\n");
  const [startNode, endNode] = data[0].trim().split(" ");
  const graph = {};

  data.slice(1).forEach((line) => {
    if (line.trim()) {
      const [from, to, cost] = line.trim().split(" ");
      if (!graph[from]) graph[from] = [];
      if (!graph[to]) graph[to] = [];
      graph[from].push({ node: to, cost: parseInt(cost) });
      graph[to].push({ node: from, cost: parseInt(cost) }); // Nếu đồ thị là vô hướng
    }
  });

  return { graph, startNode, endNode };
}

// Hàng đợi ưu tiên (Min-Heap) cho phương pháp nhánh và cận
class PriorityQueue {
  constructor() {
    this.queue = [];
  }

  enqueue(element, priority) {
    this.queue.push({ element, priority });
    this.queue.sort((a, b) => a.priority - b.priority);
  }

  dequeue() {
    return this.queue.shift().element;
  }

  isEmpty() {
    return this.queue.length === 0;
  }
}

// Thuật toán Nhánh và Cận
function branchAndBound(graph, startNode, endNode) {
  const pq = new PriorityQueue();
  pq.enqueue({ path: [startNode], cost: 0 }, 0);
  const visited = new Set();
  const steps = [];

  while (!pq.isEmpty()) {
    const { path, cost } = pq.dequeue();
    const currentNode = path[path.length - 1];

    // Ghi lại bước thực hiện
    steps.push({ path: [...path], cost });

    // Nếu đã đến nút đích, trả về kết quả
    if (currentNode === endNode) {
      return { steps, path, totalCost: cost };
    }

    // Đánh dấu nút đã được duyệt
    visited.add(currentNode);

    // Khám phá các nút kề
    graph[currentNode].forEach((neighbor) => {
      if (!visited.has(neighbor.node)) {
        const newPath = [...path, neighbor.node];
        const newCost = cost + neighbor.cost;
        pq.enqueue({ path: newPath, cost: newCost }, newCost);
      }
    });
  }

  return { steps, path: null, totalCost: Infinity }; // Không tìm thấy đường đi
}

// Ghi kết quả ra tệp
function writeOutput(filename, result) {
  const lines = [];

  lines.push("Các bước:");
  result.steps.forEach((step, index) => {
    lines.push(
      `Bước ${index + 1}: Đường đi = ${step.path.join(" -> ")}, Chi phí = ${step.cost}`
    );
  });

  lines.push("\nĐường đi tối ưu:");
  lines.push(
    result.path
      ? `Đường đi = ${result.path.join(" -> ")}, Tổng chi phí = ${result.totalCost}`
      : "Không tìm thấy đường đi"
  );

  fs.writeFileSync(filename, lines.join("\n"), "utf-8");
}

// Hàm chính
function main(inputFile, outputFile) {
  const { graph, startNode, endNode } = parseGraph(inputFile);
  const result = branchAndBound(graph, startNode, endNode);
  writeOutput(outputFile, result);
}

// Chạy thuật toán với các tệp đầu vào và đầu ra
main("input.txt", "output.txt");
