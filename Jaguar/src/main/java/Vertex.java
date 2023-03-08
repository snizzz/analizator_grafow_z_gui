import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Comparator;

public class Vertex {
    private final int id;

    private double posX;
    private double posY;


    private int numberOfNeigbours;
    private final ArrayList<Neighbor> neighbors;

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }



    public int getNumberOfNeigbours() {
        return numberOfNeigbours;
    }

    public int getId() {
        return id;
    }

    public int getNumberOfNeighbors() {
        return numberOfNeigbours;
    }

    public ArrayList<Neighbor> getNeighbors() {
        return neighbors;
    }

    public Vertex(int id,double x,double y){
        this.id = id;
        neighbors = new ArrayList<>();
        numberOfNeigbours=0;
        this.posY=y;
        this.posX=x;
    }
    public void addNeighbor(Neighbor neighbor){
        neighbors.add(neighbor);
        numberOfNeigbours++;
    }


}
