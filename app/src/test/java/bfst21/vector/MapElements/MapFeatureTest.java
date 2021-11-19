package bfst21.vector.MapElements;

import bfst21.vector.Dijkstra.Coordinate;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapFeatureTest {

    Way way = new Way();
    String type = "Type";
    @Test
    public void createAvgCoordTest(){
        way.add(new Node(55.485381f, 12.088585f, true));
        way.add(new Node(55.472381f, 12.018585f, true));
        way.add(new Node(55.489781f, 12.087285f, true));
        way.add(new Node(55.466781f, 12.047285f, true));
        MapFeature map = new MapFeature(way,type);
        map.createAvgCoord();
        var avg = map.getAvgCoord();
        Node expected = new Node((55.485381f + 55.472381f + 55.489781f + 55.466781f) / 4, (12.088585f + 12.018585f + 12.087285f + 12.047285f) / 4, true);
        Node actual = avg;

        assertEquals(expected.getX(), actual.getX());
        assertEquals(expected.getY(), actual.getY());
    }

    @Test
    public void closeNearestNodeToPointTest(){
        way.add(new Node(55.485381f, 12.088585f, true));
        way.add(new Node(55.472381f, 12.018585f, true));
        way.add(new Node(55.489781f, 12.087285f, true));
        way.add(new Node(55.466781f, 12.047285f, true));
        MapFeature map = new MapFeature(way,type);

        Node expected = new Node(55.472381f, 12.018585f, true);
        Node actual = map.nearestNodeToPoint(new Coordinate(55.472382f, 12.018586f));

        assertEquals(expected.getX(), actual.getX());
        assertEquals(expected.getY(), actual.getY());
    }

    @Test
    public void farNearestNodeToPointTest(){
        way.add(new Node(55.585381f, 12.088585f, true));
        way.add(new Node(55.572381f, 12.068585f, true));
        way.add(new Node(55.589781f, 12.087285f, true));
        way.add(new Node(55.466781f, 12.047285f, true));
        MapFeature map = new MapFeature(way,type);

        Node expected = new Node(55.466781f, 12.047285f, true);
        Node actual = map.nearestNodeToPoint(new Coordinate(0f, 0f));

        assertEquals(expected.getX(), actual.getX());
        assertEquals(expected.getY(), actual.getY());
    }


}