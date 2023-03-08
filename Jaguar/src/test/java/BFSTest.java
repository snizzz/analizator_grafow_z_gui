import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

class BFSTest {

    Graf graph;
    BFS bfs, bfs1;
    @Test
    void getResultFromConnectedGraph() throws IOException {
        graph=Graf.readFromFile("graf_for_tests_connected.txt");
        bfs = new BFS(graph);
        assertEquals(true, bfs.getResult());

    }
    @Test
    void getResultFromNotConnectedGraph() throws IOException {
        graph=Graf.readFromFile("graf_for_test_not_connected.txt");
        bfs1 = new BFS(graph);
        assertEquals(false, bfs1.getResult());
    }
}