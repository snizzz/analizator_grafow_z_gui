import java.util.ArrayList;
import java.util.Random;

public class Generator {
    double weight_precision = 10000.0;
    private Graf graph;
    public Graf tlw(int w, int k, double min, double max, double probability) throws Exception {
        if(max<min) throw new Exception("Wartosc max powinna byc wieksza od min");
        else if (min < 0 || max <0) throw new Exception("Wartosci nie moga byc mniejsze od zera");
        graph = new Graf(w,k);
        Random generator = new Random();
        for(int i =0; i<w;i++){
            for(int j =0; j<k;j++){
                int dest=i*k+j;
                if(i==0 && j==0){
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,j+1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(i==0 && j==k-1){
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,j-1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k+j, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(w*k-k == k*i+j){
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest, k*i+1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i-k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(w*k-1 == k*i+j){
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i-1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j-1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(j==0){
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i-k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(i==0){
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,j-1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,j+1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k+j, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(j==k-1) {
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i-1+j, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j-k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+k+j, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else if(i == w-1) {
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j-1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j+1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j-k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }
                else {
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j+1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j-1, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j-k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                    if(generator.nextDouble()<probability)
                        addEdgeForGraf( dest,k*i+j+k, (double)Math.round((min + (max-min)*generator.nextDouble())*weight_precision)/weight_precision);
                }

            }
        }
        return graph;
    }

    private void addEdgeForGraf(int vertex,int dest, double weight){
        graph.addEdge(vertex,new Neighbor(dest,weight));
    }
}
