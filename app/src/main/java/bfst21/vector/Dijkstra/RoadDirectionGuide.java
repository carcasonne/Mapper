package bfst21.vector.Dijkstra;

import bfst21.vector.Exceptions.AlertBox;

import java.util.ArrayList;
import java.util.List;

public class RoadDirectionGuide {
    List<DirectedEdge> inputList;
    public ArrayList<String> outputList = new ArrayList<>();
    public ArrayList<Double> roadLengthList = new ArrayList<>();
    public ArrayList<String> determinantList = new ArrayList<>();
    public ArrayList<Double> angleList = new ArrayList<>();

    //Creating the list of direction for the user
    public RoadDirectionGuide(List<DirectedEdge> inputList){
        try {
            this.inputList = inputList;
            ArrayList<String> inputSet = new ArrayList<>();
            int i = inputList.size() - 1;

            int currentRoad = 0;
            roadLengthList.add(0.0);
            inputSet.add(inputList.get(i).getRoadName());
            while (i >= 0) {
                if (i < inputList.size() - 1) {
                    if (inputList.get(i).getRoadName() == null) {
                        inputList.get(i).setName(inputList.get(i + 1).getRoadName());
                    }
                    if (!inputList.get(i).getRoadName().equals(inputList.get(i + 1).getRoadName())) {
                        inputSet.add(inputList.get(i).getRoadName());
                        double angle = getAngle(inputList.get(i), inputList.get(i + 1));
                        angleList.add(angle);
                        double determinant = getDeterminant(inputList.get(i), inputList.get(i + 1));
                        determinantList.add(String.format("%.1f", determinant));
                        currentRoad++;
                        roadLengthList.add(0.0);
                    }
                }
                double currentSpeed = roadLengthList.remove(currentRoad);
                currentSpeed += inputList.get(i).weight();
                roadLengthList.add(currentSpeed);
                i--;
            }

            i = 0;
            boolean turned = false;
            boolean started = true;
            while (i < inputSet.size()) {
                String output = "";
                if (turned) {
                    output = "and continue ";
                    turned = false;
                }
                if (started) {
                    output = "Start on " + inputSet.get(0) + " and drive ";
                    started = false;
                }

                String direction = "";
                if (roadLengthList.get(i) >= 1) {
                    output = output + "for " + String.format("%.1f", roadLengthList.get(i)) + " km";
                } else if (roadLengthList.get(i) < 1) {
                    output = output + "for " + String.format("%.0f", roadLengthList.get(i) * 1000) + " meters";
                }
                outputList.add(output);

                if (i != inputSet.size() - 1) {
                    if (angleList.get(i) < 165) {
                        if (determinantList.get(i).contains("-")) {
                            direction = "Then turn left onto " + inputSet.get(i + 1);
                            turned = true;
                        } else if (!determinantList.get(i).contains("-")) {
                            direction = "Then turn right onto " + inputSet.get(i + 1);
                            turned = true;
                        }
                    } else {
                        direction = "Then continue onto " + inputSet.get(i + 1);
                    }
                } else {
                    direction = "Then you have arrived.";
                }
                outputList.add(direction);
                i++;

            }
        }catch (NullPointerException e){
            AlertBox a = new AlertBox();
            a.pathAlert();
        }catch (IndexOutOfBoundsException e){
            AlertBox a = new AlertBox();
            a.sameAddressAlert();
        }
    }

    public double getDeterminant(DirectedEdge first, DirectedEdge last) {
        double relativeLastLon = last.to().getLongitude() - last.from().getLongitude();
        double relativeLastLat = last.to().getLatitude() - last.from().getLatitude();

        double relativeFirstLon = first.from().getLongitude() - first.to().getLongitude();
        double relativeFirstLat = first.from().getLatitude()- first.to().getLatitude();

        double determinant = (relativeFirstLon * relativeLastLat) - (relativeFirstLat * relativeLastLon);
        return determinant;
    }

    //Calculating the angle of the turn
    public double getAngle(DirectedEdge first, DirectedEdge last){
        double relativeLastLon = last.to().getLongitude() - last.from().getLongitude();
        double relativeLastLat = last.to().getLatitude() - last.from().getLatitude();

        double relativeFirstLon = first.from().getLongitude() - first.to().getLongitude();
        double relativeFirstLat = first.from().getLatitude()- first.to().getLatitude();

        double scalarProduct = (relativeFirstLon * relativeLastLon + relativeFirstLat * relativeLastLat)/
                (Math.sqrt(relativeFirstLon * relativeFirstLon + relativeFirstLat * relativeFirstLat) * Math.sqrt(relativeLastLon * relativeLastLon + relativeLastLat * relativeLastLat));
        double acos = Math.acos(scalarProduct);

        return Math.toDegrees(acos);
    }

}



