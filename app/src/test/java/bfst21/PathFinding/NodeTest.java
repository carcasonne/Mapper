package bfst21.PathFinding;

import bfst21.vector.osm.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {


    @Test
    void distanceSquaredTo() {
        Node node1 = new Node(55.485381f, 12.088585f, true);
        Node m = new Node(55.43379f, 12.95554f, true);
        double expected = 0.754;
        double actual = node1.distanceSquaredTo(m);
        assertEquals(expected, actual, 0.001);
    }

}