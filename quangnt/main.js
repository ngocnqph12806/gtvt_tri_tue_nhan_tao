const fs = require('fs');

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

function branchAndBound(graph, heuristics, startNode, goalNode) {
    const visited = new Set();
    const queue = [{ path: [startNode], cost: 0, heuristic: heuristics[startNode] }];
    const steps = [];
    let lstL = [];
    while (queue.length > 0) {
        queue.sort((a, b) => (a.cost + a.heuristic) - (b.cost + b.heuristic));
        const current = queue.shift();
        const currentNode = current.path[current.path.length - 1];
        if (lstL.length > 0) {
            lstL = lstL.slice(1);
        }
        if (graph[currentNode]) {
            graph[currentNode].sort((a, b) => (current.cost + b.cost + heuristics[b.node]) - (current.cost + a.cost + heuristics[a.node])).forEach(neighbor => {
                if (!visited.has(neighbor.node)) {
                    const newLst = [neighbor, ...lstL]
                    lstL = newLst;
                }
            });

        }
        console.log(lstL)
        steps.push(`Visiting Node: ${currentNode}, Path: ${current.path.join(' -> ')}, Cost: ${current.cost}, DanhSach: ${(lstL || []).map(e => e.node).join(", ")}`);

        if (currentNode === goalNode) {
            return { path: current.path, cost: current.cost, steps };
        }

        visited.add(currentNode);

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

function writeOutput(filePath, result) {
  const output = [];

  output.push("Bảng liệt kê các bước thực hiện thuật toán:");
  output.push("------------------------------------------------------------");
  output.push("| Bước |Nút hiện tại|     Đường đi       | Chi phí  | Danh sách L");
  output.push("------------------------------------------------------------");

  result.steps.forEach((step, index) => {
      const [_, nodeInfo, pathInfo, costInfo, danhSachInfo] = step.match(/Visiting Node: (\w+), Path: (.+), Cost: (\d+), DanhSach: (.+)/);
      output.push(`|  ${index + 1}   |      ${nodeInfo}     | ${pathInfo} |   ${costInfo}  |   ${danhSachInfo || ''} `);
      output.push("------------------------------------------------------------");
  });


  output.push("\nĐường đi từ Trạng thái đầu => Trạng thái kết thúc:");
  output.push("------------------------------------------------------------");
  output.push(`| ${result.path.join(" -> ")} |`);
  output.push("------------------------------------------------------------");
  output.push(`Tổng chi phí: ${result.cost}`);
  
  fs.writeFileSync(filePath, output.join('\n'), 'utf8');
}


const inputFilePath = './input.txt'; // Đường dẫn đến file input
const outputFilePath = './output.txt'; // Đường dẫn đến file output

const { graph, heuristics, startNode, goalNode } = readInput(inputFilePath);
const result = branchAndBound(graph, heuristics, startNode, goalNode);
writeOutput(outputFilePath, result);

