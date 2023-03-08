import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    @Test
    public void distanceToEveryVertexTest() throws IOException {
        Graf g = Graf.readFromFile("graf_for_tests_connected.txt");
        Dijkstra d = new Dijkstra(g,0,12);
        HashMap<Integer,Double> h1 = d.distanceToEveryVertex();
        HashMap<Integer,Double> h2 = new HashMap<>();
        h2.put(0,0.0);
        h2.put(1,5.795264);
        h2.put(2,11.590528);
        h2.put(3,5.648781);
        h2.put(4,11.349399);
        h2.put(5,17.144663);
        h2.put(6,11.297562);
        h2.put(7,17.019644);
        h2.put(8,22.720261999999998);
        System.out.println(h1);
        assertEquals(h2,h1);
    }

    @Test
    public void pathToEndTest() throws IOException {
        Graf g = Graf.readFromFile("graf_for_tests_connected.txt");
        Dijkstra d = new Dijkstra(g,0,8);
        ArrayList<Pair<Integer,Double>> h1 = d.pathToEnd();
        ArrayList<Pair<Integer,Double>> h2= new ArrayList<>();
        h2.add(new Pair<>(8,22.7203));
        h2.add(new Pair<>(7,17.0196));
        h2.add(new Pair<>(4,11.3494));
        h2.add(new Pair<>(3,5.6488));
        System.out.println(h1);
        assertEquals(h2,h1);
    }
}