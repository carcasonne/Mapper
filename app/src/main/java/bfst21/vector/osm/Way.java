package bfst21.vector.osm;

import bfst21.vector.MapElements.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Way implements Drawable, Comparable<Object> {
    protected String name;
    protected String type;
    protected List<Node> nodes = new ArrayList<>();

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public Node getStartPoint(){ return nodes.get(0); }
    public Node getEndPoint(){ return nodes.get(nodes.size() - 1); }

    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public void trace(GraphicsContext gc) {
        gc.moveTo(nodes.get(0).getX(), nodes.get(0).getY());
        for (var node : nodes) {
            gc.lineTo(node.getX(), node.getY());
        }
    }

    public Node first(){
        return nodes.get(0);
    }
    public Node last() { return nodes.get(nodes.size()-1); }

    //Merges two ways together and returns the new way they form
    public static Way merge(Way first, Way second) {
        if(first == null) return second;
        if(second == null) return first;
        Way merged = new Way();
        merged.nodes.addAll(first.nodes);
        merged.nodes.addAll(second.nodes.subList(1, second.nodes.size()));
        return merged;
    }

    public static Way merge(Way before, Way coast, Way after) {
        return merge(merge(before,coast), after);
    }

    //Merges two ways when making a relation, and checks if the second way should be reverted, if the ways are connected oppositely
    public static Way relationMerge(Way first, Way second){
        if (first.nodes.get(0) == second.nodes.get(0) || first.nodes.get(first.nodes.size()-1) == second.nodes.get(second.nodes.size()-1)){
            Way revertedSecond = new Way();
            int i = second.nodes.size() - 1;
            while (i >= 0){
                revertedSecond.nodes.add(second.nodes.get(i));
                i--;
            }
            return merge(first, revertedSecond);
        }
        return merge(first, second);
    }

    @Override
    public boolean equals(Object obj){ //Checks if the way is equal to another way by checking each of their nodes
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Way other = (Way) obj;
        if(nodes == null){
            if(other.nodes != null){
                return false;
            }
        } else if(!nodes.equals(other.nodes)){
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) { //Compares the way to an object, to see if the object is contained inside it's nodes' list
        if(o == null) return 1;
        if(nodes.contains(o)){
            return 1;
        } else {
            return 0;
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int size() {
        return nodes.size();
    }
}
