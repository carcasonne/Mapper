package bfst21.vector.osm;

public class Node {
    private float x,y;
    private long id;

    // The latitude and longitude are switched (y,x)
    public Node(long id, float lat, float lon) {
        this.id = id;
        this.x = lon;
        this.y = -lat/0.56f;
    }

    //for use in theoretical coordinate systems (for testing)
    //(x,y)
    public Node(float lon, float lat, boolean useActualInput) {
        this.x = lon;
        if(useActualInput) this.y = lat;
        else this.y = -lat/0.56f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public long getID(){
        return id;
    }

    @Override
    public String toString() {
        return "x" + x + ", y" + y;
    }

    //Euclidean distance
    public double distanceSquaredTo(Node n) {
        if(n == null) return 0.0;
        double dx = this.x - n.getX();
        double dy = this.y - n.getY();
        return dx*dx + dy*dy;
    }

    public boolean equals(Node that) {
        if(that == null) return false;
        return approxEquals(this.x, that.x) && approxEquals(this.y, that.y);
    }

    private boolean approxEquals(float x, float y) {
        return Math.abs(x-y) < 0.000000000000001;
    }
}
