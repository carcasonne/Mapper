package bfst21.vector;

import bfst21.vector.osm.Node;

import java.util.ArrayList;
import java.util.List;

public class LongIndex {
    List<Node> nodes = new ArrayList<>();
    boolean sorted = true;

    public void put(Node node){
        nodes.add(node);
        sorted = false;
    }

    //Returns the node assigned under the input long
    public Node get(long ref){
        if (!sorted){
            nodes.sort((a, b) -> Long.compare(a.getID(), b.getID()));

            sorted = true;
        }
        int lo = 0;
        int hi = nodes.size();
        while (lo + 1 < hi) {
            int mi = (lo + hi) / 2;
            if (nodes.get(mi).getID() <= ref){
                lo = mi;
            } else {
                hi = mi;
            }
        }
        Node node = nodes.get(lo);
        if (node.getID() == ref){
            return node;
        } else {
            return null;
        }
    }
}
