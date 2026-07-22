// tests/GraphTest.java
package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import s21_graph.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    @TempDir
    Path tempDir;

    @Test
    void loadValidGraphFromFile() throws Exception {
        String data = "0 5 0\n5 0 2\n0 2 0";
        Path file = tempDir.resolve("valid.mtx");
        Files.writeString(file, data);
        Graph g = new Graph();
        g.loadGraphFromFile(file.toString());
        assertEquals(3, g.getVerticesCount());
        assertEquals(5, g.getEdgeWeight(1, 2));
        assertEquals(0, g.getEdgeWeight(1, 3));
        assertEquals(2, g.getEdgeWeight(2, 3));
        // Проверка обратного направления (матрица симметрична в примере, но метод возвращает [from-1][to-1])
        assertEquals(5, g.getEdgeWeight(2, 1));
    }

    @Test
    void loadGraphWithSelfLoop() throws Exception {
        // Зацикливание – диагональный элемент не ноль
        String data = "2 1\n1 0";
        Path file = tempDir.resolve("loop.mtx");
        Files.writeString(file, data);
        Graph g = new Graph();
        g.loadGraphFromFile(file.toString());
        assertEquals(2, g.getVerticesCount());
        assertEquals(2, g.getEdgeWeight(1, 1));
        assertEquals(1, g.getEdgeWeight(1, 2));
    }

    @Test
    void loadGraphWithAllDifferentWeights() throws Exception {
        String data = "0 3 7\n3 0 11\n7 11 0";
        Path file = tempDir.resolve("diff.mtx");
        Files.writeString(file, data);
        Graph g = new Graph();
        g.loadGraphFromFile(file.toString());
        assertEquals(3, g.getVerticesCount());
        assertEquals(3, g.getEdgeWeight(1, 2));
        assertEquals(7, g.getEdgeWeight(1, 3));
        assertEquals(11, g.getEdgeWeight(2, 3));
    }

    @Test
    void loadNonSquareMatrixThrows() throws IOException {
        String data = "0 1\n1 0 3"; // разное количество столбцов
        Path file = tempDir.resolve("nonsquare.mtx");
        Files.writeString(file, data);
        Graph g = new Graph();
        assertThrows(GraphException.class, () -> g.loadGraphFromFile(file.toString()));
    }

    @Test
    void loadNegativeWeightThrows() throws IOException {
        String data = "0 -1\n-1 0"; // отрицательные веса запрещены
        Path file = tempDir.resolve("neg.mtx");
        Files.writeString(file, data);
        Graph g = new Graph();
        assertThrows(GraphException.class, () -> g.loadGraphFromFile(file.toString()));
    }

    @Test
    void loadEmptyFileThrows() throws IOException {
        Path file = tempDir.resolve("empty.mtx");
        Files.writeString(file, "");
        Graph g = new Graph();
        assertThrows(GraphException.class, () -> g.loadGraphFromFile(file.toString()));
    }

    @Test
    void exportToDotAndCheckContent() throws Exception {
        // Создаем граф 3x3
        String data = "0 2 0\n2 0 4\n0 4 0";
        Path input = tempDir.resolve("graph.mtx");
        Files.writeString(input, data);
        Graph g = new Graph();
        g.loadGraphFromFile(input.toString());
        Path dotFile = tempDir.resolve("graph.dot");
        g.exportGraphToDot(dotFile.toString());

        String content = Files.readString(dotFile);
        assertTrue(content.contains("graph G {"));
        // Проверяем наличие ребер с метками (индексы с 1)
        assertTrue(content.contains("1 -- 2 [label=2];") || content.contains("1 -- 2 [label=2]"));
        assertTrue(content.contains("2 -- 3 [label=4];") || content.contains("2 -- 3 [label=4]"));
        assertTrue(content.contains("}"));
        // Не должно быть ребра 1-3 (вес 0)
        assertFalse(content.contains("1 -- 3"));
    }

    @Test
    void exportBeforeLoadThrows() {
        Graph g = new Graph();
        assertThrows(GraphException.class, () -> g.exportGraphToDot("any.dot"));
    }

    @Test
    void getAdjacentVerticesCorrect() throws Exception {
        String data = "0 1 0 0\n1 0 1 0\n0 1 0 1\n0 0 1 0"; // цепь 1-2-3-4
        Path file = tempDir.resolve("chain.mtx");
        Files.writeString(file, data);
        Graph g = new Graph();
        g.loadGraphFromFile(file.toString());
        List<Integer> adj1 = g.getAdjacentVertices(1);
        assertEquals(List.of(2), adj1);
        List<Integer> adj2 = g.getAdjacentVertices(2);
        assertEquals(List.of(1,3), adj2);
        List<Integer> adj4 = g.getAdjacentVertices(4);
        assertEquals(List.of(3), adj4);
    }

    @Test
    void getVerticesCountAfterLoad() throws Exception {
        String data = "0 1\n1 0";
        Path file = tempDir.resolve("two.mtx");
        Files.writeString(file, data);   // <-- добавить эту строку
        Graph g = new Graph();
        assertEquals(0, g.getVerticesCount());
        g.loadGraphFromFile(file.toString());
        assertEquals(2, g.getVerticesCount());
    }
}