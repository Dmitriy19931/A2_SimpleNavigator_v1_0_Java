package s21_graph_algorithms;

import s21_graph.Graph;
import s21_stack.Stack;
import s21_queue.Queue;

import java.util.*;

public class GraphAlgorithms {

    /**
     * Поиск в глубину (Depth‑First Search) от заданной вершины.
     * Использует собственный стек.
     */
    public List<Integer> depthFirstSearch(Graph graph, int startVertex) {
        if (graph == null || startVertex < 1 || startVertex > graph.getVerticesCount())
            throw new IllegalArgumentException("Неверные параметры");

        List<Integer> result = new ArrayList<>();
        int n = graph.getVerticesCount();
        boolean[] visited = new boolean[n + 1];
        Stack<Integer> stack = new Stack<>();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                visited[v] = true;
                result.add(v);
                List<Integer> neighbors = graph.getAdjacentVertices(v);
                for (int neighbor : neighbors) {
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Поиск в ширину (Breadth‑First Search) от заданной вершины.
     * Использует собственную очередь.
     */
    public List<Integer> breadthFirstSearch(Graph graph, int startVertex) {
        if (graph == null || startVertex < 1 || startVertex > graph.getVerticesCount())
            throw new IllegalArgumentException("Неверные параметры");

        List<Integer> result = new ArrayList<>();
        int n = graph.getVerticesCount();
        boolean[] visited = new boolean[n + 1];
        Queue<Integer> queue = new Queue<>();
        queue.push(startVertex);
        visited[startVertex] = true;

        while (!queue.isEmpty()) {
            int v = queue.pop();
            result.add(v);
            for (int neighbor : graph.getAdjacentVertices(v)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.push(neighbor);
                }
            }
        }
        return result;
    }

    /**
     * Поиск кратчайшего пути между двумя вершинами алгоритмом Дейкстры.
     * @return длина пути или -1, если вершины не связаны.
     */
    public int getShortestPathBetweenVertices(Graph graph, int vertex1, int vertex2) {
        if (graph == null || vertex1 < 1 || vertex1 > graph.getVerticesCount() ||
                vertex2 < 1 || vertex2 > graph.getVerticesCount())
            throw new IllegalArgumentException("Неверные параметры");

        int n = graph.getVerticesCount();
        int[] dist = new int[n + 1];
        boolean[] visited = new boolean[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[vertex1] = 0;


                }
            }
            if (v == -1) break;
            visited[v] = true;

            for (int neighbor : graph.getAdjacentVertices(v)) {
                int weight = graph.getEdgeWeight(v, neighbor);
                if (dist[v] != Integer.MAX_VALUE && dist[v] + weight < dist[neighbor]) {
                    dist[neighbor] = dist[v] + weight;
                }
            }
        }
        return dist[vertex2] == Integer.MAX_VALUE ? -1 : dist[vertex2];
    }

    /**
     * Поиск кратчайших путей между всеми парами вершин алгоритмом Флойда‑Уоршалла.
     * @return матрица N×N (0‑based). Отсутствие пути помечено Integer.MAX_VALUE.
     */
    public int[][] getShortestPathsBetweenAllVertices(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("Граф не должен быть null");
        int n = graph.getVerticesCount();
        int[][] dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    int w = graph.getEdgeWeight(i + 1, j + 1);
                    dist[i][j] = (w == 0) ? Integer.MAX_VALUE : w;
                }
            }
        }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE)
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);

        return dist;
    }

    /**
     * Минимальное остовное дерево алгоритмом Прима.
     * @return матрица смежности остовного дерева.
     */
    public int[][] getLeastSpanningTree(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("Граф не должен быть null");
        int n = graph.getVerticesCount();
        int[][] mst = new int[n][n];
        boolean[] inMST = new boolean[n + 1];
        int[] minWeight = new int[n + 1];
        int[] parent = new int[n + 1];

        Arrays.fill(minWeight, Integer.MAX_VALUE);
        minWeight[1] = 0;
        parent[1] = -1;

        for (int i = 1; i <= n; i++) {
            int u = -1;
            int best = Integer.MAX_VALUE;
            for (int v = 1; v <= n; v++) {
                if (!inMST[v] && minWeight[v] < best) {
                    best = minWeight[v];
                    u = v;
                }
            }
            if (u == -1) break;
            inMST[u] = true;

            if (parent[u] != -1) {
                int p = parent[u];
                int w = graph.getEdgeWeight(p, u);
                mst[p - 1][u - 1] = w;
                mst[u - 1][p - 1] = w;
            }

            for (int v : graph.getAdjacentVertices(u)) {
                int w = graph.getEdgeWeight(u, v);
                if (!inMST[v] && w < minWeight[v]) {
                    minWeight[v] = w;
                    parent[v] = u;
                }
            }
        }
        return mst;
    }

    /**
     * Задача коммивояжёра (муравьиный алгоритм).
     * @return TsmResult с vertices и distance. Если решение не найдено, выбрасывается RuntimeException.
     */
    public TsmResult solveTravelingSalesmanProblem(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("Граф не должен быть null");
        int n = graph.getVerticesCount();
        if (n == 0) throw new RuntimeException("Граф пуст");
        if (n == 1) {
            return new TsmResult(new int[]{1}, 0.0);
        }

        final int MAX_ITER = 100;
        final int ANTS = n;
        final double ALPHA = 1.0;
        final double BETA = 2.0;
        final double EVAPORATION = 0.5;
        final double Q = 100.0;

        double[][] pheromone = new double[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (graph.getEdgeWeight(i, j) != 0) {
                    pheromone[i][j] = 1.0;
                    pheromone[j][i] = 1.0;
                }
            }
        }

        Random rnd = new Random();
        int[] bestTour = null;
        double bestLength = Double.MAX_VALUE;

        for (int iter = 0; iter < MAX_ITER; iter++) {
            for (int ant = 0; ant < ANTS; ant++) {
                int start = 1;
                List<Integer> visited = new ArrayList<>();
                boolean[] used = new boolean[n + 1];
                int current = start;
                visited.add(current);
                used[current] = true;
                double length = 0.0;

                while (visited.size() < n) {
                    List<Integer> candidates = new ArrayList<>();
                    List<Double> probabilities = new ArrayList<>();
                    double sum = 0.0;
                    for (int nb : graph.getAdjacentVertices(current)) {
                        if (!used[nb] && graph.getEdgeWeight(current, nb) > 0) {
                            double tau = Math.pow(pheromone[current][nb], ALPHA);
                            double eta = Math.pow(1.0 / graph.getEdgeWeight(current, nb), BETA);
                            double p = tau * eta;
                            candidates.add(nb);
                            probabilities.add(p);
                            sum += p;
                        }
                    }
                    if (candidates.isEmpty()) break;

                    double rand = rnd.nextDouble() * sum;
                    double cumulative = 0.0;
                    int chosen = -1;
                    for (int idx = 0; idx < candidates.size(); idx++) {
                        cumulative += probabilities.get(idx);
                        if (rand <= cumulative) {
                            chosen = candidates.get(idx);
                            break;
                        }
                    }
                    if (chosen == -1) chosen = candidates.get(0);

                    length += graph.getEdgeWeight(current, chosen);
                    visited.add(chosen);
                    used[chosen] = true;
                    current = chosen;
                }

                if (visited.size() == n && graph.getEdgeWeight(current, start) > 0) {
                    length += graph.getEdgeWeight(current, start);
                    int[] tour = visited.stream().mapToInt(Integer::intValue).toArray();
                    if (length < bestLength) {
                        bestLength = length;
                        bestTour = tour;
                    }
                }
            }

            for (int i = 1; i <= n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    if (graph.getEdgeWeight(i, j) != 0) {
                        pheromone[i][j] *= (1.0 - EVAPORATION);
                        pheromone[j][i] *= (1.0 - EVAPORATION);
                    }
                }
            }

            if (bestTour != null) {
                double deposit = Q / bestLength;
                for (int i = 0; i < bestTour.length; i++) {
                    int u = bestTour[i];
                    int v = bestTour[(i + 1) % bestTour.length];
                    if (graph.getEdgeWeight(u, v) > 0) {
                        pheromone[u][v] += deposit;
                        pheromone[v][u] += deposit;
                    }
                }
            }
        }

        if (bestTour == null) {
            throw new RuntimeException("Не удалось найти маршрут, удовлетворяющий условию задачи");
        }
        return new TsmResult(bestTour, bestLength);
    }

    // -------------------------------------------------------------------------
    // ДОПОЛНИТЕЛЬНЫЕ АЛГОРИТМЫ ДЛЯ ЗАДАЧИ КОММИВОЯЖЁРА
    // -------------------------------------------------------------------------

    /**
     * Жадный алгоритм ближайшего соседа для задачи коммивояжёра.
     */
    public TsmResult solveTravelingSalesmanNearestNeighbor(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("Граф не должен быть null");
        int n = graph.getVerticesCount();
        if (n == 0) throw new RuntimeException("Граф пуст");
        if (n == 1) return new TsmResult(new int[]{1}, 0.0);

        boolean[] visited = new boolean[n + 1];
        int[] route = new int[n];
        int current = 1;
        visited[current] = true;
        route[0] = current;
        double totalDistance = 0.0;

        for (int i = 1; i < n; i++) {
            int nearest = -1;
            int minDist = Integer.MAX_VALUE;
            for (int neighbor : graph.getAdjacentVertices(current)) {
                if (!visited[neighbor]) {
                    int w = graph.getEdgeWeight(current, neighbor);
                    if (w < minDist) {
                        minDist = w;
                        nearest = neighbor;
                    }
                }
            }
            if (nearest == -1) {
                throw new RuntimeException("Невозможно построить полный маршрут – граф неполный");
            }
            visited[nearest] = true;
            route[i] = nearest;
            totalDistance += minDist;
            current = nearest;
        }

        if (graph.getEdgeWeight(current, 1) == 0) {
            throw new RuntimeException("Невозможно вернуться в стартовую вершину – граф неполный");
        }
        totalDistance += graph.getEdgeWeight(current, 1);

        return new TsmResult(route, totalDistance);
    }

    /**
     * Алгоритм имитации отжига для задачи коммивояжёра.
     */
    public TsmResult solveTravelingSalesmanSimulatedAnnealing(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("Граф не должен быть null");
        int n = graph.getVerticesCount();
        if (n == 0) throw new RuntimeException("Граф пуст");
        if (n == 1) return new TsmResult(new int[]{1}, 0.0);

        Random rnd = new Random();
        List<Integer> perm = new ArrayList<>();
        for (int i = 1; i <= n; i++) perm.add(i);
        Collections.shuffle(perm, rnd);
        int[] currentRoute = perm.stream().mapToInt(Integer::intValue).toArray();
        double currentDistance = calculateTourDistance(graph, currentRoute);

        int[] bestRoute = Arrays.copyOf(currentRoute, n);
        double bestDistance = currentDistance;

        double temperature = 1000.0;
        final double COOLING_RATE = 0.995;
        final int STEPS = 5000;

        for (int step = 0; step < STEPS; step++) {
            int[] newRoute = Arrays.copyOf(currentRoute, n);
            int i = rnd.nextInt(n);
            int j = rnd.nextInt(n);
            while (i == j) j = rnd.nextInt(n);
            int tmp = newRoute[i];
            newRoute[i] = newRoute[j];
            newRoute[j] = tmp;

            double newDistance = calculateTourDistance(graph, newRoute);
            if (newDistance < currentDistance ||
                    rnd.nextDouble() < Math.exp((currentDistance - newDistance) / temperature)) {
                currentRoute = newRoute;
                currentDistance = newDistance;
                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                    bestRoute = Arrays.copyOf(currentRoute, n);
                }
            }
            temperature *= COOLING_RATE;
            if (temperature < 0.001) break;
        }

        if (bestDistance == Double.MAX_VALUE) {
            throw new RuntimeException("Невозможно построить полный маршрут – граф неполный");
        }
        return new TsmResult(bestRoute, bestDistance);
    }

    private double calculateTourDistance(Graph graph, int[] route) {
        double dist = 0.0;
        int n = route.length;
        for (int i = 0; i < n; i++) {
            int u = route[i];
            int v = route[(i + 1) % n];
            int w = graph.getEdgeWeight(u, v);
            if (w == 0) {
                return Double.MAX_VALUE;
            }
            dist += w;
        }
        return dist;
    }
}