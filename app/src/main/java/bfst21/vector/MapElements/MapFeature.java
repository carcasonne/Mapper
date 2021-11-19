package bfst21.vector.MapElements;

import bfst21.vector.Dijkstra.Coordinate;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;

public class MapFeature implements Comparable<MapFeature> {
    protected Way way;
    protected String type;
    protected boolean kdTreeEven = true;
    private final double MARGIN = 0.00000000000001;
    private Node avgCoordinate = null;

    public MapFeature(Way way, String type){
        this.way = way;
        this.type = type;
    }

    public Node getAvgCoord() {
        if(avgCoordinate == null) createAvgCoord();
        return avgCoordinate;
    }

    public void setAvgCoord(Node newCoord) {
        avgCoordinate = newCoord;
    }

    //Finding the average node in the way
    public void createAvgCoord() {
        if(way == null) return;

        float sumX = 0, sumY = 0, avgX = 0, avgY = 0;

        for(Node n : way.getNodes()) {
            sumX += n.getX();
            sumY += n.getY();
        }

        avgX = sumX / way.size();
        avgY = sumY / way.size();
        avgY *= -0.56f;                 //counterbalance to Node's constructor's manipulation of longitude

        avgCoordinate = new Node(0, avgY, avgX);
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public Way getWay() {
        return way;
    }

    public void setEven(boolean even) {
        kdTreeEven = even;
    }

    public boolean isEven() {
        return kdTreeEven;
    }

    //Finding the nearest node to the inserted coordinate
    public Node nearestNodeToPoint(Coordinate coord) {
        Node queryNode = new Node(coord.x(), coord.y(), true);
        Node nearestNode = new Node((float) Double.POSITIVE_INFINITY, (float) Double.POSITIVE_INFINITY, true);
        for(Node n : way.getNodes()) {
            double distanceToNewNode = queryNode.distanceSquaredTo(n);
            double distanceToOldNode = queryNode.distanceSquaredTo(nearestNode);
            if(distanceToOldNode > distanceToNewNode) {
                nearestNode = n;
            }
        }
        return nearestNode;
    }

    //based on average coordinates
    public int compareTo(MapFeature that) {
        if(that == null) return 1;
        Node thisN = getAvgCoord(), thatN = that.getAvgCoord();

        if(kdTreeEven) {
            if(approxEquals(thisN.getX(), thatN.getX())) return 0;
            if(thisN.getX() < thatN.getX()) return -1;
            if(thisN.getX() > thatN.getX()) return 1;
        } else {
            if(approxEquals(thisN.getY(), thatN.getY())) return 0;
            if(thisN.getY() < thatN.getY()) return -1;
            if(thisN.getY() > thatN.getY()) return 1;
        }
        return 0;
    }

    private boolean approxEquals(float x, float y) {
        return Math.abs(x-y) < MARGIN;
    }
}
