package bfst21.vector;

import bfst21.vector.Dijkstra.Coordinate;
import bfst21.vector.Dijkstra.DirectedEdge;
import bfst21.vector.Dijkstra.Graph;
import bfst21.vector.Dijkstra.GraphNode;
import bfst21.vector.KDTree.KDTree;
import bfst21.vector.MapElements.*;
import bfst21.vector.addressparser.Address;
import bfst21.vector.osm.Node;
import bfst21.vector.osm.Way;
import bfst21.vector.radix.RadixTree;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class Model implements Iterable<Drawable> {
    Merger merger = new Merger();
    List<Drawable> shapes;
    List<Runnable> observers = new ArrayList<>();
    List<MapFeature> roads = new ArrayList<>();
    List<Road> roadBridges = new ArrayList<>();
    List<Building> buildings = new ArrayList<>();
    List<Landuse> landuses = new ArrayList<>();
    List<Leisure> leisures = new ArrayList<>();
    List<Electrified> electrifieds = new ArrayList<>();
    List<Natural> naturals = new ArrayList<>();
    List<Waterway> waterways = new ArrayList<>();
    List <Man_Made> man_mades = new ArrayList<>();
    List <Amenity> amenities = new ArrayList<>();
    List <Aeroway> aeroways = new ArrayList<>();
    List <MapFeature> priorityZones = new ArrayList<>();
    List <Drawable> islands = new ArrayList<>();
    Graph graph;
    List<DirectedEdge> shortestPath = new ArrayList<>();
    List<Pointer> pointers = new ArrayList<>();
    KDTree kdtree0;
    KDTree kdtree1;
    KDTree kdtree2;
    KDTree kdtree3;
    KDTree kdtree4;
    KDTree kdtree_roads;
    ArrayList<Way> tempRelation = new ArrayList<>();
    String relationType;

    RedBlackBST<Long, Way> idToWay = new RedBlackBST<>();
    RadixTree<RadixTree<Coordinate>> radixTree = new RadixTree<RadixTree<Coordinate>>();
    long wayID;
    Coordinate coordinate;

    float minx, miny, maxx, maxy;

    public List<DirectedEdge> getShortestPath() {
        return shortestPath;
    }

    public List<Pointer> getPointers() {
        return pointers;
    }

    public void loadRoadIntoGraph(Road road) {
        boolean isOneWay = road.isOneWay();
        boolean isCarway = road.isCarWay();
        boolean isFootway = road.isFootWay();

        for (int i = 0; i + 1 < road.getWay().size(); i++) {
            GraphNode v = new GraphNode(road.getWay().getNodes().get(i), road.getMaxSpeed());
            GraphNode w = new GraphNode(road.getWay().getNodes().get(i + 1), road.getMaxSpeed());

            graph.createPath(v, w, road.getRoadName(), isOneWay, isCarway, isFootway);
        }
    }

    public Model(String filename) throws FactoryConfigurationError, Exception {
        load(filename);
    }

    //Checks for loaded filetype
    public void load(String filename) throws FactoryConfigurationError, Exception {
        long time = -System.nanoTime();
        graph = new Graph();
        if (filename.endsWith(".txt")) {
            shapes = Files.lines(Path.of(filename)).map(Line::new).collect(Collectors.toList());
        } else if (filename.endsWith(".osm")) {
            loadOSM(filename);
        } else if (filename.endsWith(".zip")) {
            loadZIP(filename);
        }

        sortTrees();
        time += System.nanoTime();
        System.out.println("Load time program: " + time / 1000000 + "ms");
    }

    private void loadZIP(String filename) throws IOException, XMLStreamException {
        var zip = new ZipInputStream(new FileInputStream(filename));
        zip.getNextEntry();
        loadOSM(zip);
    }

    private void loadOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        loadOSM(new FileInputStream(filename));
    }

    private void loadOSM(InputStream input) throws XMLStreamException, FactoryConfigurationError {
        XMLStreamReader reader = XMLInputFactory
            .newInstance()
            .createXMLStreamReader(new BufferedInputStream(input));
        var idToNode = new LongIndex();
        //Sets all possible elements to null from the start
        Address.Builder builder = null;
        Way way = null;
        Building building = null;
        Road road = null;
        Landuse landuse = null;
        Leisure leisure = null;
        Natural natural = null;
        Waterway waterway = null;
        Man_Made man_made = null;
        Amenity amenity = null;
        Electrified electrified = null;
        Aeroway aeroway = null;

        shapes = new ArrayList<>();
        //falsifies every boolean from the start
        boolean iscoastline = false;
        boolean isbuilding = false;
        boolean isRoad = false;
        boolean isLanduse = false;
        boolean isLeisure = false;
        boolean isNatural = false;
        boolean isWaterway = false;
        boolean roadBridge = false;
        boolean isMan_Made = false;
        boolean isAmenity = false;
        boolean isElectrified = false;
        boolean isMultipolygon = false;
        boolean isPriorityZone = false;
        boolean isAeroway = false;
        boolean hasAddress = false;

        var coastlines = new ArrayList<Way>();

        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "bounds":
                            minx = Float.parseFloat(reader.getAttributeValue(null, "minlon"));
                            maxx = Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
                            maxy = Float.parseFloat(reader.getAttributeValue(null, "minlat"))/-0.56f;
                            miny = Float.parseFloat(reader.getAttributeValue(null, "maxlat"))/-0.56f;
                            break;
                        case "node":
                            var id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            var lon = Float.parseFloat(reader.getAttributeValue(null, "lon"));
                            var lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
                            idToNode.put(new Node(id, lat, lon));
                            hasAddress = false;
                            coordinate = new Coordinate(lat, lon);
                            builder = new Address.Builder();
                            break;
                        case "way":
                            way = new Way();
                            iscoastline = false;
                            isbuilding = false;
                            isRoad = false;
                            isNatural = false;
                            isLanduse = false;
                            isLeisure = false;
                            isWaterway = false;
                            isMan_Made = false;
                            roadBridge = false;
                            isAmenity = false;
                            isElectrified = false;
                            isPriorityZone = false;
                            isAeroway = false;
                            wayID = Long.parseLong(reader.getAttributeValue(null, "id"));
                            break;
                        case "nd":
                            //parses the node reference to each of the ways
                            var ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            way.add(idToNode.get(ref));
                            break;
                        case "tag":
                            //assigns every relevant types to the ways
                                var k = reader.getAttributeValue(null, "k");
                                var v = reader.getAttributeValue(null, "v");
                                if (k.equals("bridge")){
                                    roadBridge = true;
                                }
                                if (k.equals("natural") && v.equals("coastline")) {
                                    iscoastline = true;
                                } else if (k.equals("building")) {
                                    isbuilding = true;
                                    building = new Building(way, v);
                                    relationType = v;
                                } else if(k.equals("highway") && !v.equals("proposed")){ //removes proposed roads that doesn't exist.
                                    isRoad = true;
                                    road = new Road(way, v);
                                    if (road.getType().contains("_link")){
                                        road.setType(road.getType().split("_")[0]);
                                    }
                                    if (roadBridge){
                                        road.setBridge(true);
                                    }
                                } else if((k.equals("natural") && !v.equals("coastline")) && (v.equals("wood") || v.equals("water") || v.equals("grassland") || v.equals("heath") || v.equals("scrub"))){
                                    isNatural = true;
                                    natural = new Natural(way, v);
                                    relationType = v;
                                } else if (k.equals("electrified")) {
                                    isElectrified = true;
                                    electrified = new Electrified(way, v);
                                } else if (k.equals("landuse") && v.equals("military")) {
                                    isPriorityZone = true;
                                    landuse = new Landuse(way, v);
                                    relationType = v;
                                } else if (k.equals("landuse") && (v.equals("residential") || v.equals("commerical") || v.equals("industrial") || v.equals("retail") || v.equals("forest"))){
                                    isLanduse = true;
                                    if (v.equals("residential")){
                                        v = "residentialArea";
                                    }
                                    landuse = new Landuse(way, v);
                                    relationType = v;
                                } else if (k.equals("leisure") && v.equals("park")) {
                                    isLeisure = true;
                                    leisure = new Leisure(way, v);
                                    relationType = v;
                                } else if (k.equals("waterway")) {
                                    isWaterway = true;
                                    waterway = new Waterway(way, v);
                                } else if (k.equals("man_made")) {
                                    isMan_Made = true;
                                    man_made = new Man_Made(way, v);
                                    relationType = v;
                                } else if (k.equals("amenity") && v.equals("grave_yard")){
                                    isAmenity = true;
                                    amenity = new Amenity(way,v);
                                    relationType = v;
                                } else if (k.equals("aeroway")){
                                    isAeroway = true;
                                    aeroway = new Aeroway(way,v);
                                }
                                if (k.equals("type") && v.equals("multipolygon")){
                                    isMultipolygon = true;
                                }
                            //Assigns maxspeed to roads and ignores non-integer inputs from the OSM file.
                            if(isRoad) {
                                if(k.equals("maxspeed")){
                                    if(v.equals("signals")) break; //idk
                                    else if (v.equals("default")) break; //idk either
                                    else if (v.equals("implicit")) break; //idk either... again
                                    else if (v.equals("none")) break; //idk either... again again
                                    try {
                                        road.setMaxSpeed(Integer.parseInt(v));
                                    }
                                    catch(Exception NumberFormatException) { //in case maxspeed is a double
                                        int speed = (int) Math.round(Double.parseDouble(v));
                                        road.setMaxSpeed(speed);
                                    }
                                }
                                if(k.equals("name")){
                                    road.setRoadName(v);
                                }
                                if(k.equals("oneway")) {
                                    if(v.equals("yes")) road.setOneWay(true);
                                }
                                if(k.equals("lanes")) {
                                    if(v.equals("forward")) road.setOneWay(true);
                                }
                            }

                            //Adding addresses to buildings
                            if(k.equals("addr:street")){
                                if (builder != null) {
                                    builder.street(v);
                                    hasAddress = true;
                                }
                            } else if(k.equals("addr:housenumber")){

                                if (builder != null) {
                                    builder.house(v);
                                    hasAddress = true;
                                }
                            } else if(k.equals("addr:postcode")){
                                if (builder != null) {
                                    builder.postcode(v);
                                    hasAddress = true;
                                }
                            } else if(k.equals("addr:city")){
                                if(v != null){
                                    if (builder != null) {
                                        builder.city(v);
                                        hasAddress = true;
                                    }
                                }
                            }
                            break;
                        case "relation":
                            tempRelation.clear();
                            isMultipolygon = false;
                            iscoastline = false;
                            isbuilding = false;
                            isRoad = false;
                            isNatural = false;
                            isLanduse = false;
                            isLeisure = false;
                            isWaterway = false;
                            isMan_Made = false;
                            roadBridge = false;
                            isAmenity = false;
                            isElectrified = false;
                            isPriorityZone = false;
                            isAeroway = false;
                            break;

                        case "member":
                            //Assigns types and relevant details to the relations
                            Way relationWay = new Way();

                            long refID = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            var role = reader.getAttributeValue(null, "role");
                            if (idToWay.get(refID) != null){
                                relationWay = idToWay.get(refID);

                            } else {
                                relationWay = null;
                            }

                            if (relationWay != null && role.equals("outer")){
                                tempRelation.add(relationWay);
                            }
                            break;
                    }
                    break;
                case END_ELEMENT:
                    //Finishes each node, way and relation in the file
                    switch (reader.getLocalName()) {
                        case "node":
                            if(hasAddress){
                                Address address = builder.build();
                                if(address.getStreet() != null){
                                    if (!radixTree.containsKey(address.getStreet())) {
                                        RadixTree<Coordinate> radix = new RadixTree<>();
                                        if(address.houseNumber != null){
                                            radix.put(address.housePostcodeCityString(), coordinate);
                                            radixTree.put(address.getStreet(), radix);
                                        }
                                    } else {
                                        if(address.houseNumber != null){
                                            radixTree.get(address.getStreet()).put(address.housePostcodeCityString(), coordinate);
                                        }
                                    }
                                }
                            }

                            break;
                        case "way":
                            idToWay.put(wayID, way);
                            if (iscoastline) coastlines.add(way);
                            else if (isbuilding) buildings.add(building);
                            else if (isRoad){
                                loadRoadIntoGraph(road);
                                roads.add(road);
                                if (road.isBridge()){
                                    roadBridges.add(road);
                                }
                            }
                            else if (isNatural) naturals.add(natural);
                            else if (isElectrified) electrifieds.add(electrified);
                            else if (isLanduse) landuses.add(landuse);
                            else if (isLeisure) leisures.add(leisure);
                            else if (isWaterway) waterways.add(waterway);
                            else if (isMan_Made) man_mades.add(man_made);
                            else if (isAmenity) amenities.add(amenity);
                            else if (isPriorityZone) priorityZones.add(landuse);
                            else if (isAeroway) aeroways.add(aeroway);
                            break;
                        case "relation":
                            if (isMultipolygon){
                                var mergedRelation = merger.mergeOuterRelations(tempRelation);

                                if(isLanduse){
                                    for (Way w : mergedRelation){
                                        Landuse landuseRelation = new Landuse(w, relationType);
                                        landuses.add(landuseRelation);
                                    }
                                } else if (isAmenity){
                                    for (Way w : mergedRelation){
                                        Amenity amenityRelation = new Amenity(w, relationType);
                                        amenities.add(amenityRelation);
                                    }
                                } else if (isNatural){
                                    for (Way w : mergedRelation){
                                        Natural naturalRelation = new Natural(w, relationType);
                                        naturals.add(naturalRelation);
                                    }
                                } else if (isLeisure){
                                    for (Way w : mergedRelation){
                                        Leisure leisureRelation = new Leisure(w, relationType);
                                        leisures.add(leisureRelation);
                                    }
                                } else if (isbuilding){
                                    for (Way w : mergedRelation){
                                        Building buildingRelation = new Building(w, relationType);
                                        buildings.add(buildingRelation);
                                    }
                                } else if (isPriorityZone){
                                    for (Way w : mergedRelation){
                                        Landuse landuseRelation = new Landuse(w, relationType);
                                        priorityZones.add(landuseRelation);
                                    }
                                }
                            }
                            break;
                    }
                    break;
            }
        }
        islands = merger.mergeCoastLines(coastlines);
    }

    public void save(String filename) throws FileNotFoundException {
        try (var out = new PrintStream(filename)) {
            for (var line : shapes)
                out.println(line);
        }
    }

    void addObserver(Runnable observer) {
        observers.add(observer);
    }
    void notifyObservers() {
        for (var observer : observers) observer.run();
    }

    @Override
    public Iterator<Drawable> iterator() {
        return shapes.iterator();
    }

	public void add(Line line) {
        shapes.add(line);
        notifyObservers();
	}

	//Sorts each of the trees with the correct elements for each zoom level. 20001 leafsize worked best for us after testing out different sizes
    public void sortTrees(){
        kdtree0 = new KDTree(zoomList0(), 20001);
        kdtree1 = new KDTree(zoomList1(), 20001);
        kdtree2 = new KDTree(zoomList2(), 20001);
        kdtree3 = new KDTree(zoomList3(), 20001);
        kdtree4 = new KDTree(zoomList4(), 20001);
        kdtree_roads = new KDTree(roads, 20001);
    }

    public ArrayList<MapFeature> zoomList0(){
        ArrayList<MapFeature> areaList = new ArrayList<>();
        for (MapFeature feature : roads){
            if (feature.getType().equals("motorway") || feature.getType().equals("trunk") || feature.getType().equals("primary")){
                areaList.add(feature);
            }
        }
        return areaList;
    }

    public ArrayList<MapFeature> zoomList1(){
        ArrayList<MapFeature> areaList = new ArrayList<>();
        for (MapFeature feature : landuses){
            if (feature.getType().equals("residentialArea") ||
                    feature.getType().equals("commercial") ||
                    feature.getType().equals("industrial") ||
                    feature.getType().equals("retail")) {
                areaList.add(feature);
            }
        }
        for (MapFeature feature : naturals){
            if (feature.getType().equals("water")){
                areaList.add(feature);
            }
        }
        for (MapFeature feature : roads){
            if (feature.getType().equals("secondary")){
                areaList.add(feature);
            }
        }
        return areaList;
    }

    public ArrayList<MapFeature> zoomList2(){
        ArrayList<MapFeature> areaList = new ArrayList<>();
        for (MapFeature feature : landuses){
            if (feature.getType().equals("forest")){
                areaList.add(feature);
            }
        }
        for (MapFeature feature : naturals){
            if (feature.getType().equals("wood")){
                areaList.add(feature);
            }
        }
        for (MapFeature feature : roads){
            if (feature.getType().equals("tertiary")){
                areaList.add(feature);
            }
        }
        return areaList;
    }

    public ArrayList<MapFeature> zoomList3(){
        ArrayList<MapFeature> areaList = new ArrayList<>();
        for (MapFeature feature : roads){
            if (!feature.getType().equals("motorway") && !feature.getType().equals("primary") && !feature.getType().equals("secondary") && !feature.getType().equals("tertiary")){
                areaList.add(feature);
            }
        }
        areaList.addAll(aeroways);
        areaList.addAll(electrifieds);
        for (MapFeature feature : waterways){
            if (feature.getType().equals("river") ||
                    feature.getType().equals("riverbank") ||
                    feature.getType().equals("canal")) {
                areaList.add(feature);
            }
        }
        areaList.addAll(leisures);
        areaList.addAll(priorityZones);
        for (MapFeature feature : naturals){
            if (feature.getType().equals("heath") ||
                    feature.getType().equals("scrub") ||
                    feature.getType().equals("grassland")) {
                areaList.add(feature);
            }
        }
        return areaList;
    }

    public ArrayList<MapFeature> zoomList4(){
        ArrayList<MapFeature> areaList = new ArrayList<>();
        for(MapFeature feature : waterways){
            if (!feature.getType().equals("river") && !feature.getType().equals("riverbank") && !feature.getType().equals("canal")) {
                areaList.add(feature);
            }
        }
        areaList.addAll(amenities);
        areaList.addAll(man_mades);
        areaList.addAll(buildings);
        return areaList;
    }

}
