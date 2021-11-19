package bfst21.vector.MapElements;

import bfst21.vector.Merger;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MergerTest {
    @BeforeEach
    public void setUP(){
    }

    @AfterEach
    public void tearDown(){
    }


    @Test
    void mergeOuterRelationsTestVaried() {
        ArrayList wayList = new ArrayList();
        Node node1 = new Node(1, 9, 1);
        Node node2 = new Node(2, 9, 5);
        Node node3 = new Node(3, 8, 10);
        Node node4 = new Node(4, 5, 9);
        Node node5 = new Node(5, 3, 7);
        Node node6 = new Node(6, 5, 2);

        Way wayA = new Way();
        Way wayB = new Way();
        Way wayC = new Way();

        wayA.add(node1);
        wayA.add(node2);
        wayA.add(node3);

        wayB.add(node3);
        wayB.add(node4);
        wayB.add(node5);

        wayC.add(node1);
        wayC.add(node6);
        wayC.add(node5);

        wayList.add(wayA);
        wayList.add(wayB);
        wayList.add(wayC);

        Merger merger = new Merger();
        Way finalWay = new Way();
        finalWay = (Way) merger.mergeOuterRelations(wayList).get(0);

        assertEquals(finalWay.first().getX(),finalWay.last().getX());
        assertEquals(finalWay.first().getY(),finalWay.last().getY());
    }

    @Test
    void mergeOuterRelationsTestSameWays() {
        ArrayList wayList = new ArrayList();
        Node node1 = new Node(1, 9, 1);
        Node node2 = new Node(2, 9, 5);
        Node node3 = new Node(3, 8, 10);

        Way wayA = new Way();
        Way wayB = new Way();

        wayA.add(node3);
        wayA.add(node2);
        wayA.add(node1);

        wayB.add(node3);
        wayB.add(node2);
        wayB.add(node1);

        wayList.add(wayA);
        wayList.add(wayB);

        Merger merger = new Merger();
        Way finalWay = new Way();
        finalWay = (Way) merger.mergeOuterRelations(wayList).get(0);

        assertEquals(finalWay.first().getY(),finalWay.last().getY());
        assertEquals(finalWay.first().getX(),finalWay.last().getX());
    }

    @Test
    void mergeCoastLinesTest() {
        Merger merger = new Merger();
        ArrayList<Way> wayList = new ArrayList<>();

        Node node1 = new Node(1, 10, 8);
        Node node2 = new Node(2, 15, 11);
        Node node3 = new Node(3, 18, 6);
        Node node4 = new Node(4, 12, 3);
        Node node5 = new Node(5, 17, 1);

        Way wayA = new Way();
        Way wayB = new Way();

        wayA.add(node1);
        wayA.add(node2);
        wayA.add(node3);

        wayB.add(node4);
        wayB.add(node5);

        wayList.add(wayA);
        wayList.add(wayB);

        int counter = 0;
        for (Drawable d : merger.mergeCoastLines(wayList)) {
            if (d instanceof Way) {
                counter += ((Way) d).getNodes().size();
            }

        }
        assertEquals(5, counter);
    }
}
