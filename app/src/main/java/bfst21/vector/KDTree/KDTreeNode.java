package bfst21.vector.KDTree;

import bfst21.vector.MapElements.MapFeature;
import bfst21.vector.osm.Node;

import java.util.ArrayList;
import java.util.List;

public class KDTreeNode {
    private final KDTreeNode left;
    private final KDTreeNode right;
    private final int depth;
    private final Node node;
    private List<MapFeature> mapElements;
    private boolean isLeaf = false;

    public KDTreeNode(Node node, int depth, KDTreeNode left, KDTreeNode right) {
        this.node = node;
        this.depth = depth;
        this.left = left;
        this.right = right;
        this.mapElements = null;
    }

    //leaf constructor
    public KDTreeNode(Node node, int depth, List<MapFeature> mapElements) {
        this.node = node;
        this.depth = depth;
        this.left = null;
        this.right = null;
        this.mapElements = mapElements;
        this.isLeaf = true;
    }

    public boolean isEven() {return depth % 2 == 0;}
    public List<MapFeature> getMapFeatures() {
        return mapElements;
    }
    public Node getNode() {
        return node;
    }
    public KDTreeNode getLeft() {
        return left;
    }
    public KDTreeNode getRight() {
        return right;
    }
    public boolean isLeaf() {
        return isLeaf;
    }

    public int size() {
        if(mapElements != null) return mapElements.size();
        return left.size() + right.size();
    }

    //Number of leaves
    public int leaves() {
        if(mapElements != null)
            return 1;
        return left.leaves() + right.leaves();
    }

    @Override
    public String toString() {
        String node = this.node.toString();
        if(right != null) node += right.toString();
        if(left  != null) node += left.toString();
        return node;
    }
}