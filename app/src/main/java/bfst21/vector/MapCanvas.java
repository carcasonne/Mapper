package bfst21.vector;

import bfst21.vector.Dijkstra.DirectedEdge;
import bfst21.vector.KDTree.KDRectangle;
import bfst21.vector.MapElements.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapCanvas extends Canvas {
    private Model model;
    private Affine trans = new Affine();
    private KDRectangle currentview = new KDRectangle(0,0,0,0);
    private MapTheme mapTheme = new MapTheme();
    private GraphicsContext gc;
    final ContextMenu contextMenu = new ContextMenu();
    ComboBox<String> contextMenuComboBox = new ComboBox<String>();

    HashMap<String, Color> typeToColorMap = mapTheme.getColors();
    ArrayList<MapFeature> toBeAddedLater = new ArrayList<>();

    public void init(Model model) {
        this.model = model;
        pan(-model.minx,-model.miny);
        zoom(getWidth()/(model.maxx -model.minx), new Point2D(0,0));
    }

    public void repaint0(List<MapFeature> zoomList0) { //Highways, trunks, primary roads and coastlines only.
        contextMenu.hide();
        gc = getGraphicsContext2D();
        gc.save();
        gc.setTransform(new Affine());
        gc.setFill(typeToColorMap.get("natural_water_color"));
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(trans);
        gc.setFill(typeToColorMap.get("background"));
        for (var line : model.islands)
            line.fill(gc);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        for (var line : model)
            line.draw(gc);
        gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
        gc.setStroke(Color.valueOf("#000000"));
        gc.setLineDashes(0.00);

        for (var feature : zoomList0) {
            toBeAddedLater.add(feature);
        }
    }

    public void repaint1(List<MapFeature> zoomList0, List<MapFeature> zoomList1) {//Cities, lakes and secondary roads only.
        repaint0(zoomList0);

        for (var feature : zoomList1) {
            if (feature instanceof Landuse) {
                gc.setFill(typeToColorMap.get("landuse_residential_color"));
                gc.setStroke(typeToColorMap.get("landuse_residential_color"));
                feature.getWay().draw(gc);
                gc.fill();

            } else if (feature instanceof Road) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Natural) {
                gc.setFill(typeToColorMap.get("natural_water_color"));
                gc.setStroke(typeToColorMap.get("natural_water_color"));
                feature.getWay().draw(gc);
                gc.fill();
            }
        }
    }

    public void repaint2(List<MapFeature> zoomList0, List<MapFeature> zoomList1, List<MapFeature> zoomList2){//Tertiary roads and forests only.
        repaint1(zoomList0, zoomList1);
        for (var feature : zoomList2) {
            if (feature instanceof Road) {
                toBeAddedLater.add(feature);

            } else {
                gc.setFill(typeToColorMap.get("landuse_forest_color"));
                gc.setStroke(typeToColorMap.get("landuse_forest_color"));
                feature.getWay().draw(gc);
                gc.fill();
            }
        }
    }

    public void repaint3(List<MapFeature> zoomList0, List<MapFeature> zoomList1, List<MapFeature> zoomList2, List<MapFeature> zoomList3){//The rest but not buildings, graveyards and man made bridges.
        repaint2(zoomList0, zoomList1, zoomList2);
        for (var feature : zoomList3) {
            if (feature instanceof Road) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Aeroway) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Electrified) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Waterway) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Leisure) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Natural) {
                gc.setFill(typeToColorMap.get("natural_heath_color"));
                gc.setStroke(typeToColorMap.get("natural_heath_color"));
                feature.getWay().draw(gc);
                gc.fill();

            } else if (feature.getType().equals("military")) {
                toBeAddedLater.add(feature);

            } else if (feature.getType().equals("road_bridge")) {
                gc.setLineWidth(1.5 / Math.sqrt(trans.determinant()));
                gc.setStroke(typeToColorMap.get("bridge_color"));
                feature.getWay().draw(gc);
                gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                gc.setStroke(Color.valueOf("#000000"));
            }
        }
    }

    public void repaint4(List<MapFeature> zoomList0, List<MapFeature> zoomList1, List<MapFeature> zoomList2, List<MapFeature> zoomList3, List<MapFeature> zoomList4){//The rest (Mostly buildings).
        repaint3(zoomList0, zoomList1, zoomList2, zoomList3);
        for (var feature : zoomList4) {

            if (feature instanceof Waterway) {
                toBeAddedLater.add(feature);

            } else if (feature instanceof Amenity) {
                gc.setFill(typeToColorMap.get("amenity_graveyard_color"));
                feature.getWay().draw(gc);
                gc.fill();

            } else if (feature instanceof Man_Made) {
                if (feature.getType().equals("bridge")) {
                    gc.setFill(typeToColorMap.get("manmade_bridge_color"));
                    feature.getWay().draw(gc);
                    gc.fill();

                } else if (feature.getType().equals("pier")) {
                    gc.setFill(typeToColorMap.get("manmade_pier_color"));
                    feature.getWay().draw(gc);
                    gc.fill();
                }

            } else if (feature instanceof Building) {
                toBeAddedLater.add(feature);
            }
        }
    }

    //A list for the map elements that needs to be added later, so it will be drawn on top of other elements (such as roads)
    public void repaintAddedLater(){
        for(MapFeature feature : toBeAddedLater) {
            if(feature instanceof Building) {
                gc.setFill(typeToColorMap.get("building_color"));
                gc.setStroke(typeToColorMap.get("building_color"));
                feature.getWay().draw(gc);
                gc.fill();
                gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                gc.setStroke(Color.valueOf("#000000"));

            } else if (feature.getType().equals("park")) {
                gc.setFill(typeToColorMap.get("leisure_park_color"));
                gc.setStroke(typeToColorMap.get("leisure_park_color"));
                feature.getWay().draw(gc);
                gc.fill();

            } else if(feature instanceof Road) {
                if (feature.getType().equals("primary")) {
                    gc.setLineWidth((2.5/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_primary_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("secondary")) {
                    gc.setLineWidth((2/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_secondary_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("tertiary")) {
                    gc.setLineWidth((1.5/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_tertiary_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("unclassified")) {
                    gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_unclassified_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("residential")) {
                    gc.setLineWidth(1.2 / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_residential_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("motorway")) {
                    gc.setLineWidth((getZoomFactor() + 1) / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_motorway_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("trunk")) {
                    gc.setLineWidth((3/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_trunk_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("living_street")) {
                    gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_livingStreet_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("service")) {
                    gc.setLineWidth(0.8 / Math.sqrt(trans.determinant()));
                    gc.setStroke(typeToColorMap.get("road_service_color"));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("pedestrian")) {
                    gc.setStroke(typeToColorMap.get("road_pedestrian_color"));
                    feature.getWay().draw(gc);
                    gc.setStroke(Color.valueOf("#000000"));

                } else if (feature.getType().equals("track")) {
                    gc.setStroke(typeToColorMap.get("road_track_color"));
                    feature.getWay().draw(gc);
                    gc.setStroke(Color.valueOf("#000000"));

                } else {
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()) );
                    gc.setStroke(typeToColorMap.get("road_default_color"));
                    feature.getWay().draw(gc);
                    gc.setStroke(Color.valueOf("#000000"));
                }

            }  else if(feature instanceof Aeroway) {
                gc.setStroke(typeToColorMap.get("aeroway_default_color"));
                if (feature.getType().equals("runway")) {
                    gc.setLineWidth((6/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));
                } else if (feature.getType().equals("taxiway")) {
                    gc.setLineWidth((1.5/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    feature.getWay().draw(gc);
                    gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                    gc.setStroke(Color.valueOf("#000000"));
                }

            } else if(feature instanceof Waterway) {
                gc.setStroke(typeToColorMap.get("natural_water_color"));
                if (feature.getType().equals("river") ||
                        feature.getType().equals("riverbank") ||
                        feature.getType().equals("canal")) {

                    gc.setLineWidth((4.5/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    feature.getWay().draw(gc);

                } else {
                    gc.setLineWidth((2/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                    feature.getWay().draw(gc);
                }
                gc.setStroke(typeToColorMap.get("#000000"));
                gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));

            } else if (feature.getType().equals("contact_line")) {
                gc.setLineWidth((2.5/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                gc.setStroke(typeToColorMap.get("electrified_contactLine_color"));
                feature.getWay().draw(gc);
                gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                gc.setStroke(Color.valueOf("#000000"));

            } else if (feature.getType().equals("rail")) {
                gc.setLineWidth((2.5/(5-getZoomFactor())) / Math.sqrt(trans.determinant()));
                gc.setStroke(typeToColorMap.get("electrified_rail_color"));
                feature.getWay().draw(gc);
                gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                gc.setStroke(Color.valueOf("#000000"));

            } else if(feature instanceof Landuse && feature.getType().equals("military")) {
                gc.setFill(typeToColorMap.get("priorityzone_military_color"));
                feature.getWay().draw(gc);
                gc.fill();
            }
        }
        if(model.getShortestPath() != null) {
            for(DirectedEdge e : model.getShortestPath()) {
                gc.setLineWidth(2.5 / Math.sqrt(trans.determinant()));
                if (e.getIsBeingDriven()){
                    gc.setStroke(typeToColorMap.get("dijkstra_edges_driven"));
                    e.draw(gc);
                    gc.setStroke(typeToColorMap.get("dijkstra_vertices_driven"));
                    e.from().draw(gc);
                    e.to().draw(gc);
                } else {
                    gc.setStroke(typeToColorMap.get("dijkstra_edges_notDriven"));
                    e.draw(gc);
                    gc.setStroke(typeToColorMap.get("dijkstra_vertices_notDriven"));
                    e.from().draw(gc);
                    e.to().draw(gc);
                }
                gc.setLineWidth(0.5 / Math.sqrt(trans.determinant()));
                gc.setStroke(Color.valueOf("#000000"));
            }
        }

        currentview.draw(gc);

        Point2D xymin = new Point2D(trans.getTx()*(-1)/trans.getMxx(), (trans.getTy()*(-1)/trans.getMxx()));
        Point2D xymax = new Point2D(trans.getTx()*(-1)/trans.getMxx()+540/trans.getMxx(), trans.getTy()*(-1)/trans.getMxx()+380/trans.getMxx());
        toBeAddedLater.clear();
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenuComboBox(ComboBox<String> cbox) {
        contextMenuComboBox = cbox;
    }

    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        updateView();
        smartRepaint();
    }

    public void zoom(double factor, Point2D center) {
        trans.prependScale(factor, factor, center);
        updateView();
        smartRepaint();
    }

    //Returns the current zoom level the user is on
    public int getZoomFactor (){
        if (trans.getMxx() < 500) {
            return 0;
        } else if (trans.getMxx() >= 500 && trans.getMxx() < 1000){
            return 1;
        } else if (trans.getMxx() >= 1000 && trans.getMxx() <= 4000){
            return 2;
        } else if (trans.getMxx() > 4000 && trans.getMxx() <= 30000){
            return 3;
        }
        return 4;
    }

    //Repaints accordingly in terms of which zoom level the user is on.
    public void smartRepaint(){
        if (getZoomFactor() == 0){
            for(Pointer p : model.getPointers()) {
                p.setFat();
            }

            repaint0(model.kdtree0.query(currentview));
            repaintAddedLater();
        } else if (getZoomFactor() == 1){
            for(Pointer p : model.getPointers()) {
                p.setBig();
            }
            repaint1(model.kdtree0.query(currentview),model.kdtree1.query(currentview));
            repaintAddedLater();
        } else if (getZoomFactor() == 2){
            for(Pointer p : model.getPointers()) {
                p.setMedium();
            }
            repaint2(model.kdtree0.query(currentview),model.kdtree1.query(currentview),model.kdtree2.query(currentview));
            repaintAddedLater();
        } else if (getZoomFactor() == 3){
            for(Pointer p : model.getPointers()) {
                p.setSmall();
            }
            repaint3(model.kdtree0.query(currentview),model.kdtree1.query(currentview),model.kdtree2.query(currentview),model.kdtree3.query(currentview));
            repaintAddedLater();
        } else if (getZoomFactor() == 4){
            for(Pointer p : model.getPointers()) {
                p.setVerySmall();
            }
            repaint4(model.kdtree0.query(currentview),model.kdtree1.query(currentview),model.kdtree2.query(currentview),model.kdtree3.query(currentview),model.kdtree4.query(currentview));
            repaintAddedLater();
        }

        gc = getGraphicsContext2D();
        for(Pointer p : model.getPointers()) {
            p.draw(gc);
        }
    }

    //Returns the point where the mouse is on the map
    public Point2D mouseToModelCoords(Point2D point) {
        try {
            return trans.inverseTransform(point);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
            return null;
        }
	}

    public Affine zoomLevelReturner(){
        return trans;
    }

    //Updates the KD-tree view and adds an additional 150 pixels
	public void updateView(){
        int padding = 150;

        currentview.setXmin((float) ((trans.getTx()*(-1)-padding)/trans.getMxx()));
        currentview.setYmin((float) ((trans.getTy()*(-1)-padding)/trans.getMxx()));
        currentview.setXmax((float) (trans.getTx()*(-1)/trans.getMxx()+(getWidth()+ padding)/trans.getMxx()));
        currentview.setYmax((float) (trans.getTy()*(-1)/trans.getMxx()+(getHeight()+ padding)/trans.getMxx()));
   }

    public MapTheme getMapTheme(){return mapTheme;}
}
