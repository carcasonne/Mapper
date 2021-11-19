package bfst21.vector;

import bfst21.vector.MapElements.Drawable;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Merger {

    //Merges the relations that has more than one way, else returns the way it contains
    public ArrayList<Way> mergeOuterRelations(ArrayList<Way> relationWays) {
        if (relationWays.size() < 2){
            return relationWays;
        } else {
            ArrayList<Way> currentRelationWays = new ArrayList<>();
            for (Way w : relationWays){
                currentRelationWays.add(w);
            }
            Way mergedWay = currentRelationWays.remove(0);
            int i = 0;
            while (i < relationWays.size()-1){
                for (Way w : currentRelationWays){
                    if (mergedWay.last() == w.last() || mergedWay.last() == w.first()){
                        mergedWay = mergedWay.relationMerge(mergedWay, w);
                        currentRelationWays.remove(w);
                        break;
                    }
                }
                i++;
            }
            currentRelationWays.add(mergedWay);
            return currentRelationWays;
        }
    }

    //Merges the coastlines for each island on the map
    public List<Drawable> mergeCoastLines(ArrayList<Way> coastlines) {
        Map<Node, Way> pieces = new HashMap<>();
        for(var coast : coastlines){
            var before = pieces.remove(coast.first());
            var after = pieces.remove(coast.last());
            if(before == after) after = null;
            var merged = Way.merge(before, coast, after);
            pieces.put(merged.first(), merged);
            pieces.put(merged.last(), merged);
        }
        List<Drawable> merged = new ArrayList<>();
        pieces.forEach((node, way) -> {
            if(way.last() == node){
                merged.add(way);
            }
        });
        return merged;
    }
}
