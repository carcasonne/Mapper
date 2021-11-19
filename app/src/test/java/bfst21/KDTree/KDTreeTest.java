package bfst21.KDTree;

import bfst21.vector.KDTree.KDRectangle;
import bfst21.vector.KDTree.KDTree;
import bfst21.vector.MapElements.Building;
import bfst21.vector.MapElements.MapFeature;
import bfst21.vector.MapElements.Road;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class KDTreeTest {
    KDTree kdtree;
    ArrayList<MapFeature> mapFeatures;
    ArrayList<MapFeature> expectedFeatures;
    ArrayList<MapFeature> actualFeatures;

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
        mapFeatures = new ArrayList<>();
        expectedFeatures = new ArrayList<>();
        actualFeatures = new ArrayList<>();

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
        mapFeatures      = null;
        expectedFeatures = null;
        actualFeatures   = null;

        kdtree = null;

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
    public void initialization_addAllElements_positive() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 1);

        int expected = 9;
        int actual = kdtree.size();

        assertEquals(expected, actual);
    }

    @Test
    public void initialization_leafSize1_has9Leaves() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 1);

        int expected = 9;
        int actual = kdtree.leaves();

        assertEquals(expected, actual);
    }

    //This should return 4, as it is a full binary tree. Parents with only 1 child should not be possible
    @Test
    public void initialization_leafSize3_has4Leaves() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3); //3 elements per leaf

        int expected = 4;
        int actual = kdtree.leaves();

        assertEquals(expected, actual);
    }

    @Test
    public void rangeSearch_infinteRange_containsAllPoints() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) Double.NEGATIVE_INFINITY,
                (float) Double.NEGATIVE_INFINITY,
                (float) Double.POSITIVE_INFINITY,
                (float) Double.POSITIVE_INFINITY);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    @Test
    public void rangeSearch_illegalRange_containsNoPoints() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) Double.POSITIVE_INFINITY,
                (float) Double.POSITIVE_INFINITY,
                (float) Double.NEGATIVE_INFINITY,
                (float) Double.NEGATIVE_INFINITY);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures = mapFeatures;

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    @Test
    public void rangeSearch_Xrange_containsCenterPoints() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) -0.5,
                (float) Double.NEGATIVE_INFINITY,
                (float) 0.5,
                (float) Double.POSITIVE_INFINITY);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures.add(lowerCenterMapFeature); //(0,-1)
        expectedFeatures.add(upperCenterMapFeature); //(0,1)
        expectedFeatures.add(centerMapFeature);      //(0,0)

        for(int i = 0; i < expectedFeatures.size(); i++) {
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
        }

    }

    @Test
    public void rangeSearch_Yrange_containsCenterPoints() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) Double.NEGATIVE_INFINITY,
                (float) -0.5,
                (float) Double.POSITIVE_INFINITY,
                (float) 0.5);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures.add(leftCenterMapFeature); //(-1,0)
        expectedFeatures.add(rightCenterMapFeature); //(1,0)
        expectedFeatures.add(centerMapFeature); //(0,0)

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    @Test
    public void rangeSearch_firstQuadrant_findPositiveXPositiveY() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) 0,
                (float) 0,
                (float) Double.POSITIVE_INFINITY,
                (float) Double.POSITIVE_INFINITY);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures.add(upperRightMapFeature); //(1,1)
        expectedFeatures.add(rightCenterMapFeature); //(1,0)
        expectedFeatures.add(upperCenterMapFeature); //(0,1)
        expectedFeatures.add(centerMapFeature); //(0,0)

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    @Test
    public void rangeSearch_secondQuadrant_findNegativeXPositiveY() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) Double.NEGATIVE_INFINITY,
                (float) 0,
                (float) 0,
                (float) Double.POSITIVE_INFINITY);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures.add(upperLeftMapFeature); //(-1,1)
        expectedFeatures.add(leftCenterMapFeature); //(-1,0)
        expectedFeatures.add(upperCenterMapFeature); //(0,1)
        expectedFeatures.add(centerMapFeature); //(0,0)

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    @Test
    public void rangeSearch_thirdQuadrant_findNegativeXNegativeY() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) Double.NEGATIVE_INFINITY,
                (float) Double.NEGATIVE_INFINITY,
                (float) 0,
                (float) 0);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures.add(lowerLeftMapFeature); //(-1,-1)
        expectedFeatures.add(leftCenterMapFeature); //(-1,0)
        expectedFeatures.add(lowerCenterMapFeature); //(0,-1)
        expectedFeatures.add(centerMapFeature); //(0,0)

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    @Test
    public void rangeSearch_fourthQuadrant_findPositiveXNegativeY() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);
        KDRectangle range = new KDRectangle((float) 0,
                (float) Double.NEGATIVE_INFINITY,
                (float) Double.POSITIVE_INFINITY,
                (float) 0);

        kdtree = new KDTree(mapFeatures, 1);

        actualFeatures = kdtree.query(range);
        expectedFeatures.add(lowerRightMapFeature); //(1,-1)
        expectedFeatures.add(rightCenterMapFeature); //(1,0)
        expectedFeatures.add(lowerCenterMapFeature); //(0,-1)
        expectedFeatures.add(centerMapFeature); //(0,0)

        for(int i = 0; i < expectedFeatures.size(); i++)
            if(!actualFeatures.contains(expectedFeatures.get(i)))
                assertTrue(false);
    }

    //Test doesn't work.
    //Can't find any feature to input position (0,0)
    @Test
    public void nearestNeighbor_center_findCenter() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = centerMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(0, 0, true);
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_closeToCenter_findCenter() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = centerMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(0.00000001, 0.00000001, true);
            System.out.println(actual.getAvgCoord());
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_upperRight_findUpperRight() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = upperRightMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(1000, 1000, true);
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_lowerLeft_findLowerLeft() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = lowerLeftMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(-1000, -1000, true);
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_closerToLeftThanCenter_findCenterLeft() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = leftCenterMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(-0.51, 0.00000001, true);
            System.out.println(actual.getAvgCoord());
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_closerToCenterThanLeft_findCenter() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = centerMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(-0.49, 0.00001, true);
            System.out.println(actual.getAvgCoord());
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_closerToUpperThanCenter_findUpperCenter() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = upperCenterMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(0.00001, 0.5, true);
            System.out.println(actual.getAvgCoord());
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_closerToCenterThanUpper_findCenter() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        MapFeature expected = upperCenterMapFeature;
        MapFeature actual;
        try {
            actual = kdtree.getNearestMapFeature(0.00001, 0.5, true);
            System.out.println(actual.getAvgCoord());
            assertEquals(expected, actual);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void nearestNeighbor_illegalCoordinate_throwsException() {
        mapFeatures.add(upperLeftMapFeature);
        mapFeatures.add(leftCenterMapFeature);
        mapFeatures.add(lowerLeftMapFeature);
        mapFeatures.add(upperRightMapFeature);
        mapFeatures.add(rightCenterMapFeature);
        mapFeatures.add(lowerRightMapFeature);
        mapFeatures.add(lowerCenterMapFeature);
        mapFeatures.add(upperCenterMapFeature);
        mapFeatures.add(centerMapFeature);

        kdtree = new KDTree(mapFeatures, 3);

        try {
            MapFeature actual = kdtree.getNearestMapFeature(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, true);
        } catch (Exception e) {
            if(e.getMessage().equals("Illegal coordinate")) assertTrue(true);
            else assertTrue(false);
        }

    }
}