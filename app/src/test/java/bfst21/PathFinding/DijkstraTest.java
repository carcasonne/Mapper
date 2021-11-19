package bfst21.PathFinding;

import bfst21.vector.Dijkstra.Dijkstra;
import bfst21.vector.Dijkstra.DirectedEdge;
import bfst21.vector.Dijkstra.Graph;
import bfst21.vector.Dijkstra.GraphNode;
import bfst21.vector.osm.Node;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@TestInstance(Lifecycle.PER_CLASS)

/**
 *  Using the naming convention of Roy Osherove for methods
 *  https://osherove.com/blog/2005/4/3/naming-standards-for-unit-tests.html
 *  [UnitOfWork_StateUnderTest_ExpectedBehavior]
 */


public class DijkstraTest {
    Graph graph;

    GraphNode vertex1;
    GraphNode vertex2;
    GraphNode vertex3;
    GraphNode vertex4;
    GraphNode vertex5;
    GraphNode vertex6;

    @BeforeEach
    public void setUp() {
        graph = new Graph();

        Node node1 = new Node(-5, 2/-0.56f, true);
        Node node2 = new Node(-1, 1/-0.56f, true);
        Node node3 = new Node(-1, 5/-0.56f, true);
        Node node4 = new Node(5, 2/-0.56f, true);
        Node node5 = new Node(-5, -5/-0.56f, true);
        Node node6 = new Node(-2, -4/-0.56f, true);

        vertex1 = new GraphNode(node1, 130);
        vertex2 = new GraphNode(node2, 130);
        vertex3 = new GraphNode(node3, 130);
        vertex4 = new GraphNode(node4, 130);
        vertex5 = new GraphNode(node5, 130);
        vertex6 = new GraphNode(node6, 130);

        //ID's, order of insertion:
        //vertex 1: 0
        //vertex 2: 1
        //vertex 3: 3
        //vertex 4: 5
        //vertex 5: 4
        //vertex 6: 2

        graph.createPath(vertex1, vertex2, "roadName", true, false, true); //length: 4.12
        graph.createPath(vertex1, vertex6, "roadName", false, false, true); //length: 6.71

        graph.createPath(vertex2, vertex3, "roadName", true, false, true); //length: 4
        graph.createPath(vertex2, vertex5, "roadName", true, false, true); //length: 8.49

        graph.createPath(vertex3, vertex4, "roadName", false, false, true); //length: 6.71

        graph.createPath(vertex5, vertex4, "roadName", true, false, true); //length: 7
        graph.createPath(vertex5, vertex6, "roadName", true, false, true); //length: 7.07
    }

    @AfterEach
    public void tearDown() {
        graph = null;
    }

    @Test
    public void graph_construction_correctNoOfVerteces() {
        int actual = graph.V();
        int expected = 6;

        assertEquals(expected, actual);
    }

    @Test
    public void shortestPath_vertex2ToItself_equals0() throws Exception {
        double expected = 0;
        double actual   = 0;

        GraphNode start = graph.getAllVertices().get(vertex2.getID());
        GraphNode end   = graph.getAllVertices().get(vertex2.getID());

        Dijkstra.TransportType transportType = Dijkstra.TransportType.WALK;

        Dijkstra shortestPath = new Dijkstra(graph, start, end, transportType);

        ArrayList<DirectedEdge> list = shortestPath.pathTo(end);

        if(list == null) assertTrue(false);
        for(DirectedEdge e : list)
            actual += e.weight();

        assertEquals(expected, actual);
    }

    @Test
    public void shortestPath_vertex1ToVertex3_pathFound() throws Exception {
        double expected       = 458.4 + 444.7; //1 -> 2 -> 3
        double expected_lower = expected * 0.97;
        double expected_upper = expected * 1.03;
        double actual         = 0;

        GraphNode start = graph.getAllVertices().get(vertex1.getID());
        GraphNode end   = graph.getAllVertices().get(vertex3.getID());

        Dijkstra.TransportType transportType = Dijkstra.TransportType.WALK;

        Dijkstra shortestPath = new Dijkstra(graph, start, end, transportType);

        ArrayList<DirectedEdge> list = shortestPath.pathTo(end);

        if(list == null) assertTrue(false);
        for(DirectedEdge e : list)
            actual += e.weight();

        if(expected_upper > actual && actual > expected_lower) assertTrue(true);
        else                                                   assertTrue(false);
    }

