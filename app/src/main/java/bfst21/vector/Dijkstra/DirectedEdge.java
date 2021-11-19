package bfst21.vector.Dijkstra;

import bfst21.vector.MapElements.Drawable;
import javafx.scene.canvas.GraphicsContext;

public class DirectedEdge implements Comparable<DirectedEdge>, Drawable {
    private GraphNode v;   // The tail vertex
    private GraphNode w;   // The head vertex
    private double weight; //Weight of the directed edge
    private String roadName;
    private int maxSpeed;
    private boolean isCarway;
    private boolean isFootway;
    private boolean isBikeway;
    private boolean isOneWay;
    private boolean isBeingDriven;

    public DirectedEdge(GraphNode v, GraphNode w){
        this.v = v;
        this.w = w;
        this.weight = createWeight();
    }

    public void setIsCarway() {
        this.isCarway = true;
    }

    public void setIsFootway() {
        this.isFootway = true;
    }

    public void setIsBikeway() {
        this.isBikeway = true;
    }

    public void setBeingDriven(boolean isBeingDriven) {
        this.isBeingDriven = isBeingDriven;
    }

    public void setOneWay(boolean isOneWay) {
        this.isOneWay = isOneWay;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public boolean hasTransportType(Dijkstra.TransportType transportType) {
        if(transportType.equals(Dijkstra.TransportType.ALL))               return true;
        if(transportType.equals(Dijkstra.TransportType.CAR)  && isCarway)  return true;
        if(transportType.equals(Dijkstra.TransportType.WALK) && isFootway) return true;
        if(transportType.equals(Dijkstra.TransportType.BIKE) && isBikeway) return true;
        return false;
    }

    private double createWeight() {
        return GraphNode.haversineDistanceBetween(v, w);
    }

    // Returns the vertex v
    public GraphNode from(){
        return v;
    }

    //Returns where it needs to go to
    public GraphNode to(){
        return w;
    }

    //Return the weight
    public double weight(){
        return weight;
    }

    public String getRoadName(){ return roadName;}

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public boolean getIsBeingDriven() { return isBeingDriven; }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setName(String name) {
        this.roadName = name;
    }

    public String toString(){
        return v + " -> " + w + " " +String.format("%5.2f", weight);
    }

    public int compareTo(DirectedEdge that) {
        if(this.weight > that.weight) return 1;
        if(this.weight < that.weight) return -1;
        return 0;
    }
    
    @Override
    public boolean equals(Object that) {
        if(that == null) return false;
        if(!(that instanceof DirectedEdge)) return false;

        DirectedEdge thatEdge = (DirectedEdge) that;

        if(this.v.equals(thatEdge.v) &&
           this.w.equals(thatEdge.w)) return true;
        else return false;
    }

    @Override
    public void trace(GraphicsContext gc) {
        gc.strokeLine(v.getLongitude(), v.getLatitude(), w.getLongitude(), w.getLatitude());
    }
}
