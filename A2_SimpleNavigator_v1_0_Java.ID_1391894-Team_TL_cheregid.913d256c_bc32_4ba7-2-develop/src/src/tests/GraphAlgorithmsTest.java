package tests;

import org.junit.jupiter.api.*;
import s21_graph.*;
import s21_graph_algorithms.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GraphAlgorithmsTest {

    private Graph createTestGraph(String matrix) throws Exception {
        Path tmp = Files.createTempFile("graph", ".mtx");
        Files.writeString(tmp, matrix.trim());
        Graph g = new Graph();
        g.loadGraphFromFile(tmp.toString());
        Files.deleteIfExists(tmp);
        return g;
    }

    // =================== DFS =====================
    @Test
    void dfsSimpleChain() throws Exception {
        Graph g = createTestGraph("0 1 0\n1 0 1\n0 1 0");
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> result = algo.depthFirstSearch(g, 1);
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void dfsBranching() throws Exception {
        String m = "0 1 1 0\n1 0 0 0\n1 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.depthFirstSearch(g, 1);
        assertEquals(List.of(1, 3, 4, 2), res);
    }

    @Test
    void dfsStartVertexMiddle() throws Exception {
        String m = "0 1 0 0\n1 0 1 0\n0 1 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.depthFirstSearch(g, 3);
        assertEquals(List.of(3, 4, 2, 1), res);
    }

    @Test
    void dfsDisconnectedGraph() throws Exception {
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.depthFirstSearch(g, 2);
        assertEquals(List.of(2, 1), res);
    }

    @Test
    void dfsInvalidStartVertex() throws Exception {
        Graph g = createTestGraph("0 1\n1 0");
        GraphAlgorithms algo = new GraphAlgorithms();
        assertThrows(IllegalArgumentException.class, () -> algo.depthFirstSearch(g, 0));
        assertThrows(IllegalArgumentException.class, () -> algo.depthFirstSearch(g, 3));
    }

    // =================== BFS =====================
    @Test
    void bfsSimpleChain() throws Exception {
        Graph g = createTestGraph("0 1 0\n1 0 1\n0 1 0");
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.breadthFirstSearch(g, 1);
        assertEquals(List.of(1, 2, 3), res);
    }

    @Test
    void bfsBranching() throws Exception {
        String m = "0 1 1 0\n1 0 0 0\n1 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.breadthFirstSearch(g, 1);
        assertEquals(List.of(1, 2, 3, 4), res);
    }

    @Test
    void bfsStartVertex3() throws Exception {
        String m = "0 1 0 0\n1 0 1 0\n0 1 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.breadthFirstSearch(g, 3);
        assertEquals(List.of(3, 2, 4, 1), res);
    }

    @Test
    void bfsDisconnectedGraph() throws Exception {
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> res = algo.breadthFirstSearch(g, 1);
        assertEquals(List.of(1, 2), res);
    }

    @Test
    void bfsInvalidStartVertex() throws Exception {
        Graph g = createTestGraph("0 1\n1 0");
        GraphAlgorithms algo = new GraphAlgorithms();
        assertThrows(IllegalArgumentException.class, () -> algo.breadthFirstSearch(g, 0));
        assertThrows(IllegalArgumentException.class, () -> algo.breadthFirstSearch(g, 5));
    }

    // =================== Дейкстра =====================
    @Test
    void dijkstraSimplePath() throws Exception {
        String m = "0 5 0 0\n5 0 3 0\n0 3 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        assertEquals(9, algo.getShortestPathBetweenVertices(g, 1, 4));
    }

    @Test
    void dijkstraDirectEdge() throws Exception {
        String m = "0 10\n10 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        assertEquals(10, algo.getShortestPathBetweenVertices(g, 1, 2));
    }

    @Test
    void dijkstraNoPathReturnMinusOne() throws Exception {
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        assertEquals(-1, algo.getShortestPathBetweenVertices(g, 1, 4));
    }

    @Test
    void dijkstraInvalidVertex() throws Exception {
        Graph g = createTestGraph("0 1\n1 0");
        GraphAlgorithms algo = new GraphAlgorithms();
        assertThrows(IllegalArgumentException.class, () -> algo.getShortestPathBetweenVertices(g, 0, 1));
    }

    // =================== Флойд‑Уоршалл =====================
    @Test
    void floydWarshallAllPairs() throws Exception {
        String m = "0 2 10\n2 0 3\n10 3 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        int[][] dist = algo.getShortestPathsBetweenAllVertices(g);

        assertEquals(0, dist[0][0]);
        assertEquals(2, dist[0][1]);
        assertEquals(5, dist[0][2]);
        assertEquals(2, dist[1][0]);
        assertEquals(0, dist[1][1]);
        assertEquals(3, dist[1][2]);
        assertEquals(5, dist[2][0]);
        assertEquals(3, dist[2][1]);
        assertEquals(0, dist[2][2]);
    }

    @Test
    void floydWarshallDisconnectedGraph() throws Exception {
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        int[][] dist = algo.getShortestPathsBetweenAllVertices(g);
        assertEquals(1, dist[0][1]);
        assertEquals(Integer.MAX_VALUE, dist[0][2]);
    }

    // =================== Прима =====================
    @Test
    void primSimpleTree() throws Exception {
        String m = "0 2 3\n2 0 1\n3 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        int[][] mst = algo.getLeastSpanningTree(g);
        int edges = 0;
        for (int i = 0; i < 3; i++)
            for (int j = i + 1; j < 3; j++)
                if (mst[i][j] != 0) edges++;
        assertEquals(2, edges);
    }

    @Test
    void primFourVertices() throws Exception {
        String m = "0 5 0 0\n5 0 3 6\n0 3 0 1\n0 6 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        int[][] mst = algo.getLeastSpanningTree(g);
        assertTrue(mst[0][1] == 5 || mst[1][0] == 5);
        assertTrue(mst[1][2] == 3 || mst[2][1] == 3);
        assertTrue(mst[2][3] == 1 || mst[3][2] == 1);
    }

    // =================== Муравьиный алгоритм (TSP) =====================
    @Test
    void tspTriangle() throws Exception {
        String m = "0 10 15\n10 0 20\n15 20 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        TsmResult res = algo.solveTravelingSalesmanProblem(g);
        assertNotNull(res.vertices);
        assertEquals(3, res.vertices.length);
        assertEquals(45.0, res.distance, 0.01);
    }

    @Test
    void tspFourVertices() throws Exception {
        String m = "0 2 9 10\n2 0 6 4\n9 6 0 8\n10 4 8 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        TsmResult res = algo.solveTravelingSalesmanProblem(g);
        assertNotNull(res.vertices);
        assertEquals(4, res.vertices.length);
        assertTrue(res.distance >= 23.0);
    }

    @Test
    void tspTspOnDisconnectedFails() throws Exception {
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        assertThrows(RuntimeException.class, () -> algo.solveTravelingSalesmanProblem(g));
    }

    // =================== Ближайший сосед (TSP) =====================
    @Test
    void nearestNeighborCompleteGraph() throws Exception {
        String m = "0 2 9 10\n2 0 6 4\n9 6 0 8\n10 4 8 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        TsmResult res = algo.solveTravelingSalesmanNearestNeighbor(g);
        assertNotNull(res.vertices);
        assertEquals(4, res.vertices.length);
        // Проверяем, что маршрут начинается с 1 и все вершины присутствуют
        assertEquals(1, res.vertices[0]);
        Set<Integer> set = new HashSet<>();
        for (int v : res.vertices) set.add(v);
        assertEquals(4, set.size());
        assertTrue(res.distance > 0);
    }

    @Test
    void nearestNeighborDisconnectedFails() throws Exception {
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        assertThrows(RuntimeException.class, () -> algo.solveTravelingSalesmanNearestNeighbor(g));
    }

    // =================== Имитация отжига (TSP) =====================
    @Test
    void simulatedAnnealingCompleteGraph() throws Exception {
        String m = "0 2 9 10\n2 0 6 4\n9 6 0 8\n10 4 8 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        TsmResult res = algo.solveTravelingSalesmanSimulatedAnnealing(g);
        assertNotNull(res.vertices);
        assertEquals(4, res.vertices.length);
        Set<Integer> set = new HashSet<>();
        for (int v : res.vertices) set.add(v);
        assertEquals(4, set.size());
        assertTrue(res.distance >= 23.0); // может не найти оптимум, но должен быть >= минимуму
    }

    @Test
    void simulatedAnnealingDisconnectedFails() throws Exception {
        // Для отжига мы не делаем строгой проверки связности, но при вычислении расстояния
        // отсутствие ребра даст Double.MAX_VALUE, и алгоритм будет пытаться его улучшить,
        // но не сможет. Он вернёт какой-то маршрут, но расстояние будет огромным.
        // В нашей реализации calculateTourDistance возвращает MAX_VALUE при отсутствии ребра,
        // поэтому bestDistance останется MAX_VALUE, и в итоге вернётся маршрут с огромной длиной.
        // Однако задание требует выбросить ошибку, если невозможно построить маршрут.
        // Чтобы соответствовать условию, добавим проверку в самом алгоритме: если после отжига расстояние == MAX_VALUE, кидаем исключение.
        // Поэтому тест ожидает RuntimeException.
        String m = "0 1 0 0\n1 0 0 0\n0 0 0 1\n0 0 1 0";
        Graph g = createTestGraph(m);
        GraphAlgorithms algo = new GraphAlgorithms();
        assertThrows(RuntimeException.class, () -> algo.solveTravelingSalesmanSimulatedAnnealing(g));
    }
}