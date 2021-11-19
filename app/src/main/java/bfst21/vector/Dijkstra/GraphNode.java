package bfst21.vector.Dijkstra;

import bfst21.vector.MapElements.Drawable;
import bfst21.vector.osm.Node;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class GraphNode implements Drawable {
    private List<DirectedEdge> edges;
    private float lon;
    private float lat;
    private int id;
    private int maxSpeed;

    public GraphNode(Node position, int maxSpeed) {
        this.lon = position.getX();
        this.lat = position.getY();
        this.edges = new ArrayList<>();
        this.maxSpeed = maxSpeed;
    }

    /** The mathematics and code has been taken from this website: https://www.movable-type.co.uk/scripts/latlong.html
     *  And modified to java
     */
    public static double haversineDistanceBetween(GraphNode v, GraphNode w) {
        double lat1 = Math.toRadians(v.getLatitude()*-0.56f);
        double lat2 = Math.toRadians(w.getLatitude()*-0.56f);

        double deltaLongitude = Math.toRadians(w.getLongitude() - v.getLongitude());
        double deltaLatitude = Math.toRadians(w.getLatitude()*-0.56f - v.getLatitude()*-0.56f);

        int R = 6371; //radius of the Earth in kilometers
        double a = Math.sin(deltaLatitude/2) * Math.sin(deltaLatitude/2) +
                   Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLongitude/2) * Math.sin(deltaLongitude/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    //Calculate the Euclidian Distance between node v and w
    public static double euclidianDistanceBetween(GraphNode v, GraphNode w) {
        double v1 = v.getLongitude(), v2 = v.getLatitude();
        double w1 = w.getLongitude(), w2 = w.getLatitude();

        double dx = Math.abs(v1-w1);
        double dy = Math.abs(v2-w2);

        return Math.sqrt(dx*dx + dy*dy);
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public float getLongitude() {
        return lon;
    }

    public float getLatitude() {
        return lat;
    }

    public int getID() {
        return id;
    }

    public List<DirectedEdge> getEdges() {
        return edges;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object that) {
        if(that == null) return false;
        if(!(that instanceof GraphNode)) return false;

        GraphNode w = (GraphNode) that;

        if(approxEquals(this.lat, w.lat) && approxEquals(this.lon, w.lon)) return true;
        return false;
    }

    public boolean approxEquals(float a, float b) {
        return Math.abs(a-b) < 0.0000000000001;
    }

    @Override
    public void trace(GraphicsContext gc) {
        gc.strokeLine(lon-0.00001, lat, lon+0.00001, lat);
        gc.strokeLine(lon, lat-0.00001, lon, lat+0.00001);
    }
}
