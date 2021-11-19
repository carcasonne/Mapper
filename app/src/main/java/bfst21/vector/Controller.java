package bfst21.vector;

import bfst21.vector.Dijkstra.*;
import bfst21.vector.Exceptions.AlertBox;
import bfst21.vector.Exceptions.InvalidAddressException;
import bfst21.vector.Exceptions.InvalidPathException;
import bfst21.vector.MapElements.MapFeature;
import bfst21.vector.MapElements.Pointer;
import bfst21.vector.MapElements.Road;
import bfst21.vector.addressparser.Address;
import bfst21.vector.addressparser.Recommender;
import bfst21.vector.osm.Node;
import bfst21.vector.radix.RadixTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;

public class Controller {
    private Model model;
    private View view;
    private Point2D lastMouse;
    public double scalingDouble = 1.06;
    public Label label1 = null;
    public Label label2 = null;
    public String converter = new String("");
    private MapTheme mapTheme;
    private double factor;
    private Recommender recommender;
    private Dijkstra.TransportType transportType = Dijkstra.TransportType.CAR; //default car
    private Coordinate routeCoordinateFrom = null;
    private Coordinate routeCoordinateTo   = null;

    @FXML
    private Button btnPlusZoom;

    @FXML
    private MapCanvas canvas;

    public void init(Model model, View view) { //Initializes important fields for later use
        this.model = model;
        this.view = view;
        canvas.init(model);
        recommender = new Recommender(model.radixTree);
        falsifyLists();
        roadGuideListView.setVisible(false);

        createContextMenu();
        addResizeWindowActionListener();
	}

