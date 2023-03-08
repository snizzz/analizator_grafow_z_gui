import java.util.ArrayDeque;

public class BFS {
    private boolean result = true;
    public boolean getResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public BFS(Graf graph) {
        try {
                    boolean[] visited = new boolean[graph.getNumberOfRows() * graph.getNumberOfColumns()];
                    ArrayDeque<Integer> queue = new ArrayDeque<>();
                    for (int g = 0; g < visited.length; g++) visited[g] = false;
                    queue.add(0);
                    visited[0] = true;
                    while (queue.size() != 0) {
                        int w = queue.getFirst();
                        for (int h = 0;h < graph.getVertexArray().get(w).getNumberOfNeighbors(); h++) {
                            if (!visited[graph.getVertexArray().get(w).getNeighbors().get(h).getDestination()]) {
                                queue.add(graph.getVertexArray().get(w).getNeighbors().get(h).getDestination());
                                visited[graph.getVertexArray().get(w).getNeighbors().get(h).getDestination()] = true;
                            }
                        }
                        queue.removeFirst();
                    }

                    for (boolean b : visited) {
                        if (!b) {
                            setResult(false);
                            break;
                        } else setResult(true);
                    }
            }


        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Graf nie istnieje");
        }
    }
}
