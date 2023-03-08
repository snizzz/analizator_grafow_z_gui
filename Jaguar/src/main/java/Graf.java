import java.io.*;
import java.util.*;

public class Graf {
    private int numberOfRows;
    private int numberOfColumns;
    private final ArrayList<Vertex> vertexArray;

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public ArrayList<Vertex> getVertexArray() {
        return vertexArray;
    }

    public Graf(int numberOfRows,int numberOfColumns){
        this.numberOfRows=numberOfRows;
        this.numberOfColumns=numberOfColumns;
        this.vertexArray = new ArrayList<>(numberOfColumns * numberOfRows);
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++) {
                this.vertexArray.add(new Vertex(i*numberOfColumns+j,(double)j,(double)i));
            }
        }
    }

    public void addVertex(Vertex v){
        vertexArray.add(v);
    }
    public void addEdge(int i,Neighbor n){vertexArray.get(i).addNeighbor(n);}
    public static Graf readFromFile(String filename) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String [] lines = reader.readLine().split(" ");
        Graf graph = new Graf(Integer.parseInt(lines[0]),Integer.parseInt(lines[1]));
        String line;
        Scanner scanner;
        int i=0;
        int j=0;
        while((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter(" :|\\s* ");
            scanner.useLocale(Locale.US);
            while(scanner.hasNext()){
                int dest = scanner.nextInt();
                double weight = scanner.nextDouble();
                if(weight<0) throw new IOException("Waga nie moze byc ujemna");
                graph.getVertexArray().get(i).addNeighbor(new Neighbor(dest,weight));
                j++;
            }
            i++;
        }
        reader.close();
        return graph;
    }

    public String showNeighbors() {
        String string = "";
        for(int i = 0; i< vertexArray.size(); i++) {
            string +="The " + i  + " node\n";
            for(int j = 0; j< vertexArray.get(i).getNumberOfNeighbors(); j++) {
                string += vertexArray.get(i).getNeighbors().get(j).getDestination() + "\n";
                string += vertexArray.get(i).getNeighbors().get(j).getWeight() + "\n";
            }
            string+="-----\n";

        }
        return string;
    }

    public void writeToFile(String filename) throws IOException {
        File file = new File(filename);
        if(!file.exists()) file.createNewFile();
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf("%d %d", getNumberOfRows(), getNumberOfColumns());
        printWriter.printf("%n");
        for (Vertex vertex : vertexArray) {
            printWriter.printf("     ");
            for (int j = 0; j < vertex.getNumberOfNeighbors(); j++) {
                printWriter.printf(Locale.US, "%d :%f  ", vertex.getNeighbors().get(j).getDestination(), vertex.getNeighbors().get(j).getWeight());
            }
            printWriter.printf("%n");
        }
        printWriter.close();
        fileWriter.close();
    }

}