	//Repaints the map when the user drags the window
    private void addResizeWindowActionListener() {
        view.getStage().widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.smartRepaint();
            canvas.updateView();
        });
        view.getStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.smartRepaint();
            canvas.updateView();
        });
    }

    private void createContextMenu() {
        //context menu (right click)
        //Parent
        javafx.scene.control.Menu addPointerMenu = new javafx.scene.control.Menu("Add Pointer");
        //Children
        javafx.scene.control.MenuItem addPointerMenuItem_home = new javafx.scene.control.MenuItem("Home");
        javafx.scene.control.MenuItem addPointerMenuItem_work = new javafx.scene.control.MenuItem("Work");
        javafx.scene.control.MenuItem addPointerMenuItem_any  = new javafx.scene.control.MenuItem("Favorite");
        addPointerMenu.getItems().addAll(addPointerMenuItem_home, addPointerMenuItem_work, addPointerMenuItem_any);

        javafx.scene.control.MenuItem findRouteFromMenuItem = new javafx.scene.control.MenuItem("Find route from");
        javafx.scene.control.MenuItem findRouteToMenuItem   = new javafx.scene.control.MenuItem("Find route to");
        canvas.getContextMenu().getItems().addAll(addPointerMenu, findRouteFromMenuItem, findRouteToMenuItem);

        canvas.setOnContextMenuRequested(ev ->
                canvas.getContextMenu().show(canvas, ev.getScreenX(), ev.getScreenY())
        );

        ObservableList<String> pointerOptionsList = FXCollections.observableArrayList("Home", "Work", "Favorite");
        canvas.setContextMenuComboBox(new ComboBox<String>(pointerOptionsList));

        addPointerMenuActionListener(addPointerMenuItem_home, "Home");
        addPointerMenuActionListener(addPointerMenuItem_work, "Work");
        addPointerMenuActionListener(addPointerMenuItem_any, "Favorite");
        setFromCoordinateActionListener(findRouteFromMenuItem);
        setToCoordinateActionListener(findRouteToMenuItem);
    }

    public void carRouteActionListener() {
        this.transportType = Dijkstra.TransportType.CAR;
    }

    public void motorlessRouteActionListener() {
        this.transportType = Dijkstra.TransportType.WALK;
    }

    public void driveWalkRouteActionListener() {
        this.transportType = Dijkstra.TransportType.ALL;
    }

    @FXML
    private void streetCloseToPointer(MouseEvent e) throws ClassCastException{ //Finds the closest road to the mouse pointer and returns the name of it to the label
        try {
            lastMouse = new Point2D(e.getX(), e.getY());
            Point2D mouseToCoords = canvas.mouseToModelCoords(lastMouse);
            Road road = (Road) model.kdtree_roads.getNearestMapFeature(mouseToCoords.getX(), mouseToCoords.getY(), true);
            label2.setText(road.getRoadName());
        }catch(ClassCastException a){
            a.getStackTrace();
        }
    }

    private void insertPointer(Pointer newPointer) { //Adds user pointers onto the map
        for(Pointer pointer : model.getPointers()) {
            if(pointer.getType().equals(newPointer.getType())) {
                model.getPointers().remove(pointer);
                break;
            }
        }
        model.getPointers().add(newPointer);
    }

    //Action listener for the menu, containing the pointers
    private void addPointerMenuActionListener(javafx.scene.control.MenuItem addPointerMenuItem, String type) {
        addPointerMenuItem.setOnAction(e -> {
            Point2D mouseToCoords = canvas.mouseToModelCoords(lastMouse);
            Pointer newPointer = new Pointer(mouseToCoords.getX(), mouseToCoords.getY(), type);
            insertPointer(newPointer);

            RadixTree<Coordinate> markersToCoordinate = new RadixTree<Coordinate>();
            Coordinate coordinate = new Coordinate((float) mouseToCoords.getY()*-0.56f, (float) mouseToCoords.getX());

            markersToCoordinate.put("from My Collection", coordinate);

            model.radixTree.put(type +  " ", markersToCoordinate);
            canvas.smartRepaint();
        });
    }

    //Adds "from" pointers to the map
    private void setFromCoordinateActionListener(javafx.scene.control.MenuItem setFromCoordinateMenuItem) {
        setFromCoordinateMenuItem.setOnAction(e -> {
            Point2D mouseToCoords = canvas.mouseToModelCoords(lastMouse);
            routeCoordinateFrom   = new Coordinate((float) mouseToCoords.getX(), (float) mouseToCoords.getY());

            Pointer newPointer = new Pointer(mouseToCoords.getX(), mouseToCoords.getY(), "StartCircle");
            insertPointer(newPointer);

            canvas.smartRepaint();

            try {
                if(routeCoordinateFrom == null) return;
                if(routeCoordinateTo   == null) return;

                drawRouteBetween(routeCoordinateFrom, routeCoordinateTo);
            } catch (Exception exception) {}
        });
    }

    //Adds "to" pointers to the map
    private void setToCoordinateActionListener(javafx.scene.control.MenuItem setToCoordinateMenuItem) {
        setToCoordinateMenuItem.setOnAction(e -> {
            Point2D mouseToCoords = canvas.mouseToModelCoords(lastMouse);
            routeCoordinateTo     = new Coordinate((float) mouseToCoords.getX(), (float) mouseToCoords.getY());

            Pointer newPointer = new Pointer(mouseToCoords.getX(), mouseToCoords.getY(), "EndCircle");
            insertPointer(newPointer);

            canvas.smartRepaint();

            try {
                if(routeCoordinateFrom == null) return;
                if(routeCoordinateTo   == null) return;

                drawRouteBetween(routeCoordinateFrom, routeCoordinateTo);
            } catch (Exception exception) {}
        });
    }

    @FXML
    private void onScroll(ScrollEvent e) { //Scrolls in and out accordingly, and adds a maximum/minimum zoom level
        factor = Math.pow(1.01, e.getDeltaY());

        if(canvas.zoomLevelReturner().getMxx() > 30000){
            if (e.getDeltaY() > 0) {
                factor = 1;
            }
        }
        if(canvas.zoomLevelReturner().getMxx() < 80){
            if (e.getDeltaY() < 0) {
                factor = 1;
            }
        }
        canvas.zoom(factor, new Point2D(e.getX(), e.getY()));

        scalingDouble = 3027.5 / canvas.zoomLevelReturner().getMxx();
        converter = String.format("%.2f", scalingDouble);
        label1.setText(converter + " km");
    }

    @FXML
    private void toDarkMode(){
        mapTheme = canvas.getMapTheme();
        if(mapTheme.getIsDarkMode() == false){
            mapTheme.setThemeToDarkMode();
            label2.setTextFill(Color.WHITE);
            label1.setTextFill(Color.WHITE);
            canvas.smartRepaint();
        } else {
            mapTheme.resetThemeToDefault();
            label2.setTextFill(Color.BLACK);
            label1.setTextFill(Color.BLACK);
            canvas.smartRepaint();
        }
    }

    @FXML
    private void onMouseDragged(MouseEvent e) { //Pans the map
        double dx = e.getX() - lastMouse.getX();
        double dy = e.getY() - lastMouse.getY();
        if (e.isPrimaryButtonDown()) {
            canvas.pan(dx, dy);
        }
        onMousePressed(e);
    }

    @FXML
    private void zoomInButton(MouseEvent e){ //Zooms in on the map when clicking on the button
        double factor = Math.pow(1.01, e.getY());
        if(canvas.zoomLevelReturner().getMxx() > 30000){
            if (e.getY() > 0) {
                factor = 1;
            }
        }
            canvas.zoom(factor, new Point2D(e.getX() + canvas.getWidth()/2, e.getY() + canvas.getHeight()/2));
            scalingDouble = 3027.5 / canvas.zoomLevelReturner().getMxx();
        converter = String.format("%.2f", scalingDouble);
        label1.setText(converter + " km");
        }

    @FXML
    private void zoomOutButton(MouseEvent e){ //Zooms out on the map when clicking on the button
            double factor = Math.pow(0.99, e.getY());
        if(canvas.zoomLevelReturner().getMxx() < 80){
            if (e.getY() > 0) {
                factor = 1;
            }
        }
            canvas.zoom(factor, new Point2D(e.getX() + canvas.getWidth() / 2, e.getY() + canvas.getHeight() / 2));
            scalingDouble = 3027.5 / canvas.zoomLevelReturner().getMxx();
            converter = String.format("%.2f", scalingDouble);
            label1.setText(converter + " km");
    }

    @FXML
    private void onMousePressed(MouseEvent e) { //removes lists and right click bar when the user clicks on the map
        lastMouse = new Point2D(e.getX(), e.getY());
        falsifyLists();
        canvas.smartRepaint();
    }

    //draws route between the two marked points on the map
    private void drawRouteBetween(Coordinate from, Coordinate to) throws Exception {
        MapFeature road_from = model.kdtree_roads.getNearestMapFeature(from.x(), from.y(), true);
        MapFeature road_to = model.kdtree_roads.getNearestMapFeature(to.x(), to.y(), true);

        Node nearestNode_from = road_from.nearestNodeToPoint(new Coordinate((float) from.x(), (float) from.y()));
        Node nearestNode_to   = road_to.nearestNodeToPoint(new Coordinate((float) to.x(), (float) to.y()));

        String coord_from = new Coordinate(nearestNode_from.getX(), nearestNode_from.getY()).toString();
        String coord_to   = new Coordinate(nearestNode_to.getX(), nearestNode_to.getY()).toString();

        GraphNode graphNode_from = model.graph.getAllVertices().get(model.graph.getIDToNodeMap().get(coord_from));
        GraphNode graphNode_to   = model.graph.getAllVertices().get(model.graph.getIDToNodeMap().get(coord_to));

        //update model's shortest path
        Dijkstra pathToMouse = new Dijkstra(model.graph, graphNode_from, graphNode_to, transportType);
        model.shortestPath = pathToMouse.pathTo(graphNode_to);

        destinationGuide(); //get navigation
        canvas.smartRepaint();

        double distance = 0.0;
        for(DirectedEdge ed : model.shortestPath) distance += ed.weight();
    }

    @FXML TextField searchField;
    @FXML TextField endAddressField;
    public void searchRoute(MouseEvent event) { //Takes user input and parses it to one of the correct addresses or the user-made pointers
        try {
            String startDest = searchField.getText();
            String endDest = endAddressField.getText();

        Address address_from = Address.parse(startDest);
        Address address_to   = Address.parse(endDest);
            if (address_from.toString().length() == 0 || address_to.toString().length() == 0) {
                throw new InvalidAddressException();
            }

        Coordinate coordinate_from = null;
        Coordinate coordinate_to   = null;

        if(startDest.equals("Work ") || startDest.equals("Home ") || startDest.equals("Favorite ")) {
            coordinate_from = model.radixTree.get(startDest).get("from My Collection");
        } else{
            coordinate_from = model.radixTree.get(address_from.getStreet()).get(address_from.housePostcodeCityString());
        }

        if(endDest.equals("Work ") || endDest.equals("Home ") || endDest.equals("Favorite ")) {
            coordinate_to = model.radixTree.get(endDest).get("from My Collection");
        } else{
            coordinate_to   = model.radixTree.get(address_to.getStreet()).get(address_to.housePostcodeCityString());

        }

        //Calculates which address is closest to the specified point
        MapFeature road_from = model.kdtree_roads.getNearestMapFeature(coordinate_from.y(), coordinate_from.x()/-0.56f, true);
        MapFeature road_to   = model.kdtree_roads.getNearestMapFeature(coordinate_to.y(), coordinate_to.x()/-0.56f, true);
        Node nearestNode_from = road_from.nearestNodeToPoint(new Coordinate((float) coordinate_from.y(), (float) coordinate_from.x()/-0.56f));
        Node nearestNode_to   = road_to.nearestNodeToPoint(new Coordinate((float) coordinate_to.y(), (float) coordinate_to.x()/-0.56f));


            String vertexLocation_from = new Coordinate(nearestNode_from.getX(), nearestNode_from.getY()).toString();
            String vertexLocation_to = new Coordinate(nearestNode_to.getX(), nearestNode_to.getY()).toString();


            GraphNode fromNode = model.graph.getAllVertices().get(model.graph.getIDToNodeMap().get((vertexLocation_from)));
            GraphNode toNode = model.graph.getAllVertices().get(model.graph.getIDToNodeMap().get((vertexLocation_to)));

            Dijkstra pathToMouse = new Dijkstra(model.graph, fromNode, toNode, transportType);

            model.shortestPath = pathToMouse.pathTo(toNode);
            canvas.smartRepaint();

        model.radixTree.get(endAddressField.getText());

        //Handles exceptions in case the user writes invalid input
            destinationGuide();
        }catch(InvalidAddressException e) {
            AlertBox a = new AlertBox();
            a.addressAlert();
        }catch (InvalidPathException e){
            AlertBox b = new AlertBox();
            b.pathAlert();
        }catch (Exception e) {
            if (e.getMessage().equals("Cannot invoke \"bfst21.vector.Dijkstra.Coordinate.y()\" because \"coordinate_from\" is null")){
                AlertBox a = new AlertBox();
                a.addressAlert();
            }
        }
    }

    @FXML
    ListView<String> roadGuideListView = new ListView<>();
    private void destinationGuide(){ //Displays the navigation list for the user
        RoadDirectionGuide guide = new RoadDirectionGuide(model.getShortestPath());
        roadGuideListView.getItems().clear();
        roadGuideListView.setVisible(true);
        for (String s : guide.outputList) {
            roadGuideListView.getItems().add(s);
        }
    }

    @FXML
    ListView<String> fromListView = new ListView<>();
    public void recommendFromAddress(KeyEvent event) { //Displays the recommendations in the "From" field
        try {
            falsifyLists();
            fromListView.getItems().clear();
            if (searchField.getText().length() > 0) {
                var recommendedList = recommender.recommendations(searchField.getText());
                for (String s : recommendedList) {
                    fromListView.getItems().add(s);
                }
            }

            if (fromListView.getItems().size() > 0) {
                fromListView.setVisible(true);
            }
        }catch (Exception e){}
    }

    @FXML
    ListView<String> toListView = new ListView<>();
    public void recommendToAddress(KeyEvent event) { //Displays the recommendations in the "To" field
        try {
            falsifyLists();
            toListView.getItems().clear();
            if (endAddressField.getText().length() > 0) {
                toListView.setVisible(true);
                var recommendedList = recommender.recommendations(endAddressField.getText());
                for (String s : recommendedList) {
                    toListView.getItems().add(s);
                }
            }
        } catch (Exception e) {}
    }

    @FXML
    public void hideRoadGuide(KeyEvent k){ //Un-displays the recommender lists if the user removes the text in any of the fields
        if (searchField.getText().isEmpty() || endAddressField.getText().isEmpty() ){
            roadGuideListView.setVisible(false);
        }
    }

    @FXML
    private void selectFromAddress(MouseEvent e){ //Makes the user able to select the recommendations from the "From" list
        try{
            String selected = fromListView.getSelectionModel().getSelectedItem();
            if (!selected.equals("")){
                searchField.setText(selected);
            }
            toListView.setVisible(false);
            searchField.requestFocus();
            searchField.selectEnd();
            if (searchField.getText().matches(".*\\d.*")) {
                fromListView.setVisible(false);
            }
        }catch (Exception a){}
    }

    @FXML
    private void selectToAddress(MouseEvent e){ //Same as above but for the "To" list
        try {
            String selected = toListView.getSelectionModel().getSelectedItem();
            if (!selected.equals("")){
                endAddressField.setText(selected);
            }
            fromListView.setVisible(false);
            endAddressField.requestFocus();
            endAddressField.selectEnd();
            if (endAddressField.getText().matches(".*\\d.*")) {
                toListView.setVisible(false);
            }
        }catch (Exception a){}
    }

    //Removes the visibility for the recommendation lists
    public void falsifyLists(){
        toListView.setVisible(false);
        fromListView.setVisible(false);
    }

    //Allows the user to load in a new file while the program is running, as long as the file is an "OSM", "TXT" or "ZIP" file, (With the ZIP containing a valid file)
    public void loadNewFile(ActionEvent actionEvent) {
        AlertBox a = new AlertBox();

        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        String file = selectedFile.getAbsolutePath();
        if (file.endsWith(".osm") || file.endsWith(".txt")) {
            try {
                view.closeCurrentView();
                App app = new App();
                app.changeFile(file, new Stage());
            } catch (Exception e) {
                a.fileAlert();
            }
        } else if (file.endsWith(".zip")) {
            try {
                var zip = new ZipInputStream(new FileInputStream(file));
                zip.getNextEntry();
                view.closeCurrentView();
                App app = new App();
                app.changeFile(file, new Stage());
            } catch (Exception e) {
                a.fileAlert();
            }
        } else {
            a.fileAlert();
        }
    }
}