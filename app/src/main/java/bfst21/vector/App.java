package bfst21.vector;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    String filename = "data/bornholm.zip";
    View map;

    //Method is being called from Controller if the user changes current OSM file
    public void changeFile(String filePath, Stage primaryStage) throws Exception {
        filename = filePath;
        start(primaryStage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var model = new Model(filename);
        map = new View(model, primaryStage);
    }
}