package s21_graph_algorithms;

public class TsmResult {
    public int[] vertices;     // порядок обхода вершин
    public double distance;    // длина маршрута

    public TsmResult(int[] vertices, double distance) {
        this.vertices = vertices;
        this.distance = distance;
    }
}