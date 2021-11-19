package bfst21.vector.MapElements;

import bfst21.vector.osm.Way;
// TODO: Getters and setters
// TODO: if statement in constructor.
public class Road extends MapFeature{
    private int MaxSpeed;
    private String roadName;
    private boolean footWay;
    private boolean cycleWay;
    private boolean carWay;
    private boolean isBridge;
    private boolean isOneWay;

    public Road(Way way, String type){
        super(way, type);
        footWay = true;
        cycleWay = true;
        carWay = true;
        isOneWay = false;
        if(type.equals("motorway")) {
            setMaxSpeed(130);
            footWay = false;
            cycleWay = false;
            carWay = true;
        }
        if(type.equals("secondary") || type.equals("tertiary")) {
            setMaxSpeed(80);
            carWay = true;
        }
        if(type.equals("residential")) {
            setMaxSpeed(50);
            carWay = true;
        }
        if(type.equals("pedestrian") ||
           type.equals("footway")    ||
           type.equals("bridleway")  ||
           type.equals("cycleway")   ||
           type.equals("path")       ||
           type.equals("steps")      ||
           type.equals("track")) {
            carWay = false;
        }
    }

    public void setRoadName(String input){
        roadName = input;
    }

    public String getRoadName(){
        return roadName;
    }

    public void setOneWay(boolean isOneWay) {
        this.isOneWay = isOneWay;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public int getMaxSpeed() {
        return MaxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        MaxSpeed = maxSpeed;
    }

    public boolean isFootWay() {
        return footWay;
    }

    public void setFootWay(boolean footWay) {
        this.footWay = footWay;
    }

    public boolean isCycleWay() {
        return cycleWay;
    }

    public void setCycleWay(boolean cycleWay) {
        this.cycleWay = cycleWay;
    }

    public boolean isCarWay() {
        return carWay;
    }

    public void setCarWay(boolean carWay) {
        this.carWay = carWay;
    }

    public boolean isBridge() {
        return isBridge;
    }

    public void setBridge(boolean bridge) {
        isBridge = bridge;
    }
}
