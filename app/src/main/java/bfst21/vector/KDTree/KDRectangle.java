package bfst21.vector.KDTree;

import bfst21.vector.MapElements.Drawable;
import bfst21.vector.MapElements.MapFeature;
import bfst21.vector.osm.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class KDRectangle implements Drawable {
    private float xmin;
    private float ymin;
    private float xmax;
    private float ymax;

    public KDRectangle(float xmin, float ymin, float xmax, float ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    public float xmin() {
        return xmin;
    }
    public float ymin() {
        return ymin;
    }
    public float xmax() {
        return xmax;
    }
    public float ymax() {
        return ymax;
    }

    public void setXmin(float xmin) {
        this.xmin = xmin;
    }
    public void setYmin(float ymin) {
        this.ymin = ymin;
    }
    public void setXmax(float xmax) {
        this.xmax = xmax;
    }
    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    public boolean intersects(KDRectangle that) {
        return this.xmin <= that.xmax &&
               this.ymin <= that.ymax &&
               that.xmin <= this.xmax &&
               that.ymin <= this.ymin;
    }

    public boolean intersectsX(KDRectangle that) {
        return this.xmin <= that.xmax &&
               this.xmax >= that.xmin;
    }

    public boolean fullyContains(KDRectangle that) {
        return this.xmin <= that.xmin &&
               that.xmin <= that.xmax &&
               that.xmax <= this.xmax &&
               this.ymin <= that.ymin &&
               that.ymin <= that.ymax &&
               that.ymax <= this.ymax;
    }

    public boolean containsMapFeature(MapFeature mf) {
        Node n = mf.getAvgCoord();
        return (n.getX() >= xmin) &&
                (n.getX() <= xmax) &&
                (n.getY() >= ymin) &&
                (n.getY() <= ymax);
    }

    @Override
    public String toString() {
        return "x_min:" + xmin + " ymin:" + ymin + " xmax:" + xmax + " ymax:" + ymax;
    }

    @Override
    public void trace(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineDashes(0.01);
        gc.strokeLine(xmin, ymin, xmin, ymax);
        gc.strokeLine(xmin, ymin, xmax, ymin);
        gc.strokeLine(xmax, ymax, xmin, ymax);
        gc.strokeLine(xmax, ymax, xmax, ymin);
    }
}
