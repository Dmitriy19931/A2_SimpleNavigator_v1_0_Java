package app;

import s21_graph.*;
import s21_graph_algorithms.*;

import java.util.*;

public class ConsoleApp {
    private static Graph graph = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Консольное приложение для работы с графами");
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": loadGraph(); break;
                case "2": bfs(); break;
                case "3": dfs(); break;
                case "4": shortestPath(); break;
                case "5": allShortestPaths(); break;
                case "6": minSpanningTree(); break;
                case "7": tspAntColony(); break;
                case "8": compareTSP(); break;
                case "9": System.out.println("Выход."); return;
                default: System.out.println("Неверный пункт меню. Попробуйте ещё раз.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Загрузить граф из файла");
        System.out.println("2. Обход в ширину (BFS)");
        System.out.println("3. Обход в глубину (DFS)");
        System.out.println("4. Кратчайший путь между двумя вершинами (Дейкстра)");
        System.out.println("5. Кратчайшие пути между всеми парами (Флойд-Уоршалл)");
        System.out.println("6. Минимальное остовное дерево (Прим)");
        System.out.println("7. Задача коммивояжёра (муравьиный алгоритм)");
        System.out.println("8. Сравнение алгоритмов задачи коммивояжёра");
        System.out.println("9. Выход");
        System.out.print("> ");
    }

    private static void loadGraph() {
        System.out.print("Введите путь к файлу с матрицей смежности: ");
        String filename = scanner.nextLine().trim();
        Graph newGraph = new Graph();
        try {
            newGraph.loadGraphFromFile(filename);
            graph = newGraph;
            System.out.println("Граф успешно загружен. Количество вершин: " + graph.getVerticesCount());
        } catch (GraphException e) {
            System.out.println("Ошибка загрузки графа: " + e.getMessage());
        }
    }

    private static void checkGraph() {
        if (graph == null) {
            System.out.println("Сначала загрузите граф (пункт 1).");
        }
    }

    private static int readVertex(String prompt, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= 1 && v <= max) return v;
                System.out.println("Вершина должна быть от 1 до " + max);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private static void bfs() {
        if (graph == null) { checkGraph(); return; }
        int start = readVertex("Начальная вершина: ", graph.getVerticesCount());
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> order = algo.breadthFirstSearch(graph, start);
        System.out.println("BFS порядок: " + order);
    }

    private static void dfs() {
        if (graph == null) { checkGraph(); return; }
        int start = readVertex("Начальная вершина: ", graph.getVerticesCount());
        GraphAlgorithms algo = new GraphAlgorithms();
        List<Integer> order = algo.depthFirstSearch(graph, start);
        System.out.println("DFS порядок: " + order);
    }

    private static void shortestPath() {
        if (graph == null) { checkGraph(); return; }
        int v1 = readVertex("Первая вершина: ", graph.getVerticesCount());
        int v2 = readVertex("Вторая вершина: ", graph.getVerticesCount());
        GraphAlgorithms algo = new GraphAlgorithms();
        int dist = algo.getShortestPathBetweenVertices(graph, v1, v2);
        if (dist == -1) {
            System.out.println("Путь между вершинами " + v1 + " и " + v2 + " не существует.");
        } else {
            System.out.println("Кратчайшее расстояние: " + dist);
        }
    }

    private static void allShortestPaths() {
        if (graph == null) { checkGraph(); return; }
        GraphAlgorithms algo = new GraphAlgorithms();
        int[][] dist = algo.getShortestPathsBetweenAllVertices(graph);
        int n = graph.getVerticesCount();
        System.out.println("Матрица кратчайших расстояний (∞ — нет пути):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (dist[i][j] == Integer.MAX_VALUE) {
                    System.out.print("∞\t");
                } else {
                    System.out.print(dist[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    private static void minSpanningTree() {
        if (graph == null) { checkGraph(); return; }
        GraphAlgorithms algo = new GraphAlgorithms();
        int[][] mst = algo.getLeastSpanningTree(graph);
        int n = graph.getVerticesCount();
        System.out.println("Матрица смежности минимального остовного дерева:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(mst[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void tspAntColony() {
        if (graph == null) { checkGraph(); return; }
        GraphAlgorithms algo = new GraphAlgorithms();
        try {
            TsmResult result = algo.solveTravelingSalesmanProblem(graph);
            System.out.println("Маршрут: " + Arrays.toString(result.vertices));
            System.out.println("Длина маршрута: " + result.distance);
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Сравнение скорости трёх алгоритмов задачи коммивояжёра.
     * Пользователь вводит число повторений N, после чего для каждого алгоритма
     * производится N запусков, замеряется общее время и выводится среднее.
     */
    private static void compareTSP() {
        if (graph == null) { checkGraph(); return; }
        System.out.print("Введите количество повторений N: ");
        int n;
        try {
            n = Integer.parseInt(scanner.nextLine().trim());
            if (n <= 0) {
                System.out.println("N должно быть положительным.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректное число.");
            return;
        }

        GraphAlgorithms algo = new GraphAlgorithms();
        System.out.println("\nСравнение алгоритмов на " + n + " запусках...");

        // Муравьиный алгоритм
        long timeAnt = 0;
        boolean antSuccess = true;
        try {
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                algo.solveTravelingSalesmanProblem(graph);
            }
            timeAnt = System.nanoTime() - start;
        } catch (RuntimeException e) {
            antSuccess = false;
            System.out.println("Муравьиный алгоритм: ошибка - " + e.getMessage());
        }

        // Ближайший сосед
        long timeNN = 0;
        boolean nnSuccess = true;
        try {
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                algo.solveTravelingSalesmanNearestNeighbor(graph);
            }
            timeNN = System.nanoTime() - start;
        } catch (RuntimeException e) {
            nnSuccess = false;
            System.out.println("Ближайший сосед: ошибка - " + e.getMessage());
        }

        // Имитация отжига
        long timeSA = 0;
        boolean saSuccess = true;
        try {
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                algo.solveTravelingSalesmanSimulatedAnnealing(graph);
            }
            timeSA = System.nanoTime() - start;
        } catch (RuntimeException e) {
            saSuccess = false;
            System.out.println("Имитация отжига: ошибка - " + e.getMessage());
        }

        System.out.println("\nРезультаты (время указано в миллисекундах):");
        if (antSuccess)
            System.out.printf("Муравьиный алгоритм: всего %d мс, среднее %.3f мс%n",
                    timeAnt / 1_000_000, (double) timeAnt / n / 1_000_000);
        if (nnSuccess)
            System.out.printf("Ближайший сосед:     всего %d мс, среднее %.3f мс%n",
                    timeNN / 1_000_000, (double) timeNN / n / 1_000_000);
        if (saSuccess)
            System.out.printf("Имитация отжига:     всего %d мс, среднее %.3f мс%n",
                    timeSA / 1_000_000, (double) timeSA / n / 1_000_000);
    }
}