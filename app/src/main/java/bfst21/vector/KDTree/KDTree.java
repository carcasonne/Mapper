package bfst21.vector.KDTree;

import bfst21.vector.Dijkstra.Coordinate;
import bfst21.vector.MapElements.MapFeature;
import bfst21.vector.MapElements.Road;
import bfst21.vector.osm.Node;

import java.util.ArrayList;
import java.util.List;


public class KDTree {
    private KDTreeNode root;
    private int leafSize;
    private MapFeature nearestMapFeature = null;

    public KDTree(List<MapFeature> mapFeatures, int leafSize) {
        this.leafSize = leafSize;
        this.root = buildKDTree(mapFeatures, 0);
    }

    private KDTreeNode buildKDTree(List<MapFeature> mapFeatures, int depth) {
        if(mapFeatures.size() < 1 || mapFeatures == null) {
            return null;
        }

        boolean isEven = depth % 2 == 0;
        int mid = mapFeatures.size()/2;

        //sort (either by x or y)
        Quick.select(mapFeatures, mapFeatures.size()/2);

        Node splitNode = mapFeatures.get(mid).getAvgCoord();

        //If mapFeatures is smaller than leafSize stop splitting (leaf)
        if(mapFeatures.size() <= leafSize) return new KDTreeNode(splitNode, depth, mapFeatures);

        List<MapFeature>[] listPair  = splitByMedian(mapFeatures, isEven);

        List<MapFeature> leftSubset  = listPair[0];
        List<MapFeature> rightSubset = listPair[1];
        splitNode = leftSubset.get(leftSubset.size()-1).getAvgCoord();

        //create left and right subtrees (children)
        KDTreeNode leftSubtree = buildKDTree(leftSubset, depth + 1);
        KDTreeNode rightSubtree = buildKDTree(rightSubset, depth + 1);

        return new KDTreeNode(splitNode, depth, leftSubtree, rightSubtree);
    }

    private List<MapFeature>[] splitByMedian(List<MapFeature> listToSplit, boolean even) {
        int mid = listToSplit.size()/2;

        List<MapFeature>[] arrayToReturn = (List<MapFeature>[]) new List[2];

        List<MapFeature> list_1 = new ArrayList<MapFeature>();
        List<MapFeature> list_2 = new ArrayList<MapFeature>();

        for(int i = 0; i < mid; i++) {
            //invert depthEven for all map features for each iteration
            listToSplit.get(i).setEven(!listToSplit.get(i).isEven());

            list_1.add(listToSplit.get(i));
        }

        for(int j = mid; j < listToSplit.size(); j++) {
            listToSplit.get(j).setEven(!listToSplit.get(j).isEven());

            list_2.add(listToSplit.get(j));
        }

        arrayToReturn[0] = list_1;
        arrayToReturn[1] = list_2;

        return arrayToReturn;
    }

    public int size() {
        return root.size();
    }


    public int leaves() {
        return root.leaves();
    }

    //Returns MapFeatures with avg-coordinate inside of range
    public ArrayList<MapFeature> query(KDRectangle rangeRect) {
        ArrayList<MapFeature> queryResult = new ArrayList<MapFeature>();
        KDRectangle startRegion = new KDRectangle((float) Double.NEGATIVE_INFINITY,
                                                  (float) Double.NEGATIVE_INFINITY,
                                                  (float) Double.POSITIVE_INFINITY,
                                                  (float) Double.POSITIVE_INFINITY);
        searchKDTree(root, startRegion, rangeRect, queryResult);

        return queryResult;
    }

    //Search for Node in KDTree
    private void searchKDTree(KDTreeNode localRoot, KDRectangle localRegion, KDRectangle rangeRegion, List<MapFeature> queryResult) {
        if(localRoot == null) return;
        if(localRoot.isLeaf()) {
            for(MapFeature mf : localRoot.getMapFeatures()) {
                if(rangeRegion.containsMapFeature(mf))
                    queryResult.add(mf);
            }
        } else {
            KDRectangle[] regionSplitByRoot = regionsSplit(localRoot, localRegion);
            KDRectangle leftRegion = regionSplitByRoot[0];
            KDRectangle rightRegion = regionSplitByRoot[1];
            KDTreeNode leftChild = localRoot.getLeft();
            KDTreeNode rightChild = localRoot.getRight();

            if(rangeRegion.fullyContains(leftRegion)) {
                reportSubTree(leftChild, queryResult);
            } else if(localRoot.isEven() && rangeRegion.intersectsX(leftRegion)) {
                searchKDTree(leftChild, leftRegion, rangeRegion, queryResult);
            } else {
                searchKDTree(leftChild, leftRegion, rangeRegion, queryResult);
            }

            if(rangeRegion.fullyContains(rightRegion)) {
                reportSubTree(rightChild, queryResult);
            } else if(localRoot.isEven() && rangeRegion.intersectsX(rightRegion)) {
                searchKDTree(rightChild, rightRegion, rangeRegion, queryResult);
            } else {
                searchKDTree(rightChild, rightRegion, rangeRegion, queryResult);
            }
        }
    }

