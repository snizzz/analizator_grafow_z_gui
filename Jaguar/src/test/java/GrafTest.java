import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GrafTest {

    Graf graf;

    @Test
    void getRow() throws IOException {
        graf=Graf.readFromFile("graf_for_tests_connected.txt");
        assertEquals(3, graf.getNumberOfRows());
    }

    @Test
    void getColumn() throws IOException {
        graf=Graf.readFromFile("graf_for_tests_connected.txt");
        assertEquals(3,graf.getNumberOfColumns());
    }

    @Test
    void showNeighbors() throws IOException {
        graf=Graf.readFromFile("graf_for_tests_connected.txt");
        String expected = "The 0 node\n" +
                "3\n" +
                "5.648781\n" +
                "1\n" +
                "5.795264\n" +
                "-----\n" +
                "The 1 node\n" +
                "4\n" +
                "5.648781\n" +
                "2\n" +
                "5.795264\n" +
                "0\n" +
                "5.670245\n" +
                "-----\n" +
                "The 2 node\n" +
                "5\n" +
                "5.670245\n" +
                "1\n" +
                "5.700618\n" +
                "-----\n" +
                "The 3 node\n" +
                "4\n" +
                "5.700618\n" +
                "6\n" +
                "5.648781\n" +
                "0\n" +
                "5.795264\n" +
                "-----\n" +
                "The 4 node\n" +
                "7\n" +
                "5.670245\n" +
                "1\n" +
                "5.700618\n" +
                "3\n" +
                "5.648781\n" +
                "5\n" +
                "5.795264\n" +
                "-----\n" +
                "The 5 node\n" +
                "8\n" +
                "5.795264\n" +
                "2\n" +
                "5.670245\n" +
                "4\n" +
                "5.700618\n" +
                "-----\n" +
                "The 6 node\n" +
                "3\n" +
                "5.648781\n" +
                "7\n" +
                "5.795264\n" +
                "-----\n" +
                "The 7 node\n" +
                "4\n" +
                "5.670245\n" +
                "8\n" +
                "5.700618\n" +
                "6\n" +
                "5.648781\n" +
                "-----\n" +
                "The 8 node\n" +
                "7\n" +
                "5.670245\n" +
                "5\n" +
                "5.700618\n" +
                "-----\n";

        assertEquals(expected, graf.showNeighbors());

    }

}