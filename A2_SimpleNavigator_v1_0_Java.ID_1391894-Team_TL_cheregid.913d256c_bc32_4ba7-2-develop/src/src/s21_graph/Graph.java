package s21_graph;

import java.io.*;
import java.util.*;

public class Graph {
    private int[][] adjacencyMatrix;
    private int verticesCount;

    public Graph() {
        adjacencyMatrix = null;
        verticesCount = 0;
    }

    public void loadGraphFromFile(String filename) throws GraphException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new GraphException("Ошибка чтения файла: " + e.getMessage());
        }
        if (lines.isEmpty()) throw new GraphException("Файл пуст");
        verticesCount = lines.size();
        adjacencyMatrix = new int[verticesCount][verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            String[] parts = lines.get(i).split("\\s+");
            if (parts.length != verticesCount)
                throw new GraphException("Неверный формат матрицы смежности");
            for (int j = 0; j < verticesCount; j++) {
                int val = Integer.parseInt(parts[j]);
                if (val < 0) throw new GraphException("Веса рёбер должны быть натуральными числами");
                adjacencyMatrix[i][j] = val;
            }
        }
    }

    public void exportGraphToDot(String filename) throws GraphException {
        if (adjacencyMatrix == null) throw new GraphException("Граф не загружен");
        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println("graph G {");
            for (int i = 0; i < verticesCount; i++) {
                for (int j = i + 1; j < verticesCount; j++) {
                    if (adjacencyMatrix[i][j] != 0) {
                        pw.printf("    %d -- %d [label=%d];\n", i+1, j+1, adjacencyMatrix[i][j]);
                    }
                }
            }
            pw.println("}");
        } catch (IOException e) {
            throw new GraphException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public int getVerticesCount() { return verticesCount; }
    public int getEdgeWeight(int from, int to) {
        return adjacencyMatrix[from-1][to-1];
    }
    public int[][] getAdjacencyMatrix() { return adjacencyMatrix; }

    // Для удобства: список смежных вершин (ненулевые веса)
    public List<Integer> getAdjacentVertices(int vertex) {
        List<Integer> adj = new ArrayList<>();
        for (int j = 0; j < verticesCount; j++) {
            if (adjacencyMatrix[vertex-1][j] != 0) adj.add(j+1);
        }
        return adj;
    }
}