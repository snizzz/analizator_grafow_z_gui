import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Neighbor {
    private final int destination;
    private Color color;
    private final double weight;
    public Neighbor(int destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }
}
