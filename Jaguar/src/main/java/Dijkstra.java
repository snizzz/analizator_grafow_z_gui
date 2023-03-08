import javafx.util.Pair;

import java.util.*;

public class Dijkstra {
    private final int start;
    private final int end;
    private final int w;
    private final int k;
    private final ArrayList<Vertex> vertexArray;
    private final int[] predecessor;
    private final int[] visited;

    private final HashMap<Integer,Double> path;
    private final PriorityQueue<Integer> que = new PriorityQueue<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return path.get(o1).compareTo(path.get(o2));
        }
    });

    public int getStart() {
        return start;
    }

    public int[] getPredecessor() {
        return predecessor;
    }

    public int getEnd() {
        return end;
    }

    public Dijkstra(Graf graph, int start, int end) {
        this.vertexArray = graph.getVertexArray();
        this.start = start;
        this.end = end;
        this.w = graph.getNumberOfRows();
        this.k = graph.getNumberOfColumns();
        predecessor = new int[graph.getNumberOfColumns() * graph.getNumberOfRows()];
        visited = new int[graph.getNumberOfColumns() * graph.getNumberOfRows()];
        path=new HashMap<>();
    }

    private void prepare() {
        for (int i = 0; i < (w * k); i++) {
            predecessor[i] = -1;
            path.put(i,Double.MAX_VALUE);
            visited[i] = 0;
        }
        path.replace(start,0.0);
        if (start == 0) {
            que.add(1);
        } else{
            que.add(0);
        }
        for (int i = 0; i < (w * k); i++) {
            if (i != start) {
                que.add(i);
            }
        }

    }

    public HashMap<Integer, Double> distanceToEveryVertex() {
        prepare();
        int current = start;

        do {
            for (int j = 0; j < vertexArray.get(current).getNumberOfNeighbors(); j++) {
                if (path.get(vertexArray.get(current).getNeighbors().get(j).getDestination()) > (path.get(current) + vertexArray.get(current).getNeighbors().get(j).getWeight())) {
                    predecessor[vertexArray.get(current).getNeighbors().get(j).getDestination()] = current;
                    path.replace(vertexArray.get(current).getNeighbors().get(j).getDestination(),path.get(current) + vertexArray.get(current).getNeighbors().get(j).getWeight());
                    que.remove(vertexArray.get(current).getNeighbors().get(j).getDestination());
                    que.add(vertexArray.get(current).getNeighbors().get(j).getDestination());
                }
            }
            visited[current] = 1;
            current = que.poll();
            while (true) {
                if (que.isEmpty())
                    break;
                if (visited[current] == 0)
                    break;
                current = que.poll();
            }

        } while (!que.isEmpty());
        return path;
    }
    public ArrayList<Pair<Integer,Double>> pathToEnd() {
        ArrayList<Pair<Integer,Double>> pathToEndMap = new ArrayList<>();
        prepare();
        int current = start;

        do {
            for (int j = 0; j < vertexArray.get(current).getNumberOfNeighbors(); j++) {
                if (path.get(vertexArray.get(current).getNeighbors().get(j).getDestination()) > (path.get(current) + vertexArray.get(current).getNeighbors().get(j).getWeight())) {
                    predecessor[vertexArray.get(current).getNeighbors().get(j).getDestination()] = current;
                    path.replace(vertexArray.get(current).getNeighbors().get(j).getDestination(),path.get(current) + vertexArray.get(current).getNeighbors().get(j).getWeight());
                    que.remove(vertexArray.get(current).getNeighbors().get(j).getDestination());
                    que.add(vertexArray.get(current).getNeighbors().get(j).getDestination());
                }
            }
            visited[current] = 1;
            current = que.remove();
            while (true) {
                if (que.isEmpty())
                    break;
                if (visited[current] == 0)
                    break;
                current = que.remove();
            }

        } while (!que.isEmpty());


        if (path.get(end) ==Double.MAX_VALUE) {
            return pathToEndMap;
        } else {
            current = end;
            int i = 0;
            while (current != start) {
                i++;
                current = predecessor[current];
            }

            current = end;
            for (int j = i - 1; j >= 0; j--) {
                pathToEndMap.add(new Pair<>(current,(double)Math.round(path.get(current)*10000.0)/10000.0));
                current = predecessor[current];
            }
        }
        return pathToEndMap;
    }

}