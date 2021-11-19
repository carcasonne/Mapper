package bfst21.vector.Dijkstra;

import java.util.ArrayList;

/**
 *  Sedgewick's and Wayne's Dijksta implementation.
 *  Modified to follow the principles of A-Star pathfinding
 *  Modified to only allow specific paths in specific circumstances (eg. walking/biking/driving)
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Dijkstra {
    private double[] g_cost; // s -> v distance of the shortest path
    private DirectedEdge[] edgeTo;
    private IndexMinPq<Double> pq;
    private GraphNode destination;
    private TransportType transportType;

    //supported types of transportation
    public enum TransportType {
        ALL,
        WALK,
        BIKE,
        CAR
    }

    public Dijkstra(Graph graph, GraphNode source, GraphNode destination, TransportType transportType) throws Exception {
        this.transportType = transportType;
        this.destination = destination;

        g_cost = new double[graph.V()];
        edgeTo = new DirectedEdge[graph.V()];

        validateVertex(source);
        validateVertex(destination);

        for (int v = 0; v < graph.V(); v++) {
            g_cost[v] = Double.POSITIVE_INFINITY;
        }
        g_cost[source.getID()] = 0.0;

        pq = new IndexMinPq<>(graph.V());
        pq.insert(source.getID(), 0.0);

        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if(graph.getAllVertices().get(v) == destination) break;
            for (DirectedEdge e : graph.adjacent(graph.getAllVertices().get(v))) {
                boolean carIsAllowed = (transportType.equals(TransportType.CAR) || transportType.equals(TransportType.ALL)) && e.hasTransportType(TransportType.CAR);
                //filter illegal roads away
                if(e.hasTransportType(transportType)) {
                    relaxEdge(e, carIsAllowed);
                }
            }
        }
    }


    public void relaxEdge(DirectedEdge edge, boolean carIsAllowed) {
        GraphNode v = edge.from(), w = edge.to();
        int vID = v.getID(), wID = w.getID();

        double weight = edge.weight();

        if(carIsAllowed) {
            if(edge.getMaxSpeed() > 0) weight /= (edge.getMaxSpeed());
            else                       weight /= (50);
            //workaround. Something overrides some roads' max speeds so they're 0
        }

        if (g_cost[wID] > g_cost[vID] + weight) {
            g_cost[wID] = g_cost[vID] + weight; 
            edgeTo[wID] = edge;
            edge.setBeingDriven(carIsAllowed);
            double f_cost = g_cost[wID] + GraphNode.euclidianDistanceBetween(w, destination);

            if (pq.contains(wID))       pq.changeKey(wID, f_cost);
            else                        pq.insert(wID, f_cost);
        }
    }

    public boolean hasPathTo(GraphNode v) {
        return g_cost[v.getID()] < Double.POSITIVE_INFINITY;
    }

    public ArrayList<DirectedEdge> pathTo(GraphNode v) {
        if (!hasPathTo(v)) return null;
        ArrayList<DirectedEdge> path = new ArrayList<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v.getID()]; e != null; e = edgeTo[e.from().getID()]) {
            path.add(e);
        }
        return path;
    }

    //Checking if the vertex has a negative ID or bigger then the length
    public void validateVertex(GraphNode v) throws Exception {
        if(v.getID() < 0) throw new Exception("Vertex has negative ID");
        if(v.getID() > g_cost.length) throw new Exception("Vertex has ID greater than no. of verteces");
    }
}


