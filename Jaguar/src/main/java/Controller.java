import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    public AnchorPane graphAp;
    @FXML
    public Canvas graphCanvas;
    @FXML
    public ScrollPane graphSp;
    @FXML
    public ImageView colorScaleView;
    @FXML
    public Label maxScale;
    @FXML
    public Label minScale;
    @FXML
    public ScrollPane spPaths;
    @FXML
    public ListView<AnchorPane> listPaths;
    @FXML
    public Button addPathButton;
    @FXML
    public TextField startPath;
    @FXML
    public TextField endPath;
    @FXML
    private double min = 0;
    @FXML
    private double max = 1;
    @FXML
    private TextField minimum;
    @FXML
    private TextField maximum;
    @FXML
    private Text Connected;
    @FXML
    private TextField Rows;
    @FXML
    private TextField Cols;
    @FXML
    private Text errors;
    private Image colorScale;
    private String messages= "";
    private boolean all_verticles = false;
    private boolean connected = false;
    private GraphicsContext g;
    private double sizeOFElementInGraph;
    private boolean clickingPath;
    private double posXLastPicked;
    private double posYLastPicked;
    private int col = 2;
    private int rows = 2;
    private Graf graph;
    private BFS bfs;
    private final ArrayList<Path> arrayOfPaths = new ArrayList<>();
    private final ObservableList<AnchorPane> paneOfPaths = FXCollections.observableArrayList();

    public void addingPath(ActionEvent actionEvent) {
        if(graph==null){
            messages+="\n Generate or load graph first\n";
            errors.setText(messages);
            return;
        }
        if(startPath.getText().trim().isEmpty()||endPath.getText().trim().isEmpty()){
            messages+="\n Enter start and end Vertex to add a path to graph\n";
            return;
        }
        int start ,end;
        try{
            start=Integer.parseInt(startPath.getText());
            end=Integer.parseInt(endPath.getText());
        }catch (NumberFormatException e){
            messages+=" Start and end must be integers in: <0,"+graph.getNumberOfColumns()*graph.getNumberOfRows()+")\n";
            errors.setText(messages);
            return;
        }
        if(start<0||start>=graph.getNumberOfColumns()*graph.getNumberOfRows()||end<0||end>=graph.getNumberOfColumns()*graph.getNumberOfRows()){
            messages+=" Start and end must be integers in: <0,"+graph.getNumberOfColumns()*graph.getNumberOfRows()+")\n";
            errors.setText(messages);
        }
        else{
            Path p = new Path(new Dijkstra(graph,start,end));
            arrayOfPaths.add(p);
            showPath(Color.web("#FFFFFF"),p);
        }
    }

    private class Path{
        private final int start;
        private final int end;
        private final ArrayList<Pair<Integer,Double>> path;

        private AnchorPane pane;

        Dijkstra dijkstra;

        Path(Dijkstra d){
            this.dijkstra=d;
            this.start=d.getStart();
            this.end=d.getEnd();
            this.path=d.pathToEnd();
            createPathPane();
        }

        public void createPathPane(){
            int start = this.start;
            pane = new AnchorPane();
            Rectangle r = new Rectangle(136,136);
            r.setFill(Color.web("#808080"));
            Label startLabel = new Label("start: "+Integer.toString(this.start));
            Label endLabel = new Label("end: "+Integer.toString(this.end));
            Label distance;
            if(path.isEmpty()) {
                distance = new Label("distance: " + "-");
                messages+="\nPATH BETWEEN "+start+" AND "+end+" DOESN'T EXIST\n";
                errors.setText(messages);
            }else{
                distance = new Label("distance: " + Double.toString(path.get(0).getValue()));
            }
            Button delete = new Button("Delete");
            delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    remove();
                    paneOfPaths.remove(pane);
                    fixGraphAfterDelete();
                }
            });
            Button showDetails = new Button("ShowDetails");
            showDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    messages+="FULL DETAILS OF PATH:\n"+Integer.toString(start);
                    for(int i=path.size()-1;i>=0;i--){
                        messages+="--"+path.get(i).getValue()+"->"+path.get(i).getKey();
                    }
                    messages+="\n";
                    errors.setText(messages);
                }

            });
            pane.getChildren().add(r);
            r.setLayoutX(2);
            r.setLayoutY(6);
            pane.setPadding(new Insets(5,5,5,5));
            pane.getChildren().addAll(startLabel,endLabel,delete,distance,showDetails);
            startLabel.setLayoutX(13);
            startLabel.setLayoutY(20);
            startLabel.setTextFill(Color.web("#ffffff"));
            endLabel.setLayoutX(13);
            endLabel.setLayoutY(37);
            endLabel.setTextFill(Color.web("#ffffff"));
            distance.setLayoutX(13);
            distance.setLayoutY(55);
            distance.setTextFill(Color.web("#ffffff"));
            showDetails.setLayoutX(13);
            showDetails.setLayoutY(74);
            showDetails.setPrefHeight(25);
            showDetails.setPrefWidth(86);
            delete.setLayoutX(13);
            delete.setLayoutY(106);
            delete.setPrefHeight(25);
            delete.setPrefWidth(86);


            paneOfPaths.add(pane);

        }
        public void remove(){
            paneOfPaths.remove(pane);
            arrayOfPaths.remove(this);
            fixGraphAfterDelete();
        }

        private void fixGraphAfterDelete(){

            showPath(Color.BLACK,this);
            for(Neighbor n:graph.getVertexArray().get(start).getNeighbors()){
                int dest = n.getDestination();
                double posXstart;
                double posYstart;
                double posXend;
                double posYend;
                g.setStroke(n.getColor());
                g.setLineWidth(sizeOFElementInGraph / 4);

                if (graph.getVertexArray().get(start).getPosX() > graph.getVertexArray().get(dest).getPosX()) {
                    posXstart = graph.getVertexArray().get(start).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + 4.0 * sizeOFElementInGraph / 2.0 + sizeOFElementInGraph / 8;
                    posYstart = graph.getVertexArray().get(start).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;

                } else if (graph.getVertexArray().get(start).getPosX() < graph.getVertexArray().get(dest).getPosX()) {
                    posXstart = graph.getVertexArray().get(start).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) - sizeOFElementInGraph / 8;
                    posYstart = graph.getVertexArray().get(start).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                } else if (graph.getVertexArray().get(start).getPosY() > graph.getVertexArray().get(dest).getPosY()) {
                    posXstart =graph.getVertexArray().get(start).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYstart = graph.getVertexArray().get(start).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + 2.0 * sizeOFElementInGraph + sizeOFElementInGraph / 8;
                } else {
                    posXstart = graph.getVertexArray().get(start).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYstart = graph.getVertexArray().get(start).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph - sizeOFElementInGraph / 8;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph);
                }
                g.strokeLine(posXstart,
                        posYstart,
                        posXend,
                        posYend);
            }
            g.setFill(Color.web("#808080"));
            g.fillOval(graph.getVertexArray().get(start).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    graph.getVertexArray().get(start).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    sizeOFElementInGraph,sizeOFElementInGraph);

            for(int i=path.size()-1;i>=0;i--){
                for(Neighbor n:graph.getVertexArray().get(path.get(i).getKey()).getNeighbors()){
                    int dest = n.getDestination();
                    double posXstart;
                    double posYstart;
                    double posXend;
                    double posYend;
                    g.setStroke(n.getColor());
                    g.setLineWidth(sizeOFElementInGraph / 4);

                    if (graph.getVertexArray().get(path.get(i).getKey()).getPosX() > graph.getVertexArray().get(dest).getPosX()) {
                        posXstart = graph.getVertexArray().get(path.get(i).getKey()).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + 4.0 * sizeOFElementInGraph / 2.0 + sizeOFElementInGraph / 8;
                        posYstart = graph.getVertexArray().get(path.get(i).getKey()).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;

                    } else if (graph.getVertexArray().get(path.get(i).getKey()).getPosX() < graph.getVertexArray().get(dest).getPosX()) {
                        posXstart = graph.getVertexArray().get(path.get(i).getKey()).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) - sizeOFElementInGraph / 8;
                        posYstart = graph.getVertexArray().get(path.get(i).getKey()).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    } else if (graph.getVertexArray().get(path.get(i).getKey()).getPosY() > graph.getVertexArray().get(dest).getPosY()) {
                        posXstart =graph.getVertexArray().get(path.get(i).getKey()).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posYstart = graph.getVertexArray().get(path.get(i).getKey()).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + 2.0 * sizeOFElementInGraph + sizeOFElementInGraph / 8;
                    } else {
                        posXstart = graph.getVertexArray().get(path.get(i).getKey()).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                        posYstart = graph.getVertexArray().get(path.get(i).getKey()).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph - sizeOFElementInGraph / 8;
                        posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph);
                    }
                    g.strokeLine(posXstart,
                            posYstart,
                            posXend,
                            posYend);
                }
                g.setFill(Color.web("#808080"));
                g.fillOval(graph.getVertexArray().get(path.get(i).getKey()).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                        graph.getVertexArray().get(path.get(i).getKey()).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                        sizeOFElementInGraph,sizeOFElementInGraph);
            }
            if(path.isEmpty()){
                g.setFill(Color.web("#808080"));
                g.fillOval(graph.getVertexArray().get(end).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                        graph.getVertexArray().get(end).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                        sizeOFElementInGraph,sizeOFElementInGraph);
            }

            for(Path p:arrayOfPaths){
                showPath(Color.web("#ffffff"),p);
            }
        }
    }


    public void setConnected(ActionEvent actionEvent) {
        connected = !connected;
    }
    public void setAllVerticles(ActionEvent actionEvent) {
        all_verticles = !all_verticles;
    }
    public void setUp(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/Main.fxml")));
            primaryStage.setTitle("Jaguar");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
    public void generate(ActionEvent actionEvent) throws Exception {

        if(Rows.getText().trim().isEmpty()||Cols.getText().trim().isEmpty()){
            messages+=" Enter number of vertexes to generate graph\n";
        }
        if(minimum.getText().trim().isEmpty()||maximum.getText().trim().isEmpty()){
            messages+=" Enter a range of weights\n";
        }
        try{
            col = Integer.parseInt(Cols.getText());
            rows = Integer.parseInt(Rows.getText());
            min=Integer.parseInt(minimum.getText());
            max=Integer.parseInt(maximum.getText());
        }catch (NumberFormatException e){
            messages+=" Weight range must be real number greater than 0 and number of columns and rows must be integers greater than 1\n";
            errors.setText(messages);
        }
        if(rows<2||col<2) {
            messages += " Number of columns and rows must be integers greater than 1\n";
            errors.setText(messages);
            return;
        }
        if(min<0||max<0){
            messages+=" Min and max must be real number greater than 0\n";
            errors.setText(messages);
            return;
        }else {

            Generator generator = new Generator();
            if (Double.parseDouble(minimum.getText()) < 0) {
                messages += "Weight cannot be negative\n";
                errors.setText(messages);
            } else min = Double.parseDouble(minimum.getText());
            if (Double.parseDouble(maximum.getText()) < 0) {
                messages += "Weight cannot be negative\n";
                errors.setText(messages);
                max = min + 1;
            } else if (Double.parseDouble(maximum.getText()) <= min) {
                messages += "Maximum weight cannot be smaller/or equal than/to minimum weight\n";
                errors.setText(messages);
                max = min + 1;
            } else max = Double.parseDouble(maximum.getText());
            if (Integer.parseInt(Rows.getText()) <= 0) {
                messages += "Number of Columns cannot be negative or eqaul to 0\n";
                errors.setText(messages);
            } else rows = Integer.parseInt(Rows.getText());
            if (Integer.parseInt(Cols.getText()) <= 0) {
                messages += "Number of Rows cannot be negative or equal to 0\n";
                errors.setText(messages);
            } else col = Integer.parseInt(Cols.getText());
            if (all_verticles && connected) {
                graph = generator.tlw(col, rows, min, max, 1);
                assignColorToEdges(min, max);
                showGraph();
            } else if (!all_verticles && connected) {

                graph = generator.tlw(col, rows, min, max, 0.8);
                bfs = new BFS(graph);
                while (!bfs.getResult()) {
                    graph = generator.tlw(col, rows, min, max, 0.8);
                    bfs = new BFS(graph);
                }
                assignColorToEdges(min, max);
                showGraph();
            } else if (!all_verticles) {
                graph = generator.tlw(col, rows, min, max, 0.5);
                assignColorToEdges(min, max);
                showGraph();
            }
            showScale(min, max);
            makeCanvasClickable();
            bfs = new BFS(graph);
            if (bfs.getResult()) Connected.setText("CONNECTED: YES");
            else Connected.setText("CONNECTED: NO");
            messages += "Graph was generated with: \n" + "Weight range from " + min + " to " + max + " Number of rows: " + col + " Number of columns: " + rows + " Connected: " + connected + " All edges: " + all_verticles + "\n";
            errors.setText(messages);
        }
    }

    public void load(ActionEvent actionEvent) throws IOException {
        graphCanvas.setHeight(522);
        graphCanvas.setWidth(522);
        clickingPath=false;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".txt Files", "*.txt"));
            graph=Graf.readFromFile(String.valueOf(fileChooser.showOpenDialog(null)));
            bfs = new BFS(graph);
            if(bfs.getResult()) Connected.setText("CONNECTED: YES");
            else Connected.setText("CONNECTED: NO");
            messages += "File was loaded successfully\n";
            errors.setText(messages);

        }
        catch(IOException | RuntimeException e){
            messages += "Couldn't load the file\n";
            errors.setText(messages);
        }
        double maxi=graph.getVertexArray().get(0).getNeighbors().get(0).getWeight();
        double mini=graph.getVertexArray().get(0).getNeighbors().get(0).getWeight();

        for(Vertex v:graph.getVertexArray()){
           for(Neighbor n:v.getNeighbors()){
               if(n.getWeight()<mini) mini=n.getWeight();
               if(n.getWeight()>maxi) maxi=n.getWeight();
           }
        }
        min=mini;
        max=maxi;
        assignColorToEdges(min,max);
        showGraph();
        showScale(min,max);
        makeCanvasClickable();
    }

    public void save(ActionEvent actionEvent) {
        if(graph==null){
            messages+="\n Generate or load graph first\n";
            errors.setText(messages);
            return;
        }
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".txt Files", "*.txt"));
            graph.writeToFile(String.valueOf(fileChooser.showSaveDialog(null)));
            messages+= "File saved successfully\n";
            errors.setText(messages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteErrors(ActionEvent actionEvent) {
        messages = "";
        errors.setText(messages);
    }



    public void showGraph(){
        g=graphCanvas.getGraphicsContext2D();

        graphSp.setStyle("-fx-background-color:grey; -fx-border-color:grey;");
        if(graph.getNumberOfColumns()>graph.getNumberOfRows()){
            sizeOFElementInGraph=graphCanvas.getWidth()/(double)(graph.getNumberOfColumns()*2);

        }
        else{
            sizeOFElementInGraph=graphCanvas.getHeight()/(((double)(graph.getNumberOfRows()*2)));
        }
        if(sizeOFElementInGraph<7){
            sizeOFElementInGraph=7.0;
            graphCanvas.setWidth(sizeOFElementInGraph*((double)graph.getNumberOfColumns()*2));
            graphCanvas.setHeight(sizeOFElementInGraph*((double)(graph.getNumberOfRows()*2)));
        }

        graphCanvas.setStyle("-fx-background: #000000; -fx-border-color: #000000;");

        g.setFill(Color.BLACK);
        g.fillRect(0,0,graphCanvas.getWidth(),graphCanvas.getHeight());

        for(Vertex v:graph.getVertexArray()) {
            for (Neighbor n : v.getNeighbors()) {
                int dest = n.getDestination();
                double posXstart;
                double posYstart;
                double posXend;
                double posYend;
                g.setStroke(n.getColor());
                g.setLineWidth(sizeOFElementInGraph / 4);

                if (v.getPosX() > graph.getVertexArray().get(dest).getPosX()) {
                    posXstart = v.getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + 4.0 * sizeOFElementInGraph / 2.0 + sizeOFElementInGraph / 8;
                    posYstart = v.getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;

                } else if (v.getPosX() < graph.getVertexArray().get(dest).getPosX()) {
                    posXstart = v.getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) - sizeOFElementInGraph / 8;
                    posYstart = v.getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                } else if (v.getPosY() > graph.getVertexArray().get(dest).getPosY()) {
                    posXstart = v.getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYstart = v.getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph) + 2.0 * sizeOFElementInGraph + sizeOFElementInGraph / 8;
                } else {
                    posXstart = v.getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posXend = graph.getVertexArray().get(dest).getPosX() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph;
                    posYstart = v.getPosY() * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph - sizeOFElementInGraph / 8;
                    posYend = graph.getVertexArray().get(dest).getPosY() * 2.0 * (sizeOFElementInGraph);
                }
                g.strokeLine(posXstart,
                        posYstart,
                        posXend,
                        posYend);

            }
        }
        for(Vertex v:graph.getVertexArray()){
            g.setFill(Color.web("#808080"));
            g.fillOval(v.getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    v.getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    sizeOFElementInGraph,sizeOFElementInGraph);
        }
    }

    private void makeCanvasClickable(){
        graphCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double posX=event.getX();
                double posY=event.getY();
                double posXofVertex=Math.round(((posX-(posX%sizeOFElementInGraph))/(sizeOFElementInGraph))*100.0)/100.0;
                double posYofVertex=Math.round(((posY-(posY%sizeOFElementInGraph))/(sizeOFElementInGraph))*100.0)/100.0;
                if(posXofVertex%2!=0)
                    posXofVertex=(posXofVertex-1)/2;
                else
                    posXofVertex=(posXofVertex)/2;
                if(posYofVertex%2!=0)
                    posYofVertex=(posYofVertex-1)/2;
                else
                    posYofVertex=(posYofVertex)/2;
                g.setFill(Color.web("#FFFFFF"));
                g.fillOval(posXofVertex *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                        posYofVertex *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                        sizeOFElementInGraph,sizeOFElementInGraph);
                if(clickingPath){
                    Dijkstra d = new Dijkstra(graph,(int)(posXLastPicked+posYLastPicked*graph.getNumberOfColumns()),(int)(posXofVertex+posYofVertex*graph.getNumberOfColumns()));
                    Path p = new Path(d);
                    arrayOfPaths.add(p);
                    showPath(Color.web("#FFFFFF"),p);
                }
                clickingPath=!clickingPath;
                posXLastPicked=posXofVertex;
                posYLastPicked=posYofVertex;
            }
        });
    }

    private void showPath(Color color,Path path) {

        double lasPosX=graph.getVertexArray().get(path.start).getPosX();
        double lasPosY=graph.getVertexArray().get(path.start).getPosY();
        g.setStroke(color);
        g.setFill(color);
        if(color==Color.BLACK) g.setLineWidth(sizeOFElementInGraph / 3);
        else g.setLineWidth(sizeOFElementInGraph / 4);
        for(int i=path.path.size()-1;i>=0;i--){
            g.strokeLine(lasPosX * 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph,
                    lasPosY* 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph,
                    graph.getVertexArray().get(path.path.get(i).getKey()).getPosX()* 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph,
                    graph.getVertexArray().get(path.path.get(i).getKey()).getPosY()* 2.0 * (sizeOFElementInGraph) + sizeOFElementInGraph);
            if(color==Color.BLACK)
                g.fillOval(graph.getVertexArray().get(path.path.get(i).getKey()).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    graph.getVertexArray().get(path.path.get(i).getKey()).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    sizeOFElementInGraph+3,sizeOFElementInGraph+3);
            else
                g.fillOval(graph.getVertexArray().get(path.path.get(i).getKey()).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    graph.getVertexArray().get(path.path.get(i).getKey()).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    sizeOFElementInGraph,sizeOFElementInGraph);

            lasPosX=graph.getVertexArray().get(path.path.get(i).getKey()).getPosX();
            lasPosY=graph.getVertexArray().get(path.path.get(i).getKey()).getPosY();
        }
        if(color==Color.BLACK)
            g.fillOval(graph.getVertexArray().get(path.start).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                graph.getVertexArray().get(path.start).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                sizeOFElementInGraph+3,sizeOFElementInGraph+3);
        else
            g.fillOval(graph.getVertexArray().get(path.start).getPosX() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    graph.getVertexArray().get(path.start).getPosY() *2.0*(sizeOFElementInGraph)+sizeOFElementInGraph/2,
                    sizeOFElementInGraph,sizeOFElementInGraph);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clickingPath=false;
        showScale(0,1);
        listPaths.setItems(paneOfPaths);
    }

    public void showScale(double min, double max) {
        WritableImage scale = new WritableImage((int)colorScaleView.getFitWidth(),(int)colorScaleView.getFitHeight());
        PixelWriter pixelWriter = scale.getPixelWriter();
        for(int i=0;i<(int)colorScaleView.getFitWidth();i++){
            double v = min + (max-min)*i/colorScaleView.getFitWidth();
            Color color = assignColor(min,max,v);
            for(int j=0;j<(int)colorScaleView.getFitHeight();j++){
                pixelWriter.setColor(i,j,color);
            }
        }
        minScale.setText(Double.toString(min));
        maxScale.setText(Double.toString(max));
        colorScale=scale;
        colorScaleView.setImage(colorScale);
    }

    public Color assignColor(double min, double max, double v){
        return Color.hsb(Color.BLUE.getHue() + (Color.RED.getHue() - Color.BLUE.getHue()) * (v-min)/(max-min), 1.0, 1.0);
    }
    public void assignColorToEdges(double min, double max){
        for(Vertex v:graph.getVertexArray()){
            for(Neighbor n:v.getNeighbors()){
                n.setColor(assignColor(min,max,n.getWeight()));
            }
        }
    }
}
