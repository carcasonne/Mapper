package bfst21.vector.Dijkstra;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private float x;
    private float y;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }


    public boolean equals(Coordinate that) {
        if(that == null) return false;
        if(approxEquals(this.x, that.x) && approxEquals(this.y, that.y)) return true;
        return false;
    }

    private boolean approxEquals(float a, float b) {
        return Math.abs(a-b) < 0.00000001;
    }

    public String toString() {
        return "x" + x + ", y" + y;
    }
}
