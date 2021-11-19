package bfst21.KDTree;
import bfst21.vector.KDTree.KDRectangle;
import bfst21.vector.MapElements.Road;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;
import bfst21.vector.Dijkstra.Dijkstra;
import bfst21.vector.Dijkstra.DirectedEdge;
import bfst21.vector.Dijkstra.Graph;
import bfst21.vector.Dijkstra.GraphNode;
import bfst21.vector.KDTree.KDRectangle;
import bfst21.vector.osm.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(Lifecycle.PER_CLASS)

public class KDRectangleTest {
    private KDRectangle centerRectangle;

    Road upperLeftMapFeature;
    Road leftCenterMapFeature;
    Road lowerLeftMapFeature;
    Road upperRightMapFeature;
    Road rightCenterMapFeature;
    Road lowerRightMapFeature;
    Road lowerCenterMapFeature;
    Road upperCenterMapFeature;
    Road centerMapFeature;


    @BeforeEach
    public void setUp() {
        centerRectangle    = new KDRectangle(-1, -1, 1, 0.5f);

        Way upperLeftWay   = new Way();
        Way leftCenterWay  = new Way();
        Way lowerLeftWay   = new Way();
        Way upperRightWay  = new Way();
        Way rightCenterWay = new Way();
        Way lowerRightWay  = new Way();
        Way lowerCenterWay = new Way();
        Way upperCenterWay = new Way();
        Way centerWay      = new Way();

        Node upperLeft     = new Node(-1, 1, true);
        Node leftCenter    = new Node(-1, 0, true);
        Node lowerLeft     = new Node(-1, -1, true);
        Node upperRight    = new Node(1, 1, true);
        Node rightCenter   = new Node(1, 0, true);
        Node lowerRight    = new Node(1, -1, true);
        Node lowerCenter   = new Node(0, -1, true);
        Node upperCenter   = new Node(0, 1, true);
        Node center        = new Node(0, 0, true);

        upperLeftMapFeature   = new Road(upperLeftWay,   "type not important");
        leftCenterMapFeature  = new Road(leftCenterWay,  "type not important");
        lowerLeftMapFeature   = new Road(lowerLeftWay,   "type not important");
        upperRightMapFeature  = new Road(upperRightWay,  "type not important");
        rightCenterMapFeature = new Road(rightCenterWay, "type not important");
        lowerRightMapFeature  = new Road(lowerRightWay,  "type not important");
        lowerCenterMapFeature = new Road(lowerCenterWay, "type not important");
        upperCenterMapFeature = new Road(upperCenterWay, "type not important");
        centerMapFeature      = new Road(centerWay,      "type not important");

        //name is necesarry for the getNearestNeighbor method to inspect the MapFeature
        upperLeftMapFeature.setRoadName("name");
        leftCenterMapFeature.setRoadName("name");
        lowerLeftMapFeature.setRoadName("name");
        upperRightMapFeature.setRoadName("name");
        rightCenterMapFeature.setRoadName("name");
        lowerRightMapFeature.setRoadName("name");
        lowerCenterMapFeature.setRoadName("name");
        upperCenterMapFeature.setRoadName("name");
        centerMapFeature.setRoadName("name");

        upperLeftWay.add(upperLeft);
        leftCenterWay.add(leftCenter);
        lowerLeftWay.add(lowerLeft);
        upperRightWay.add(upperRight);
        rightCenterWay.add(rightCenter);
        lowerRightWay.add(lowerRight);
        lowerCenterWay.add(lowerCenter);
        upperCenterWay.add(upperCenter);
        centerWay.add(center);
    }

    @AfterEach
    public void tearDown() {
        centerRectangle       = null;

        upperLeftMapFeature   = null;
        leftCenterMapFeature  = null;
        lowerLeftMapFeature   = null;
        upperRightMapFeature  = null;
        rightCenterMapFeature = null;
        lowerRightMapFeature  = null;
        lowerCenterMapFeature = null;
        upperCenterMapFeature = null;
        centerMapFeature      = null;
    }


    @Test
    public void contains_centerPoint_returnTrue() {
        boolean containsMapFeature = centerRectangle.containsMapFeature(centerMapFeature);

        assertTrue(containsMapFeature);
    }

    @Test
    public void contains_upperLeft_returnFalse() {
        boolean containsMapFeature = centerRectangle.containsMapFeature(upperLeftMapFeature);

        assertFalse(containsMapFeature);
    }

    @Test
    public void intersects_itself_returnTrue() {
        boolean intersectsSelf = centerRectangle.intersects(centerRectangle);

        assertTrue(intersectsSelf);
    }

    @Test
    public void intersects_illegalRectangle_returnFalse() {
        KDRectangle illegalRectangle = new KDRectangle(100, 100, -100, -100);

        boolean intersectsIllegal = centerRectangle.intersects(illegalRectangle);

        assertFalse(intersectsIllegal);
    }

    @Test
    public void intersects_borderRectangle_returnTrue() {
        KDRectangle borderRectangle = new KDRectangle(-1, 0.4f, 1, 1.5f);

        boolean intersectsBorder = borderRectangle.intersects(centerRectangle);

        assertTrue(intersectsBorder);
    }

    @Test
    public void fullyContains_insideRectangle_returnTrue() {
        KDRectangle insideRectangle = new KDRectangle(0, 0, 0.1f, 0.1f);

        boolean intersectsBorder = centerRectangle.fullyContains(insideRectangle);

        assertTrue(intersectsBorder);
    }

    @Test
    public void fullyContains_borderRectangle_returnTrue() {
        KDRectangle borderRectangle = new KDRectangle(-1, 0.4f, 1, 1.5f);

        boolean intersectsBorder = borderRectangle.fullyContains(centerRectangle);

        assertFalse(intersectsBorder);
    }

    @Test
    public void intersectsRangeX_xLine_returnTrue() {
        KDRectangle xLine = new KDRectangle(-100, -25283, 0, -25283);

        boolean intersectsLine = centerRectangle.intersectsX(xLine);

        assertTrue(intersectsLine);
    }

    @Test
    public void intersectsRangeX_xLineBorder_returnTrue() {
        KDRectangle xLineBorder = new KDRectangle(-100, -25283, -1, -25283);

        boolean intersectsLine = centerRectangle.intersectsX(xLineBorder);

        assertTrue(intersectsLine);
    }

    @Test
    public void intersectsRangeX_xLineBorder_returnFalse() {
        KDRectangle xLine = new KDRectangle(-100, -25283, -1.01f, -25283);

        boolean intersectsLine = centerRectangle.intersectsX(xLine);

        assertFalse(intersectsLine);
    }
}