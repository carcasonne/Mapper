package bfst21.PathFinding;

import bfst21.vector.Dijkstra.DirectedEdge;
import bfst21.vector.Dijkstra.GraphNode;
import bfst21.vector.osm.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RoadDirectionGuideTest {

    @BeforeEach
    public void setUP(){
    }

    @AfterEach
    public void tearDown(){
    }


    @Test
    void getDeterminantTest() {

        Node n = new Node(1,55.485381f, 12.088585f);
        Node m = new Node(2, 55.43379f, 12.95554f);
        Node n2 = new Node(3,55.43379f, 12.95554f);
        Node m2 = new Node(4,55.56000f, 12.96554f);
        GraphNode nv = new GraphNode(n, 0); //maxspeed is irrelevant for this test
        GraphNode mv = new GraphNode(m, 0);
        GraphNode nv2 = new GraphNode(n2, 0);
        GraphNode mv2 = new GraphNode(m2, 0);
        DirectedEdge a = new DirectedEdge(nv, mv);
        DirectedEdge b = new DirectedEdge(nv2, mv2);
        a.setName("Testname");
        b.setName("Testname");
        List<DirectedEdge> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        RoadDirectionGuideTESTER roadGuide = new RoadDirectionGuideTESTER(list);

        boolean leftTurn = false;

        if (roadGuide.getDeterminant(a,b) > 0){
            leftTurn = true;
        }

        assertTrue(leftTurn);
    }

    @Test
    void getAngleTest() {
        Node n = new Node(1,55.485381f, 12.088585f);
        Node m = new Node(2, 55.43379f, 12.95554f);
        Node n2 = new Node(3,55.43379f, 12.95554f);
        Node m2 = new Node(4,55.56000f, 12.96554f);
        GraphNode nv = new GraphNode(n, 0); //maxspeed is irrelevant for this test
        GraphNode mv = new GraphNode(m, 0);
        GraphNode nv2 = new GraphNode(n2, 0);
        GraphNode mv2 = new GraphNode(m2, 0);
        DirectedEdge a = new DirectedEdge(nv, mv);
        DirectedEdge b = new DirectedEdge(nv2, mv2);
        a.setName("Testname");
        b.setName("Testname");
        List<DirectedEdge> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        RoadDirectionGuideTESTER roadGuide = new RoadDirectionGuideTESTER(list);

        assertEquals( 86.47,roadGuide.getAngle(a,b),0.01);
    }
}

// Manual import of RoadDirectionGuide.java. The class gave us trouble because of the importing of the AlertBox (Java FX Element).



class RoadDirectionGuideTESTER {
    List<DirectedEdge> inputList;
    public ArrayList<String> outputList = new ArrayList<>();
    public ArrayList<Double> roadLengthList = new ArrayList<>();
    public ArrayList<String> determinantList = new ArrayList<>();
    public ArrayList<Double> angleList = new ArrayList<>();

    public RoadDirectionGuideTESTER(List<DirectedEdge> inputList){
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
    }

    public double getDeterminant(DirectedEdge first, DirectedEdge last) {
        double relativeLastLon = last.to().getLongitude() - last.from().getLongitude();
        double relativeLastLat = last.to().getLatitude() - last.from().getLatitude();

        double relativeFirstLon = first.from().getLongitude() - first.to().getLongitude();
        double relativeFirstLat = first.from().getLatitude()- first.to().getLatitude();

        double determinant = (relativeFirstLon * relativeLastLat) - (relativeFirstLat * relativeLastLon);
        return determinant;
    }

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