package bfst21.PathFinding;

import bfst21.vector.Dijkstra.GraphNode;
import bfst21.vector.osm.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphNodeTest {
    
    @Test
    public void haversineDistance_positive() {
        Node n = new Node(55.485381f, 12.088585f, true);
        Node m = new Node(55.43379f, 12.95554f, true);

        GraphNode nv = new GraphNode(n, 0); //maxspeed is irrelevant for this test
        GraphNode mv = new GraphNode(m, 0);

        double expected = 54.960; //based on the calculations by https://www.movable-type.co.uk/scripts/latlong.html
        //3% error bound
        double lower_bound = expected * 0.97;
        double upper_bound = expected * 1.03;
        double actual = GraphNode.haversineDistanceBetween(nv, mv);

        if(actual > lower_bound && actual < upper_bound) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void haversineDistanceIs0() { 
        Node n = new Node(0, 0, true);
        Node m = new Node(0, 0, true);

        GraphNode nv = new GraphNode(n, 0); //maxspeed is irrelevant for this test
        GraphNode mv = new GraphNode(m, 0);

        double expected = 0;
        double actual = GraphNode.haversineDistanceBetween(nv, mv);

        assertEquals(expected, actual);
    }
    @Test
    public void getMaxspeed80(){
        Node b = new Node(55.485381f, 12.088585f, true);
        GraphNode a = new GraphNode(b, 80);
        assertEquals(80,a.getMaxSpeed());
    }
    @Test
    public void euclidianDistanceBetweenTest(){
        Node n = new Node(55.485381f, 12.088585f, true);
        Node m = new Node(55.43379f, 12.95554f, true);
        GraphNode a = new GraphNode(n, 0);
        GraphNode b = new GraphNode(m, 0);
        double expected = 0.868;
        double actual = a.euclidianDistanceBetween(a,b);
        assertEquals(expected, actual, 0.001);
    }



}
