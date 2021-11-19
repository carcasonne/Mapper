package bfst21.vector.Dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
    private List<GraphNode> vertices;
    private List<DirectedEdge> edges;
    private HashMap<String, Integer> positionToNodeIDMap;

    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        positionToNodeIDMap = new HashMap<>();
    }

    public List<GraphNode> getAllVertices() {
        return vertices;
    }

    public int V() {
        return vertices.size();
    }

    public HashMap<String, Integer> getIDToNodeMap() {
        return positionToNodeIDMap;
    }

    public Iterable<DirectedEdge> adjacent(GraphNode v) {
        return v.getEdges();
    }

    //Create path between node v and w. Checking if way is oneway, carway and footway/bikeway.
    public void createPath(GraphNode v, GraphNode w, String roadName, boolean isOneWay, boolean isCarWay, boolean isFootWay) {
        if(v == null || w == null) return;

        String from = new Coordinate(v.getLongitude(), v.getLatitude()).toString();
        String to = new Coordinate(w.getLongitude(), w.getLatitude()).toString();
        
        GraphNode fromGraphNode = convertPositionToNode(from);
        GraphNode toGraphNode = convertPositionToNode(to);

        //if vertex doesn't already exist
        if(fromGraphNode == null) {
            v.setID(V());
            positionToNodeIDMap.put(from, v.getID());
            vertices.add(v);
            fromGraphNode = v;
        }

        //if vertex doesn't already exist
        if(toGraphNode == null) {
            w.setID(V());
            positionToNodeIDMap.put(to, w.getID());
            vertices.add(w);
            toGraphNode = w;
        }

        addEdge(fromGraphNode, toGraphNode, isOneWay, isCarWay, isFootWay, roadName);
    }

    //Tries to get node. If it does not exist: return null
    public GraphNode convertPositionToNode(String coord_string) {
        try {
            return vertices.get(positionToNodeIDMap.get(coord_string));
        }
        catch(NullPointerException e) {
            return null;
        }
    }

    public void addEdge(GraphNode v, GraphNode w, boolean isOneWay, boolean isCarWay, boolean isFootWay, String roadname) {
        DirectedEdge edgeVtoW = new DirectedEdge(v, w);
        DirectedEdge edgeWtoV = new DirectedEdge(w, v);

        edgeVtoW.setMaxSpeed(v.getMaxSpeed());
        edgeWtoV.setMaxSpeed(v.getMaxSpeed());
        edgeVtoW.setName(roadname);
        edgeWtoV.setName(roadname);

        if(isCarWay){
            edgeVtoW.setIsCarway();
            edgeWtoV.setIsCarway();
        }

        if(isFootWay){
            edgeVtoW.setIsBikeway();
            edgeWtoV.setIsBikeway();
            edgeVtoW.setIsFootway();
            edgeWtoV.setIsFootway();
        }

        if(!v.getEdges().contains(edgeVtoW)) {
            v.getEdges().add(edgeVtoW);
        }

        if(!isOneWay) {
            if(!w.getEdges().contains(edgeWtoV)) {
                w.getEdges().add(edgeWtoV);
            }
        }

        edges.add(edgeVtoW);
        //edges.add(edgeWtoV);
    }
}
