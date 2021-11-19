package bfst21.address;

import bfst21.vector.addressparser.Recommender;
import bfst21.vector.radix.RadixTree;
import bfst21.vector.Dijkstra.Coordinate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommenderTest {

    @BeforeEach
    public void setUP(){
    }

    @AfterEach
    public void tearDown(){
    }

    @Test
    public void uft8Test() {
        RadixTree<RadixTree<Coordinate>> list2 = new RadixTree<>();
        Coordinate coordinate = new Coordinate(23242, 232424);
        list2.put("Præstemosen", new RadixTree<>());
        list2.get("Præstemosen").put("22 2650 Hvidovre", coordinate);
        list2.put("Hedegården", new RadixTree<>());
        list2.get("Hedegården").put("11 2650 Hvidovre", coordinate);
        list2.put("Præstevænget", new RadixTree<>());
        list2.get("Præstevænget").put("123 2720 Vanløse", coordinate);
        list2.put("Prostemosen", new RadixTree<>());
        list2.get("Prostemosen").put("22 2650 Hvidovre", coordinate);
        Recommender recommender = new Recommender(list2);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Præstemosen");
        expected.add("Præstevænget");

        assertEquals(expected, recommender.recommendations("Præ"));
    }

    @Test
    public void highLowerCaseTest() {
        RadixTree<RadixTree<Coordinate>> list = new RadixTree<>();
        Coordinate coordinate = new Coordinate(23242, 232424);

        list.put("Prostemosen", new RadixTree<>());
        list.get("Prostemosen").put("1 2650 Hvidovre", coordinate);
        list.put("Prustemosen", new RadixTree<>());
        list.get("Prustemosen").put("3 2650 Hvidovre", coordinate);

        Recommender recommender = new Recommender(list);

        for(String s: recommender.recommendations("P")){
            System.out.println(s);
        }

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Prostemosen");
        expected.add("Prustemosen");

        //Search for highCase
        assertEquals(expected, recommender.recommendations("P"));
        //Search for lowCase
        assertEquals(expected, recommender.recommendations("p"));
    }

    @Test
    public void searchForStreet() {
        RadixTree<RadixTree<Coordinate>> list = new RadixTree<>();
        Coordinate coordinate = new Coordinate(23242, 232424);

        list.put("Prostemosen", new RadixTree<>());
        list.get("Prostemosen").put("1 2650 Hvidovre", coordinate);
        list.put("Prustemosen", new RadixTree<>());
        list.get("Prustemosen").put("3 2650 Hvidovre", coordinate);

        Recommender recommender = new Recommender(list);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Prustemosen");

        assertEquals(expected, recommender.recommendations("Prustemosen"));
    }

    @Test
    public void searchForAddress() {
        RadixTree<RadixTree<Coordinate>> list = new RadixTree<>();
        Coordinate coordinate = new Coordinate(23242, 232424);

        list.put("Prostemosen", new RadixTree<>());
        list.get("Prostemosen").put("1 2650 Hvidovre", coordinate);
        list.put("Prustemosen", new RadixTree<>());
        list.get("Prustemosen").put("3 2650 Hvidovre", coordinate);

        Recommender recommender = new Recommender(list);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Prustemosen 3 2650 Hvidovre");

        assertEquals(expected, recommender.recommendations("Prustemosen 3"));
    }

    @Test
    public void negativeAddressTest() {
        RadixTree<RadixTree<Coordinate>> list = new RadixTree<>();
        Coordinate coordinate = new Coordinate(23242, 232424);

        list.put("Prostemosen", new RadixTree<>());
        list.get("Prostemosen").put("1 2650 Hvidovre", coordinate);
        list.put("Prustemosen", new RadixTree<>());
        list.get("Prustemosen").put("3 2650 Hvidovre", coordinate);

        Recommender recommender = new Recommender(list);

        ArrayList<String> expected = new ArrayList<>();

        assertEquals(expected, recommender.recommendations("No valid address"));
    }


}