    //Adding every leaf in subtree
    private void reportSubTree(KDTreeNode localRoot, List<MapFeature> queryResult) {
        if(localRoot.isLeaf()) {
            for(MapFeature mf : localRoot.getMapFeatures()) queryResult.add(mf);
            return;
        }

        KDTreeNode leftChild = localRoot.getLeft();
        KDTreeNode rightChild = localRoot.getRight();

        if(leftChild != null) reportSubTree(leftChild, queryResult);
        if(rightChild != null) reportSubTree(rightChild, queryResult);
    }

    //Splits the current region in two. Both horizontally and vertically
    private KDRectangle[] regionsSplit(KDTreeNode localRoot, KDRectangle region) {
        KDRectangle leftRegion;
        KDRectangle rightRegion;
        Node splitByNode = localRoot.getNode();

        if(localRoot.isEven()) {
            float splitBy = splitByNode.getX();
            leftRegion = new KDRectangle(region.xmin(), region.ymin(), splitBy, region.ymax());
            rightRegion = new KDRectangle(splitBy, region.ymin(), region.xmax(), region.ymax());
        } else {
            float splitBy = splitByNode.getY();
            leftRegion = new KDRectangle(region.xmin(), region.ymin(), region.xmax(), splitBy);
            rightRegion = new KDRectangle(region.xmin(), splitBy, region.xmax(), region.ymax());
        }

        KDRectangle[] regionArray = new KDRectangle[2];
        regionArray[0] = leftRegion;
        regionArray[1] = rightRegion;

        return regionArray;
    }

    //Finds the nearest node in the KD-Tree to the given x,y coordinates.
    public MapFeature getNearestMapFeature(double x, double y, boolean getRoad) throws NullPointerException{
        try {
            //be observant of y-coordinate. Is it being fetched from the map-canvas or does it use actual coordinates?
            Node queryNode = new Node((float) x, (float) y, true);
            nearestMapFeature = new MapFeature(null, null);
            nearestMapFeature.setAvgCoord(new Node((float) Double.POSITIVE_INFINITY, (float) Double.POSITIVE_INFINITY, true));

            getNearestMapFeature(root, queryNode, getRoad);

            Node nearestNode = new Node((float) Double.POSITIVE_INFINITY, (float) Double.POSITIVE_INFINITY, true);

            return nearestMapFeature;
        }catch (NullPointerException e){
            e.getStackTrace();
        }catch (Exception e){
            e.getStackTrace();
        }
        return nearestMapFeature;
    }

    private void getNearestMapFeature(KDTreeNode localRoot, Node queryNode, boolean getRoad) {
        Node localNode = localRoot.getNode();

        KDTreeNode nextSubtree = null;
        KDTreeNode oppositeSubtree = null;

        if(localRoot.isLeaf()) {
            inspectLeafForNN(localRoot, queryNode, getRoad);
        } else {
            //split by x
            if(localRoot.isEven()) {
                //look to the left first
                if(queryNode.getX() < localNode.getX()) {
                    nextSubtree = localRoot.getLeft();
                    oppositeSubtree = localRoot.getRight();

                //look to the right first
                } else if(queryNode.getX() > localNode.getX()) {
                    nextSubtree = localRoot.getRight();
                    oppositeSubtree = localRoot.getLeft();
                }

            //split by y
            } else {
                if(queryNode.getY() < localNode.getY()) {
                    nextSubtree = localRoot.getLeft();
                    oppositeSubtree = localRoot.getRight();

                } else if(queryNode.getY() > localNode.getY()) {
                    nextSubtree = localRoot.getRight();
                    oppositeSubtree = localRoot.getLeft();
                }
            }

            getNearestMapFeature(nextSubtree, queryNode, getRoad);
            inspectOppositeSubtree(oppositeSubtree, queryNode, getRoad);
        }
    }

    private void inspectOppositeSubtree(KDTreeNode localRoot, Node queryNode, boolean getRoad) {

        double distanceQueryToNN = queryNode.distanceSquaredTo(nearestMapFeature.getAvgCoord());
        double distanceQueryToRootX = Math.abs(queryNode.getX() - localRoot.getNode().getX());
        double distanceQueryToRootY = Math.abs(queryNode.getY() - localRoot.getNode().getY());

        //continue searching the subtree?
        if(localRoot.isEven()) {
            if(distanceQueryToNN > distanceQueryToRootX) getNearestMapFeature(localRoot, queryNode, getRoad);
        } else {
            if(distanceQueryToNN > distanceQueryToRootY) getNearestMapFeature(localRoot, queryNode, getRoad);
        }
    }

    private void inspectLeafForNN(KDTreeNode treeNode, Node queryNode, boolean getRoad) {
        for (MapFeature mf : treeNode.getMapFeatures()) {
            double distanceToNewMapFeature = queryNode.distanceSquaredTo(mf.nearestNodeToPoint(new Coordinate(queryNode.getX(), queryNode.getY())));
            double distanceToOldMapFeature = queryNode.distanceSquaredTo(nearestMapFeature.getAvgCoord());
            if(getRoad && mf instanceof Road) {
                if(((Road) mf).getRoadName() != null){
                    if(distanceToOldMapFeature > distanceToNewMapFeature) {
                        nearestMapFeature = mf;
                    }
                }
            }
        }
    }
}