    @Test
    public void shortestPath_vertex1ToVertex3_equalsPathBetweenVertex3ToVertex1() throws Exception {
        double length_1to6   = 0;
        double length_6to1   = 0;


        GraphNode vertex_1   = graph.getAllVertices().get(vertex1.getID());
        GraphNode vertex_6   = graph.getAllVertices().get(vertex6.getID());

        Dijkstra.TransportType transportType = Dijkstra.TransportType.WALK;

        Dijkstra shortestPath_1to6 = new Dijkstra(graph, vertex_1, vertex_6, transportType);
        Dijkstra shortestPath_6to1 = new Dijkstra(graph, vertex_6, vertex_1, transportType);

        ArrayList<DirectedEdge> edges_1to6 = shortestPath_1to6.pathTo(vertex_6);
        ArrayList<DirectedEdge> edges_6to1 = shortestPath_6to1.pathTo(vertex_1);

        if(edges_1to6 == null || edges_6to1 == null) assertTrue(false);
        //same list size
        if(edges_1to6.size() != edges_6to1.size())   assertTrue(false);

        for(DirectedEdge e : edges_1to6)
            length_1to6 += e.weight();

        for(DirectedEdge e : edges_6to1)
            length_6to1 += e.weight();

        assertEquals(length_1to6, length_6to1);
    }

    @Test
    public void shortestPath_transportIsCar_noPathExists() {
        try {
            //same route as shortestPath_vertex1ToVertex3_pathFound()
            GraphNode start = graph.getAllVertices().get(vertex1.getID());
            GraphNode end   = graph.getAllVertices().get(vertex3.getID());

            //GO BY CAR
            Dijkstra.TransportType transportType = Dijkstra.TransportType.CAR;

            Dijkstra shortestPath = new Dijkstra(graph, start, end, transportType);

            ArrayList<DirectedEdge> list = shortestPath.pathTo(end);
        } catch (Exception e) {
            if(e.getMessage().equals("No route exists")) assertTrue(true);
        }
    }

    @Test
    public void shortestPath_transportIsAll_pathFound() {
        try {
            //same route as shortestPath_vertex1ToVertex3_pathFound()
            GraphNode start = graph.getAllVertices().get(vertex1.getID());
            GraphNode end   = graph.getAllVertices().get(vertex3.getID());

            //GO BY ALL TRANSPORT
            Dijkstra.TransportType transportType = Dijkstra.TransportType.ALL;

            Dijkstra shortestPath = new Dijkstra(graph, start, end, transportType);

            //exception would be thrown here
            ArrayList<DirectedEdge> list = shortestPath.pathTo(end);

            assertTrue(true);

        } catch (Exception e) {
            assertTrue(false);
        }

    }



    @Test
    public void shortestPath_vertex2ToIllegalVertexPositive_throwsException() {
        try {
            int illegalVertexID = Integer.MAX_VALUE;
            GraphNode start = graph.getAllVertices().get(vertex1.getID());
            GraphNode end   = graph.getAllVertices().get(illegalVertexID);
            Dijkstra.TransportType transportType = Dijkstra.TransportType.WALK;

            Dijkstra shortestPath = new Dijkstra(graph, start, end, transportType);

            assertTrue(false);

        } catch (Exception e) {
            if(e.getMessage().equals("Vertex has ID greater than no. of verteces")) assertTrue(true);
        }
    }

    @Test
    public void shortestPath_vertex2ToIllegalVertexNegative_throwsException() {
        try {
            int illegalVertexID = -1;
            GraphNode start = graph.getAllVertices().get(vertex1.getID());
            GraphNode end   = graph.getAllVertices().get(illegalVertexID);
            Dijkstra.TransportType transportType = Dijkstra.TransportType.WALK;

            Dijkstra shortestPath = new Dijkstra(graph, start, end, transportType);

            assertTrue(false);

        } catch (Exception e) {
            if(e.getMessage().equals("Vertex has negative ID")) assertTrue(true);
        }
    }
}