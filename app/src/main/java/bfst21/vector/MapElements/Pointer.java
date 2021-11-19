package bfst21.vector.MapElements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Pointer implements Drawable {
    private double x;
    private double y;
    private String type;
    private Image iconImage;
    private boolean verySmall;
    private boolean small;
    private boolean medium;
    private boolean big;
    private boolean fat;

    public Pointer(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;

        try {
            iconImage = new Image("bfst21/vector/Pointers/favorite_icon.png");
            if(type.equals("Work")) iconImage = new Image("bfst21/vector/Pointers/work_icon.png");
            if(type.equals("Home")) iconImage = new Image("bfst21/vector/Pointers/home_icon.png");
        } catch (Exception ignored){}
    }

    public String getType() {
        return type;
    }

    public void setVerySmall() {
        verySmall = true;
        small     = false;
        medium    = false;
        big       = false;
        fat       = false;
    }

    public void setSmall() {
        verySmall = false;
        small     = true;
        medium    = false;
        big       = false;
        fat       = false;
    }

    public void setMedium() {
        verySmall = false;
        small     = false;
        medium    = true;
        big       = false;
        fat       = false;
    }

    public void setBig() {
        verySmall = false;
        small     = false;
        medium    = false;
        big       = true;
        fat       = false;
    }

    public void setFat() {
        verySmall = true;
        small     = false;
        medium    = false;
        big       = false;
        fat       = true;
    }

    @Override
    public void trace(GraphicsContext gc) {
        if(type.equals("StartCircle")) {
            gc.setFill(Color.GREEN);
            gc.fillOval(x, y, 0.001, 0.001);
        } else if(type.equals("EndCircle")) {
            gc.setFill(Color.RED);
            gc.fillOval(x, y, 0.001, 0.001);
        } else {
            double imageSizeProportion = 0;

            if(verySmall) imageSizeProportion = 0.001;
            if(small)     imageSizeProportion = 0.005;
            if(medium)    imageSizeProportion = 0.01;
            if(big)       imageSizeProportion = 0.04;
            if(fat)       imageSizeProportion = 0.08;

            gc.drawImage(iconImage, x-imageSizeProportion/2, y-imageSizeProportion/2, imageSizeProportion, imageSizeProportion);
        }
    }
}